/*  @Author: Haotian Bai @Github: https://github.com/164140757 @Date: 2020-05-09
 *  12:16:24 @LastEditors: Haotian Bai @LastEditTime: 2020-05-11 22:02:52 @FileP
 * ath: \PLO\src\main\java\Cores\Parser.java @Description:
 */
package Cores;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import Exceptions.GrammarError;
import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.Token.NonTerminal;
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
    HashMap<Production, Set<Token>> selectMap;
    private final int tableSize = 100;
    private Vector<String[]> analyseTable;

    public Parser(Grammar grammar, Lexer lexer) {
        assert (grammar != null);
        assert (lexer != null);
        this.lexer = lexer;
        this.grammar = grammar;

        tokenStack = new Stack<>();
        tokenStack.add(new Token("#"));
        tokenStack.add(grammar.getStartToken());
        productions = grammar.getProductionsMap();
        first = new HashMap<>();
        firstEmpty = new HashMap<>();
        follow = new HashMap<>();
        followSetChanged = new HashMap<>();
        selectMap = new HashMap<>();

    }

    public Parser(Grammar grammar) {
        assert (grammar != null);
        this.grammar = grammar;
        tokenStack = new Stack<>();
        tokenStack.add(grammar.getStartToken());
        productions = grammar.getProductionsMap();
        first = new HashMap<>();
        firstEmpty = new HashMap<>();
        follow = new HashMap<>();
        followSetChanged = new HashMap<>();
        selectMap = new HashMap<>();
    }

    public void run() throws IOException, GrammarError {
        getFirst();
        getFollow();
        getSelect();
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
        // give back
        tmpFirstSet.addAll(firstSet);
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
                if (token.context.equals("#")) {
                    tmpFirstSet.add(token);
                    firstSet.add(token);
                    pEmpty = true;
                    // go on
                    break;
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
                        // clean tmp

                    } else {
                        Production p_ = productions.get(token.toNonTerminal());
                        pEmpty = checkNonTerminal(p_, tmpFirstSet);
                        // add tmpFirstSet to FirstSet before removing tmpFirstSet for next input
                        firstSet.addAll(tmpFirstSet);
                        // check if it contains empty, yes -> go on, no-> break;
                        // clean tmp
                    }
                    if (!(tmpFirstSet.contains(new Token("#")))) {
                        // clean tmp
                        tmpFirstSet.clear();
                        break;
                    }
                    tmpFirstSet.clear();
                    if (pEmpty) {
                        emptyNum++;
                        // remove empty as there is a choice and it's not the first level
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
                                    Set<Token> set = new HashSet<>(first.get(n.toNonTerminal()));
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
                                        if (!token.context.equals("#"))
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
        assert (!first.isEmpty() && !follow.isEmpty());
        // iterate all productions
        for (Entry<NonTerminal, Production> production : productions.entrySet()) {
            NonTerminal k = production.getKey();
            Production v = production.getValue();
            // split productions
            for (Vector<Token> p : v.target) {
                // check if the target production is empty
                HashSet<Token> firstSet = new HashSet<>();
                Set<Token> tmpFirstSet = new HashSet<>();
                Production pNew = new Production(k, p);
                boolean isEmpty = checkProductionFirst(firstSet, tmpFirstSet, pNew);
                if (isEmpty) {
                    firstSet.remove(new Token("#"));
                    firstSet.addAll(follow.get(k));
                }
                selectMap.put(pNew, firstSet);
            }
        }

    }

    /**
     * Use it after all three steps above have been finished.
     *
     * -1 : overlap
     * 0 : grammar fail
     * 1 : win
     * @throws IOException
     */
    public int LL() throws IOException {
        assert (first != null && follow != null && selectMap != null);
        analyseTable = new Vector<>();
        if(!checkLL()){
            return -1;
        }
//        checkLL();
        Token t = tokenStack.peek();
        Token scan = getScanNext();
        int status = 0;
        while (!tokenStack.isEmpty()) {
            status = 0;
            if(t.context.equals("#")){
                status = 1;
                break;
            }
            if(t.equals(scan)){
                status = 1;
                tokenStack.pop();
                scan = getScanNext();
                t = tokenStack.peek();
            }else{
                // ID
                if(t.context.equals("ID")&&scan.type.equals("identifiers")){
                    status = 1;
                    tokenStack.pop();
                    scan = getScanNext();
                    t = tokenStack.peek();
                }
                //NUM
                if(t.context.equals("N")&&scan.type.equals("NUM")){
                    status = 1;
                    tokenStack.pop();
                    scan = getScanNext();
                    t = tokenStack.peek();
                }
                // check after scan
                if(scan == null){
                    status = 1;
                    break;
                }
            }
            for (Entry<Production, Set<Token>> map : selectMap.entrySet()) {
                Production k = map.getKey();
                Set<Token> toCheck = map.getValue();
                // find a way out
                if (k.index.equals(t.toNonTerminal()) && checkExist(scan, toCheck)) {
                    status = 1;
                    String[] row = new String[4];
                    row[0] = String.valueOf(analyseTable.size() + 1);
                    row[1] = setConcat(tokenStack);
                    row[2] = scan.context;
                    row[3] = k.index.context+"→" + setConcat(k.target.get(0));
                    analyseTable.add(row);
                    // A -> #
                    if(k.target.get(0).get(0).context.equals("#")){
                        tokenStack.pop();
                        t = tokenStack.peek();
                        break;
                    }
                    // reverse
                    tokenStack.pop();
                    Vector<Token> rt = new Vector<>(k.target.get(0));
                    Collections.reverse(rt);
                    tokenStack.addAll(rt);
                    // new t to check
                    t = tokenStack.peek();
                    break;
                }
            }
            if(status==0){
                return status;
            }
        }
        // check lexer
        if (lexer.scan() != null) {
            status = 0;
        }

        return status;
    }

    private boolean checkLL() {
        // overlap for productions starting with same indexes
        Map<NonTerminal, List<Entry<Production, Set<Token>>>> res = selectMap.entrySet().stream().collect(Collectors.groupingBy(productionSetEntry -> productionSetEntry.getKey().index));
        for (Entry<NonTerminal, List<Entry<Production, Set<Token>>>> entry : res.entrySet()) {
            List<Entry<Production, Set<Token>>> v = entry.getValue();
            HashSet<Token> set = new HashSet<>(v.get(0).getValue());
            HashSet<Token> overlap = new HashSet<>(set);
            v.remove(0);
            for (Entry<Production, Set<Token>> tokens : v) {
                overlap.retainAll(tokens.getValue());
                if (overlap.size()!=0) {
                    return false;
                }
                set.addAll(tokens.getValue());
            }
        }
        return true;
    }

    private String setConcat(Collection<Token> tokenStack) {
        StringBuilder buffer = new StringBuilder();
        for (Token token : tokenStack) {
            buffer.append(token.context).append(" ");
        }

        return buffer.toString();
    }

    private Token getScanNext() throws IOException {
        Token tp = lexer.scan();
        return tp;
    }

    // only check context
    private boolean checkExist(Token scan, Set<Token> toCheck) throws IOException {
        if (toCheck.contains(scan)) {
            return true;
        } else if (Production.preDefined != null ) {
            // additional definitions
          if(scan.type.equals("identifiers")&&checkSelect(toCheck,"ID")){
            return true;
          }
            return checkSelect(toCheck,"N")&& scan.type.equals("NUM");
        }
        return false;
    }

    // check only name and ignore the class
    private boolean checkSelect(Set<Token> toCheck, String n) {
        for (Token token : toCheck) {
            if(token.context.equals(n)){
                return true;
            }
        }
        return false;
    }

//    private void checkLL() {
//        assert(select!=null);
//        for(NonTerminal t : first.keySet()){
//            Set<Token> set = new HashSet<>();
//            for (Entry<Production,Set<Token>> map: select.entrySet()) {
//                Production k = map.getKey();
//                Set<Token> tokens = map.getValue();
//                if(k.index.equals(t)){
//                    if(set.size() == 0){
//                        set.addAll(tokens);
//                    }
//                    // set contains the same token in the previous result
//                    else{
//                        if(!set.addAll(tokens)){
//                           towrite.add("Your input grammar is wrong!");
//                        }
//                    }
//                }
//            }
//        }
//    }

    boolean isTer(Token t) {
        return !t.type.equals("NonTerminal");
    }


    void printHashSet(Set<Token> set) {
        set.forEach(t -> {
            System.out.print(t.context + " ");
        });
    }

    public void printFirst() {
        System.out.println("First:");
        first.forEach((k, v) -> {
            System.out.print(k.context + ":{");
            printHashSet(v);
            System.out.print("}\t");
        });
        System.out.println("\n");
    }

    public void printFollow() {
        System.out.println("Follow:");
        follow.forEach((k, v) -> {
            System.out.print(k.context + ":{");
            printHashSet(v);
            System.out.print("}\t");
        });
        System.out.println("\n");
    }

    public void printSelect() {

        selectMap.forEach((k, v) -> {
            StringBuilder sBuilder = new StringBuilder();
            k.target.forEach(vt -> {
                vt.forEach(t -> {
                    sBuilder.append(t.context);
                });
            });
            System.out.print(k.index.context + "->{" + sBuilder.toString() + "}:<");
            printHashSet(v);
            System.out.print(">\n");
        });
        System.out.println("\n");
    }

    public HashMap<Production, Set<Token>> getSelectMap() {
        return selectMap;
    }

    public Vector<String[]> getAnalyseTable() {
        return analyseTable;
    }
}
