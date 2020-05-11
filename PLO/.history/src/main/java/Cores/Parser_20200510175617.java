/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 12:16:24
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-10 17:56:15
 * @FilePath: \PLO\src\main\java\Cores\Parser.java
 * @Description: 
 */
package Cores;
/*  @Author: Haotian Bai @Github: https://github.com/164140757 @Date: 2020-05-09
 *  12:16:24 @LastEditors: Haotian Bai @LastEditTime: 2020-05-09 17:09:32 @FileP
 * ath: \PLO\src\main\java\Cores\Parser.java @Description: Parser for syntax ana
 * lysis in PLO using LL

 */

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

    public Parser(Grammar grammar) {
        stack = new Stack<>();
        stack.add(new Token("Terminal", "#"));
        parsingTable = new HashMap<>();
        this.grammar = grammar;
    }

    Production parse(Token t) {
        return parsingTable.get(t);
    }

    void getFirst(Token t) {
        Set<Token> first = new HashSet<>();
        HashMap<NonTerminal, Production> productions = grammar.getProductionsMap();
        // terminal itself
        if (isTer(t)) {
            first.add(t);
        }
        // non terminal
        Production production = productions.get(t.context);
        // check target
        for (Vector<Token> tokens : production.target) {
            for (Token token : tokens) {
                
                
            }
            // check every possible choice for a given production
            if(isTer(tokens.get(0))|| tokens.get(0).equals(new Token("#"))){
                // the token deduce empty or terminal add to first 
                first.add(tokens.get(0));
            }
            // nonTerminal and cannot deduce empty
            


            // else{
            //     // nonTerminal and can deduce empty
            //     // go next
            //     boolean next = true;
            //     int index = 0;
            //     while(next && index < tokens.size()){
            //         next = false;
            //         // should make sure their is no left recursion
            //         Production pY =  productions.get(tokens.get(index));
            //         // place all nonempty (Tokens context != "#") to first
                    
            //     }
            // }
        }
    }
    
    boolean isTer(Token t){
        return t.type!="NonTerminal";
    }
    

    
    

}