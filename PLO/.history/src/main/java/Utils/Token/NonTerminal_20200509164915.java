/*
 * @Author: Haotian Bai
 * @Date: 2020-05-07 16:20:20
 * @LastEditTime: 2020-05-09 16:49:13
 * @LastEditors: Haotian Bai
 * @Description: NonTerminal in production
 * @FilePath: \PLO\src\main\java\Utils\Token\NonTerminal.java
 */
package Utils.Token;

import Utils.Grammer.Production;

/**
 * NonTerminal
 */
public class NonTerminal extends Token{
 
    public NonTerminal(Production production){
        super("NonTerminal");
        this.production = production;
    }
    Production production;
}