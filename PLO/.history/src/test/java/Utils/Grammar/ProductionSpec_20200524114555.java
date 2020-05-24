/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 19:02:34
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-24 11:45:46
 * @FilePath: \PLO\src\test\java\Utils\Grammar\ProductionSpec.java
 * @Description: 
 */
package Utils.Grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import Cores.Lexer;
import Cores.Parser;
import Exceptions.GrammarError;
import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.Token.NonTerminal;
import Utils.Token.Num;
import Utils.Token.Word;

public class ProductionSpec {
    List<Production> productions;

    @Test
    void translate() throws Exception {
        final String l = "E AIB;I FC;F D|N|(E);P +|-;M *|/;A P|#;B PIB|#;C MFC|#";
        productions = Production.translate(l);
        final List<Production> res = new ArrayList<>();
        res.add(new Production("E", "AIB"));
        res.add(new Production("I", "FC"));
        res.add(new Production("F", "D").or('N').or("(E)"));
        res.add(new Production("P", "+").or('-'));
        res.add(new Production("M", "*").or('/'));
        res.add(new Production("A", "P").or('#'));
        res.add(new Production("B", "PIB").or('#'));
        res.add(new Production("C", "MFC").or('#'));
        assertEquals(res, productions, "Translation fails");
    }

    @Test
    void other() {
        assertEquals(new Production("E", "AIB"), new Production("E", "AIB"), "=");
    }

    @Test
    void evalTest() throws GrammarError, Exception {
          // Page 72 4.5
          String test = "E AIB;I FC;F D|N|(E);P +|-;M *|/;A P|#;B PIB|#;C MFC|#";
          // add definition
          Production.addDefinition('D', Word.class);
          Production.addDefinition('N', Num.class);
          Grammar grammar = new Grammar(new NonTerminal("E"), Production.translate(test));
          Lexer lexer = new Lexer();
          lexer.inputString("(a+15)*b");
          Parser parser = new Parser(grammar,lexer);
          parser.run();
          //TODO add eval() to parse() the  
    }
    
}
