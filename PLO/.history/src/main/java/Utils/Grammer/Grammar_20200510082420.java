/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 11:54:52
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-10 08:24:20
 * @FilePath: \PLO\src\main\java\Utils\Grammer\Grammar.java
 * @Description: 
 */
/*  @Author: Haotian Bai @Github: https://github.com/164140757 @Date: 2020-05-09
 *  11:54:52 @LastEditors: Haotian Bai @LastEditTime: 2020-05-09 21:09:40 @FileP
 * ath: \PLO\src\main\java\Utils\Grammer\Grammar.java @Description: Grammer clas
 * s for PLO

 */
package Utils.Grammer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import Utils.Token.NonTerminal;
import Utils.Token.Terminal;

public class Grammar {
    List<Production> productions;
    Terminal startToken;
    public Grammar(Terminal startToken, List<Production> productions) {
        this.productions = productions;
        this.startToken = startToken;
    }
    /**
     * @return the productions after connecting counterparts with the same index
     */
    public List<Production> getProductions() {
        connectProductions();
        return productions;
    }

    private void connectProductions() {
        List<Production> res = new ArrayList<>();
        // set to collect its index
        Set<NonTerminal> indexes = new HashSet<>();
    }

    /**
     *
     * @return all nonTerminals in the grammar
     */
    public List < NonTerminal > getNonTerminals() {
        List<NonTerminal> res = new ArrayList<>();
        productions.forEach(pl -> {
            NonTerminal nt = (NonTerminal) pl.index;
            res.add(nt);
            
            pl.target.forEach(p->{
                p.forEach(t->{
                    if(t.type.equals("NonTerminal")){
                        res.add((NonTerminal) t);
                    }
                });
                
            });
        });
        return res;
    }

}