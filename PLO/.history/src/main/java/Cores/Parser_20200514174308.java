/*  @Author: Haotian Bai @Github: https://github.com/164140757 @Date: 2020-05-09
 *  12:16:24 @LastEditors: Haotian Bai @LastEditTime: 2020-05-11 22:02:52 @FileP
 * ath: \PLO\src\main\java\Cores\Parser.java @Description:

 */
/*  @Author: Haotian Bai @Github: https://github.com/164140757 @Date: 2020-05-09
 *  12:16:24 @LastEditors: Haotian Bai @LastEditTime: 2020-05-11 19:13:38 @FileP
 * ath: \PLO\src\main\java\Cores\Parser.java @Description:

 */
package Cores;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import java.util.Map.Entry;

import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.Token.NonTerminal;
import Utils.Token.Token;

public class Parser {

    // ready to analyse
    Stack<Token> stack;
    // predicting table
    HashMap<Token, Production> parsingTable;
    // grammar
    Grammar grammar;
    // map for first and follow
    HashMap<NonTerminal, Set<Token>> first;
    HashMap<NonTerminal, Boolean> firstEmpty;
    HashMap<NonTerminal, Set<Token>> follow;
    HashMap<NonTerminal, Boolean> followSetChanged;
    HashMap<NonTerminal, Production> productions;

    public Parser(Grammar grammar) {
        stack = new Stack<>();
        stack.add(new Token("Terminal", "#"));
        parsingTable = new HashMap<>();
        this.grammar = grammar;
        productions = grammar.getProductionsMap();
        first = new HashMap<>();
        firstEmpty = new HashMap<>();
        follow = new HashMap<>();
        followSetChanged = new HashMap<>();
    }

    Production parse(Token t) {
        return parsingTable.get(t);
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
        boolean pEmpty = false;

        // clean first check target, all productions with the same index
        for (Vector<Token> tokens : production.target) {
            // check a certain production

            int emptyNum = 0;
            // check every possible token for a given production
            for (Token token : tokens) {
                // only A -> #
                if (token.context.equals("#")) {
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
        // add to first map
        first.put(production.index, firstSet);
        firstEmpty.put(production.index, pEmpty);
        return pEmpty;
    }

    /**
     * getFirst() should be used for calling this 
     * Start index given '!' as the parentheses,'#' as empty
     */
    public void getFollow() {
        boolean changed = false;
        do{
            // update status
            followSetChanged = new HashMap<>();
            // start '!'
            Set<Token> s =  follow.get(grammar.getStartToken());
            if(s == null){
                s = new HashSet<>();
            }
            s.add(new Token("!"));
            follow.put(grammar.getStartToken(),s);
            // production with index k
            for (Entry<NonTerminal, Production> e : productions.entrySet()) {
                NonTerminal k = e.getKey();
                // index set
                Set<NonTerminal> keySet = first.keySet();
                Production v = e.getValue();
                 // a production
                 for (Vector<Token> production :v.target) {
                     // a token
                     for (int i = 0; i < production.size(); i++) {
                        Token t = production.get(i);
                         // only for keySet
                         if (keySet.contains(t.toNonTerminal())) {
                             // remember to change the type of equal()will get wrong.
                             NonTerminal tt = t.toNonTerminal();
                             // check i last one
                             if(i==production.size()-1){
                                Set<Token> set = follow.get(tt);
                                if(set==null){
                                    set = new HashSet<>();
                                }
                                Set<Token> stmp = follow.get(k);
                                if(stmp==null){
                                    stmp = new HashSet<>();
                                }
                                // record
                                followSetChanged.put(tt,set.addAll(stmp));
                                follow.put(tt,set);
                             }
                            else{
                                 //terminal
                                 // next
                                 Token n = production.get(i+1);
                                 if(isTer(n)){
                                    Set<Token> set = follow.get(tt);
                                    if(set==null){
                                        set = new HashSet<>();
                                    }
                                    // record
                                    followSetChanged.put(tt,set.add(n));
                                    follow.put(tt,set);
                                 }
                                 // nonTerminal first
                                 else{
                                     // next
                                    Set<Token> set = first.get(n.toNonTerminal());
                                    Set<Token> stmp = follow.get(tt);
                                    if(stmp==null){
                                        stmp = new HashSet<>();
                                    }
                                     // if there's empty
                                     if(set.contains(new Token("#"))){
                                        // add follow index 
                                        Set<Token> tmp = follow.get(k);
                                        if(tmp == null){
                                            tmp = new HashSet<>();
                                        }
                                        set.addAll(tmp);
                                    }
                                    //   for(Iterator<Token> iterator = set.iterator();iterator.hasNext();){
                                    //     Token token = iterator.next();
                                    //     if(token.context == "#"){
                                    //         iterator.remove();
                                    //     }
                                    // }
                                    // I leave the code here to suggest that iterator will remove everything from source[HashMap first]
                                    // be careful

                                    // filter not empty
                                    Set<Token> tmp = new HashSet<>();
                                    for(Token token : set){
                                        if(token.context != "#")
                                        tmp.add(token);
                                    }
                                    set = tmp;
                                    // record
                                    followSetChanged.put(tt,stmp.addAll(set));
                                    follow.put(tt,stmp);
                                 }
                             }
                         }
                    }
                 } 
            
            }
    
            // get changed 
            changed = followSetChanged.containsValue(true);
        }while(changed);
    }
    /**
     * should make sure getFirst and getFollow are done before.
     */
    public void getSelect() {
        
	}


    boolean isTer(Token t) {
        return t.type != "NonTerminal";
    }


}