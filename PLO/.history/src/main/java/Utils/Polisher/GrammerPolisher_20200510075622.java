/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 17:58:46
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-10 07:56:22
 * @FilePath: \PLO\src\main\java\Utils\Polisher\GrammerPolisher.java
 * @Description: 
 */
package Utils.Polisher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.Token.NonTerminal;
import Utils.Token.Token;

/**
 * GrammerPolisher
 */
public class GrammerPolisher {
    //TODO
    public static void eliLeftRecur(Grammar g){
        List<Production> productions = g.getProductions();
        Set<Production> result = new HashSet<>();
        // get all nonTerminals
        List<NonTerminal> nonTerminals = g.getNonTerminals();
 
    }
}