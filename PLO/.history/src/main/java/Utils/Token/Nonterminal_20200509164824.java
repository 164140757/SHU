/*
 * @Author: Haotian Bai
 * @Date: 2020-05-07 16:20:20
 * @LastEditTime: 2020-05-09 16:48:23
 * @LastEditors: Haotian Bai
 * @Description: NonTerminal in production
 * @FilePath: \PLO\src\main\java\Utils\Token\Nonterminal.java
 */
package Utils.Token;

import Utils.Grammer.Production;

/**
 * NonTerminal
 */
/**
 * NonTerminal
 */
public class NonTerminal {

    Production production;
    public NonTerminal(Production production){
        super("NonTerminal");
        this.production = production;
    }
}