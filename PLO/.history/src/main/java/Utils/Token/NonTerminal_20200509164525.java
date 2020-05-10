/*
 * @Author: Haotian Bai
 * @Date: 2020-05-07 16:20:20
 * @LastEditTime: 2020-05-09 12:15:29
 * @LastEditors: Haotian Bai
 * @Description: Nontermial in production
 * @FilePath: \PLO\src\main\java\Utils\Token\Nonterminal.java
 */
package Utils.Token;

import Utils.Grammer.Production;

/**
 * Nonterminal
 */
public class Nonterminal extends Token{
    Production production;
    public Nonterminal(Production production){
        super("Nonterminal");
        this.production = production;
    }
    
    
}