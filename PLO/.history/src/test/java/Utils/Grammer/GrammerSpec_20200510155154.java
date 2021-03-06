/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-10 15:47:49
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-10 15:51:50
 * @FilePath: \PLO\src\test\java\Utils\Grammer\GrammerSpec.java
 * @Description: 
 */
package Utils.Grammer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Utils.Token.NonTerminal;

/**
 * GrammerSpec
 */
public class GrammerSpec {
    Grammar grammar;
    @BeforeEach
    void init(){
        final String l = "E AIB;I FC;F D|N|E;P +|-;M *|/;A P|#;B PIB|#;C MFC|#";
        List<Production> productions = Production.translate(l);
        grammar = new Grammar(new NonTerminal("E"), productions);
    }
    @Test
     void connectProductionsSpec() {

    }
}
