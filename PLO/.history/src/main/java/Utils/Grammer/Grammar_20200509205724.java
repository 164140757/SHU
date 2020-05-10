/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 11:54:52
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-09 20:57:19
 * @FilePath: \PLO\src\main\java\Utils\Grammer\Grammar.java
 * @Description: Grammer class for PLO
 */
package Utils.Grammer;

import java.util.HashMap;
import java.util.List;

import Utils.Token.NonTerminal;
import Utils.Token.Terminal;

public class Grammar {
    HashMap<NonTerminal,production> productions;
    Terminal startToken;
    public Grammar(Terminal startToken,HashMap<NonTerminal,Production> productions){
        this.productions = productions;
        this.startToken = startToken;
    }
    /**
     * @return the productions
     */
    public List<Production> getProductions() {
        return productions;
    }
}