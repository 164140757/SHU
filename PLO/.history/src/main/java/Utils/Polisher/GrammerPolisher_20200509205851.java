/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 17:58:46
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-09 20:58:45
 * @FilePath: \PLO\src\main\java\Utils\Polisher\GrammerPolisher.java
 * @Description: 
 */
package Utils.Polisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.Token.NonTerminal;

/**
 * GrammerPolisher
 */
public class GrammerPolisher {
    public static void eliLeftRecur(Grammar g){
        HashMap<NonTerminal,Production> productions = g.getProductions();
        List<Production> result = new ArrayList<>();
        productions.forEach((k,v));
    }
}