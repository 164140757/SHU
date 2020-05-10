/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 17:58:46
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-10 08:10:23
 * @FilePath: \PLO\src\main\java\Utils\Polisher\GrammerPolisher.java
 * @Description: 
 */
/*  @Author: Haotian Bai @Github: https://github.com/164140757 @Date: 2020-05-09
 *  17:58:46 @LastEditors: Haotian Bai @LastEditTime: 2020-05-10 08:09:15 @FileP
 * ath: \PLO\src\main\java\Utils\Polisher\GrammerPolisher.java @Description:

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
    public static void eliLeftRecur(Grammar g) {
        List<Production> productions = g.getProductions();
        Set<Production> result = new HashSet<>();
        // get all nonTerminals
        List<NonTerminal> nonTerminals = g.getNonTerminals();
        //
    }

    void expand(List<NonTerminal> nonTerminals, List<Production> productions) {
        for (int i = 0; i < nonTerminals.size(); i++) {
            for (int j = 0; j < i; j++) {
                // search for Ai -> Aj R to replace with Aj -> ...
                for (Iterator<Production> pi = productions.iterator(); pi.hasNext();) {

                    Production p = pi.next();
                    NonTerminal nt = (NonTerminal)p.index;
                    List<Vector<Token>> pl = p.target;
                    if (nt.equals(nonTerminals.get(i))) {
                        pl.forEach(p -> {
                            Vector<Token> tokens = p.t ;
                            Vector<Token> resTokens = new Vector<>();
                            // once find, expand
                            for(Token token : tokens) {
                                if (token.equals(nonTerminals.get(j))) {}
                            }
                        });

                    }
                }

            }
        }
    }
}