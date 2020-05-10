package Cores;
/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 12:16:24
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-09 12:33:45
 * @FilePath: \PLO\src\main\java\Cores\Parser.java
 * @Description: Parser for syntax analysis in PLO using LL 
 */

import java.util.HashMap;
import java.util.Stack;

import Utils.Grammer.Production;
import Utils.Token.Nonterminal;

public class Parser {
    Stack<Nonterminal> grammerStack;
    HashMap<Token,Production> parsingTable;
}