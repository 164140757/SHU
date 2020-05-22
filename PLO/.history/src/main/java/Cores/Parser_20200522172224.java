/*  @Author: Haotian Bai @Github: https://github.com/164140757 @Date: 2020-05-09
 *  12:16:24 @LastEditors: Haotian Bai @LastEditTime: 2020-05-11 22:02:52 @FileP
 * ath: \PLO\src\main\java\Cores\Parser.java @Description:
*/
package Cores;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import java.util.Map.Entry;

import Exceptions.GrammarError;
import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.Token.NonTerminal;
import Utils.Token.Terminal;
import Utils.Token.Token;

public class Parser {

    // ready to analyse
    Stack<Token> tokenStack;

    // grammar
    Grammar grammar;
    // Lexer
    Lexer lexer;
    // map for first and follow
    HashMap<NonTerminal, Set<Token>> first;
    HashMap<NonTerminal, Boolean> firstEmpty;
    HashMap<NonTerminal, Set<Token>> follow;
    HashMap<NonTerminal, Boolean> followSetChanged;
    HashMap<NonTerminal, Production> productions;
    HashMap<Production, Set<Token>> select;

    public Parser(Grammar grammar, Lexer lexer) {
        assert (grammar != null);
        assert (lexer != null);
        this.lexer = lexer;
        this.grammar = grammar;
        tokenStack = new Stack<>();
        tokenStack.add(new Token("Terminal", "!"));
        tokenStack.add(grammar.getStartToken());
        productions = grammar.getProductionsMap();
        first = new HashMap<>();
        firstEmpty = new HashMap<>();
        follow = new HashMap<>();
        followSetChanged = new HashMap<>();
        select = new HashMap<>();
    }

    public Parser(Grammar grammar) {
        assert (grammar != null);
        this.grammar = grammar;
        tokenStack = new Stack<>();
        tokenStack.add(new Token("Terminal", "!"));
        tokenStack.add(grammar.getStartToken());
        productions = grammar.getProductionsMap();
        first = new HashMap<>();
        firstEmpty = new HashMap<>();
        follow = new HashMap<>();
        followSetChanged = new HashMap<>();
        select = new HashMap<>();
    }

    /**
     * make sure their is no left recursion before using this class.
     */
    public void getFirst() {
        // iterate all productions
        productions.forEach((k, v) -> {
            checkNonTerminal(v, new HashSet<>());
        });
    }

    /**
     * Use after making every index decide one production Recursively check
     * NonTerminal t's targets, and find the NonTerminals and add with hashmap to
     * save and facilitate.
     *
     * @param production  production to observe
     * @param tmpFirstSet results
     * @return has empty?
     */
    private boolean checkNonTerminal(Production production, Set<Token> tmpFirstSet) {
        // firstSet(not cleaned)
        HashSet<Token> firstSet = new HashSet<>();
        // firstSet for all productions starting with the same index and tmpFirstSet for
        // only one production terminal itself
        if (isTer(production.index)) {
            tmpFirstSet.add(production.index);
            return false;
        }
        // index is not terminal if there is one in first set map, then skip
        if (first.containsKey(production.index)) {
            // firstSet add
            firstSet.addAll(first.get(production.index));
            // empty map
            return firstEmpty.get(production.index);
        }
        boolean pEmpty = checkProductionFirst(firstSet, tmpFirstSet, production);
        // add to first map
        first.put(production.index, firstSet);
        firstEmpty.put(production.index, pEmpty);
        return pEmpty;
    }

    private boolean checkProductionFirst(HashSet<Token> firstSet, Set<Token> tmpFirstSet, Production production) {
        boolean pEmpty = false;
        // clean first check target, all productions with the same index
        for (Vector<Token> tokens : production.target) {
            // check a certain production

            int emptyNum = 0;
            // check every possible token for a given production
            for (Token token : tokens) {
                // only A -> #
                if (token.sign.equals("#")) {
                    tmpFirstSet.add(token);
                    firstSet.add(token);
                    pEmpty = true;
                    // go on
                    continue;
                }
                if (isTer(token)) {
                    // the token deduces terminal add to firstSet
                    tmpFirstSet.add(token);
                    firstSet.add(token);
                    // then break the loop and check another production if there is one
                    break;
                } else {
                    // nonTerminal and cannot deduce empty if it's in the map
                    Set<Token> tmp = first.get(token.toNonTerminal());
                    if (tmp != null) {
                        if (tmp.contains(new Token("#"))) {
                            pEmpty = true;
                        }
                        tmpFirstSet.addAll(tmp);
                        firstSet.addAll(tmp);
                        // check if it contains empty, yes -> go on, no-> break;
                        if (!(tmpFirstSet.contains(new Token("#")))) {
                            // clean tmp
                            tmpFirstSet.clear();
                            break;
                        }

                        // clean tmp
                        tmpFirstSet.clear();

                    } else {
                        Production p_ = productions.get(token.toNonTerminal());
                        pEmpty = checkNonTerminal(p_, tmpFirstSet);
                        // add tmpFirstSet to FirstSet before removing tmpFirstSet for next input
                        firstSet.addAll(tmpFirstSet);
                        // check if it contains empty, yes -> go on, no-> break;
                        if (!(tmpFirstSet.contains(new Token("#")))) {
                            // clean tmp
                            tmpFirstSet.clear();
                            break;
                        }

                        // clean tmp
                        tmpFirstSet.clear();
                    }
                    if (pEmpty) {
                        emptyNum++;
                        // remove empty as there is a choice and it's not the first level
                        tmpFirstSet.remove(new Token("#"));
                        firstSet.remove(new Token("#"));
                    }
                }
            }
            // when all choices contain a empty
            if (emptyNum == tokens.size()) {
                tmpFirstSet.add(new Token("#"));
                firstSet.add(new Token("#"));
            }

        }
        return pEmpty;
    }

    /**
     * getFirst() should be used for calling this Start index given '!' as the
     * parentheses,'#' as empty
     */
    public void getFollow() {
        assert (!first.isEmpty());
        boolean changed = false;
        do {
            // update status
            followSetChanged = new HashMap<>();
            // start '!'
            Set<Token> s = follow.get(grammar.getStartToken());
            if (s == null) {
                s = new HashSet<>();
            }
            s.add(new Token("!"));
            follow.put(grammar.getStartToken(), s);
            // production with index k
            for (Entry<NonTerminal, Production> e : productions.entrySet()) {
                NonTerminal k = e.getKey();
                // index set
                Set<NonTerminal> keySet = first.keySet();
                Production v = e.getValue();
                // a production
                for (Vector<Token> production : v.target) {
                    // a token
                    for (int i = 0; i < production.size(); i++) {
                        Token t = production.get(i);
                        // only for keySet
                        if (keySet.contains(t.toNonTerminal())) {
                            // remember to change the type of equal()will get wrong.
                            NonTerminal tt = t.toNonTerminal();
                            // check i last one
                            if (i == production.size() - 1) {
                                Set<Token> set = follow.get(tt);
                                if (set == null) {
                                    set = new HashSet<>();
                                }
                                Set<Token> stmp = follow.get(k);
                                if (stmp == null) {
                                    stmp = new HashSet<>();
                                }
                                // record
                                followSetChanged.put(tt, set.addAll(stmp));
                                follow.put(tt, set);
                            } else {
                                // terminal next
                                Token n = production.get(i + 1);
                                if (isTer(n)) {
                                    Set<Token> set = follow.get(tt);
                                    if (set == null) {
                                        set = new HashSet<>();
                                    }
                                    // record
                                    followSetChanged.put(tt, set.add(n));
                                    follow.put(tt, set);
                                    // nonTerminal
                                } else {
                                    // next
                                    Set<Token> set = first.get(n.toNonTerminal());
                                    Set<Token> stmp = follow.get(tt);
                                    if (stmp == null) {
                                        stmp = new HashSet<>();
                                    }
                                    // if there's empty
                                    if (set.contains(new Token("#"))) {
                                        // add follow index
                                        Set<Token> tmp = follow.get(k);
                                        if (tmp == null) {
                                            tmp = new HashSet<>();
                                        }
                                        set.addAll(tmp);
                                    }
                                    // for(Iterator<Token> iterator = set.iterator();iterator.hasNext();){ Token
                                    // token = iterator.next(); if(token.context == "#"){
                                    // iterator.remove(); } } I leave the code here to suggest that iterator
                                    // will remove everything from source[HashMap first] be careful filter not empty
                                    Set<Token> tmp = new HashSet<>();
                                    for (Token token : set) {
                                        if (token.sign != "#")
                                            tmp.add(token);
                                    }
                                    set = tmp;
                                    // record
                                    followSetChanged.put(tt, stmp.addAll(set));
                                    follow.put(tt, stmp);
                                }
                            }
                        }
                    }
                }

            }

            // get changed
            changed = followSetChanged.containsValue(true);
        } while (changed);
    }

    /**
     * should make sure getFirst and getFollow are done before.
     */
 public void getSelect() {
    assert(!first.isEmpty() && !follow.isEmpty());
    // iterate all productions
    for (Entry<NonTerminal, Production> production : productions.entrySet()) {
        NonTerminal k = production.getKey();
        Production v = production.getValue();
        // split productions
        for (Vector<Token> p : v.target){
            // check if the target production is empty
            HashSet<Token> firstSet = new HashSet<>();
            Set<Token> tmpFirstSet = new HashSet<>();
            Production pNew = new Production(k, p);
            boolean isEmpty = checkProductionFirst(firstSet, tmpFirstSet, pNew);
            if (isEmpty) {
                firstSet.remove(new Token("#"));
                firstSet.addAll(follow.get(k));
            }
            select.put(pNew, firstSet);
        }
    }

}

    /**
     * Use it after all three steps above have been finished.
     * 
     * @throws IOException
     * @throws GrammarError
     */
    void LL() throws IOException, GrammarError {
        assert(first!=null && follow!=null &&select!= null);
        // check LL 
        checkLL();
        Token t = tokenStack.pop();
        Terminal tp = (Terminal)lexer.scan();
        while(!isEmpty(t)){
            NonTerminal tt = t.toNonTerminal();
            boolean fail = true;
            for (Entry<Production,Set<Token>> map: select.entrySet()) {
                Production k = map.getKey();
                Set<Token> toCheck = map.getValue();
                // find a way out
                if(k.index.equals(tt)&&checkExist(tp,toCheck)){
                    fail = false;
                    // change stack, select contains single production
                    // reverse
                    Stack<Token> tpStack = new Stack<>();
                    Vector<Token> tpVector = k.target.get(0);
                    for (int i = tpVector.size()-1;i >= 0; i--) {
                        tpStack.add(tpVector.get(i));
                    }
                    tokenStack.addAll(tpStack);
                    // scan next
                    if(k.target.get(0).get(0).sign.equals(tp.context)){
                        tp = (Terminal)lexer.scan();
                        // clean 
                        t = tokenStack.pop();
                    }
                    break;
                }
            }
            //check additional definitions
            if(Production.preDefined.containsKey(t.sign.charAt(0))){
                String className = Production.preDefined.get(t.sign.charAt(0)).getName();
                if(tp.getClass().getName().equals(className)){
                    fail = false;
                    tp = (Terminal)lexer.scan();
                    // clean 
                    t = tokenStack.pop();
                }
            }
            if(fail){
                throw new GrammarError("You input doesn't map the grammar you defined.");
            }
            t = tokenStack.pop();
            // empty 
            if(t.sign.equals("#")){
                t = tokenStack.pop();
            }
        }
        // check lexer 
        if(lexer.scan()!=null){
            throw new GrammarError("You input doesn't map the grammar you defined.");
        }
    }
    // only check context 
    private boolean checkExist(Terminal input, Set<Token> toCheck) {
        for (Token token : toCheck) {
            if(token.sign.equals(input.context)){
                return true;
            }
        }
        for (Token token : toCheck) {
            if(Production.preDefined!=null){
                // additional definitions
                if(Production.preDefined.containsKey(token.sign.charAt(0))){
                    String className = Production.preDefined.get(token.sign.charAt(0)).getName();
                    if(input.getClass().getName().equals(className)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void checkLL() throws GrammarError {
        assert(select!=null);
        for(NonTerminal t : first.keySet()){
            Set<Token> set = new HashSet<>();
            for (Entry<Production,Set<Token>> map: select.entrySet()) {
                Production k = map.getKey();
                Set<Token> tokens = map.getValue();
                if(k.index.equals(t)){
                    if(set.size() == 0){
                        set.addAll(tokens);
                    }
                    // set contains the same token in the previous result
                    else{
                        if(!set.addAll(tokens)){
                            throw new GrammarError("Your input grammars don't follow LL Standard.");
                        }
                    }
                }
            }
        }
    }

    boolean isTer(Token t) {
        return t.type != "NonTerminal";
    }

    boolean isEmpty(Token t){
        return t.sign.equals("#");
    }
    void printHashSet(Set<Token> set){
        set.forEach(t->{
            System.out.print(t.sign+" ");
        });
    }
    void printFirst(){
        System.out.println("First:");
        first.forEach((k,v)->{
            System.out.print(k.sign+":{");
            printHashSet(v);
            System.out.print("}\t");
        });
        System.out.println("\n");
    }
    void printFollow(){
        System.out.println("Follow:");
        follow.forEach((k,v)->{
            System.out.print(k.sign+":{");
            printHashSet(v);
            System.out.print("}\t");
        });
        System.out.println("\n");
    }
    void printSelect(){

        select.forEach((k,v)->{
            StringBuilder sBuilder = new StringBuilder();
            k.target.forEach(vt->{
                vt.forEach(t->{
                    sBuilder.append(t.sign);
                });
            });
            System.out.print(k.index.sign+"->{"+sBuilder.toString()+"}:<");
            printHashSet(v);
            System.out.print(">\n");
        });
        System.out.println("\n");
    }
}
