/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 19:02:34
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-24 12:16:54
 * @FilePath: \PLO\src\test\java\Utils\Grammar\ProductionSpec.java
 * @Description:
 */
package Utils.Grammar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

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
        //   String test = "E AIB;I FC;F D|N|(E);P +|-;M *|/;A P|#;B PIB|#;C MFC|#";
        //   // add definition
        //   Production.addDefinition('D', Word.class);
        //   Production.addDefinition('N', Num.class);
        //   Grammar grammar = new Grammar(new NonTerminal("E"), Production.translate(test));
        //   Lexer lexer = new Lexer();
        //   lexer.inputString("(a+15)*b");
        //   Parser parser = new Parser(grammar,lexer);
        //   parser.run();
        //TODO add eval() to parse() the parse() to run()
        HashMap<Word, Double> wordMap = new HashMap<>();
        wordMap.put(new Word("a"), 0.213);
        wordMap.put(new Word("b"), 0.4);
        Vector<Vector<String>> tmp = new Vector<>();
        tmp.add(new Vector<>());
        double res = Production.eval("(a+15)*b", wordMap, tmp);
        assertEquals(6.0852, res);
        tmp = new Vector<>();
        tmp.add(new Vector<>());
        res = Production.eval("((4 - 2^3 + 1) * -sqrt(3*3+4*4)) / 2", wordMap,tmp);
        assertEquals(7.5, res);
    }

    @Test
    void test(){
        Vector<Vector<String>> tmp = new Vector<>();
        HashMap<Word, Double> wordMap = new HashMap<>();
        wordMap.put(new Word("a"), 0.213);
        wordMap.put(new Word("b"), 0.4);
        wordMap.put(new Word("d"), 0.4);
        wordMap.put(new Word("c"), 0.4);

        tmp.add(new Vector<>());
        double res = Production.eval("(a+15)*b", wordMap, tmp);
        assertEquals(6.0852, res);
        System.out.println(tmp.toString());
        tmp = new Vector<>();
        tmp.add(new Vector<>());
        res = Production.eval("(a+c)*b*d", wordMap, tmp);
        System.out.println(tmp.toString());

        tmp = new Vector<>();
        tmp.add(new Vector<>());
        res = Production.eval("((a+c)*b)+2", wordMap, tmp);
        System.out.println(tmp.toString());


    }

}
