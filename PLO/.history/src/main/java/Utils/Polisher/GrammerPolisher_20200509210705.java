/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 17:58:46
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-09 21:07:03
 * @FilePath: \PLO\src\main\java\Utils\Polisher\GrammerPolisher.java
 * @Description: 
 */
package Utils.Polisher;

import java.util.ArrayList;
import java.util.List;

import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.Token.NonTerminal;

/**
 * GrammerPolisher
 */
public class GrammerPolisher {
    public static void eliLeftRecur(Grammar g){
        List<Production> productions = g.getProductions();
        List<Production> result = new ArrayList<>();
        // get all nonTerminals
        List<NonTerminal> nonTerminals = g.getNonTer
        for (int i = 0; i < productions.size(); i++) {
            for (int j = 0; j < i; j++) {
                
            }
        }
    }
}