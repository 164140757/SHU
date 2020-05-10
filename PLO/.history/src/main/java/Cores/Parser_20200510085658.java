/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 12:16:24
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-10 08:56:58
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
import java.util.Stack;

import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.Token.Token;

public class Parser {
    // ready to analyse
    Stack<Token> stack;
    // predicting table 
    HashMap<Token, Production> parsingTable;
    // map for first and follow
    


    public Parser(Grammar g) {
        stack = new Stack<>();
        stack.add(new Token("Terminal", "#"));
        parsingTable = new HashMap<>();
        
    }

    Production parse(Token t) {
        return parsingTable.get(t);
    }

    

    

    
    

}