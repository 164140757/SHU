/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 17:58:46
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-09 21:40:44
 * @FilePath: \PLO\src\main\java\Utils\Polisher\GrammerPolisher.java
 * @Description: 
 */
package Utils.Polisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.Token.NonTerminal;
import Utils.Token.Token;

/**
 * GrammerPolisher
 */
public class GrammerPolisher {
    public static void eliLeftRecur(Grammar g){
        List<Production> productions = g.getProductions();
        List<Production> result = new ArrayList<>();
        // get all nonTerminals
        List<NonTerminal> nonTerminals = g.getNonTerminals();
        for (int i = 0; i < nonTerminals.size(); i++) {
            for (int j = 0; j < i; j++) {
                // search for Ai -> Aj R to replace with Aj -> ...
                for (Production nonTerminal : nonTerminals) {
                    
                }
            }
        }
    }
}