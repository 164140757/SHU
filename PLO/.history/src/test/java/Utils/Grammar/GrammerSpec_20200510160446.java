/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-10 15:47:49
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-10 16:04:41
 * @FilePath: \PLO\src\test\java\Utils\Grammar\GrammerSpec.java
 * @Description: 
 */
package Utils.Grammar;

import java.util.List;

import org.antlr.v4.tool.Grammar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Utils.Grammer.Production;
import Utils.Token.NonTerminal;

/**
 * GrammerSpec
 */
public class GrammerSpec {
    Grammar grammar;
    @BeforeEach
    void init() throws Exception {
        final String l = "E AIB;I FC;F D|N|(E);P +|-;M *|/;A P|#;B PIB|#;C MFC|#;E t";
        List<Production> productions = Production.translate(l);
        grammar = new Grammar(new NonTerminal("E"), productions);
    }
    @Test
     void connectProductionsSpec() {
         
    }
}
