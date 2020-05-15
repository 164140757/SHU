/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-10 09:02:43
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-11 19:59:02
 * @FilePath: \PLO\src\test\java\Cores\ParserTest.java
 * @Description: parserTest 
 */
package Cores;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Utils.Token.*;
import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.IO.Writer;

public class ParserTest {
    
    private Parser parser ;
    @BeforeEach
    void init() throws Exception {
        // expression
        // final String l = "E AIB;I FC;F D|N|(E);P +|-;M *|/;A P|#;B PIB|#;C MFC|#";
        // List<Production> productions = Production.translate(l);
        // Grammar grammar = new Grammar(new NonTerminal("E"), productions);
        // new String("i+i*2");
        // parser = new Parser(grammar);
    }
    void findFirstTest() throws Exception {
        // Page 72 4.5
        String test = "S AB;S bC;A #;B #;A b;B aD;C AD;C b;D aS;D c";
        Grammar grammar = new Grammar(new NonTerminal("E"), Production.translate(test));
        parser = new Parser(grammar);
        parser.getFirst(new NonTerminal("S"));
        HashMap<Token, Set<Token>> first = parser.first;
        assertEquals(new HashSet<>(Arrays.asList(new Token("a"),new Token("#"),new Token("b"))), 
        first.get(), messageSupplier);
    }
}
