/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 12:16:24
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-11 19:21:01
 * @FilePath: \PLO\src\main\java\Cores\Parser.java
 * @Description: 
 */
/*  @Author: Haotian Bai @Github: https://github.com/164140757 @Date: 2020-05-09
 *  12:16:24 @LastEditors: Haotian Bai @LastEditTime: 2020-05-11 19:13:38 @FileP
 * ath: \PLO\src\main\java\Cores\Parser.java @Description:

 */
package Cores;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;

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
    HashMap<Token, Set<Token>> first;
    HashMap<Token, Set<Token>> follow;
    HashMap<NonTerminal, Production> productions;

    public Parser(Grammar grammar) {
        stack = new Stack<>();
        stack.add(new Token("Terminal", "#"));
        parsingTable = new HashMap<>();
        this.grammar = grammar;
        productions = grammar.getProductionsMap();
    }

    Production parse(Token t) {
        return parsingTable.get(t);
    }
    /**
 * make sure their is no left recursion
 * @param t token to process
 */
    void getFirst(Token t) {
        Set<Token> firstSet = new HashSet<>();
        // terminal itself
        if (isTer(t)) {
            firstSet.add(t);
        }
        // non terminal
        Production production = productions.get(t.context);
        checkNonTerminal(production,firstSet);

    }

    private void checkNonTerminal(Production production,Set<Token> firstSet) {
        // all empty
        boolean allEmpty = false;
        // check target
        for (Vector<Token> tokens : production.target) {
            for (Token token : tokens) {
                // check every possible choice for a given production
                if (isTer(token)) {
                    // the token deduces terminal add to firstSet
                    firstSet.add(token);
                    // then 
                    break;
                } else {
                    // nonTerminal and cannot deduce empty
                    Production p_ = productions.get(token);
                    checkNonTerminal(p_,firstSet);
                }
            }
        }

    }

    boolean isTer(Token t) {
        return t.type != "NonTerminal";
    }

}