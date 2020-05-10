/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 12:16:24
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-10 15:19:30
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
import java.util.Set;
import java.util.Stack;

import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.Token.Token;

public class Parser {
    // ready to analyse
    Stack<Token> stack;
    // predicting table 
    HashMap<Token, Production> parsingTable;
    // grammer 
    // map for first and follow
    HashMap<Token,Set<Token>> first;
    HashMap<Token,Set<Token>> follow;

    public Parser(Grammar g) {
        stack = new Stack<>();
        stack.add(new Token("Terminal", "#"));
        parsingTable = new HashMap<>();
        
    }

    Production parse(Token t) {
        return parsingTable.get(t);
    }

    void getFirst(Token t){
        Set<Token> first = new HashSet<>();
        // terminal itself
        if(t.type!="NonTerminal"){
                first.add(t);
        }        
        // non terminal 
        
    }
    

    

    
    

}