/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 16:49:58
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-09 16:50:14
 * @FilePath: \PLO\src\main\java\Utils\Token\NonTerminal.java
 * @Description: 
 */
package Utils.Token;

import Utils.Grammer.Production;

class NonTerminal extends Token {
    public NonTerminal(Production production){
        super("NonTerminal");
        this.production = production;
    }
    Production production;
}
