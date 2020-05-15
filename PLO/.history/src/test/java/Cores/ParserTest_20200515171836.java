/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-10 09:02:43
 * @LastEditors: Please set LastEditors
 * @LastEditTime: 2020-05-15 17:18:36
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
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Exceptions.GrammarError;
import Utils.Token.*;
import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.IO.Writer;

public class ParserTest {

    private Parser parser;

    @BeforeEach
    void init() throws Exception {
        // expression
        // final String l = "E AIB;I FC;F D|N|(E);P +|-;M *|/;A P|#;B PIB|#;C MFC|#";
        // List<Production> productions = Production.translate(l);
        // Grammar grammar = new Grammar(new NonTerminal("E"), productions);
        // new String("i+i*2");
        // parser = new Parser(grammar);
    }

    @Test
    void findFirstTest() throws Exception {
        // Page 72 4.5
        String test = "S AB;S bC;A #;B #;A b;B aD;C AD;C b;D aS;D c";
        Grammar grammar = new Grammar(new NonTerminal("E"), Production.translate(test));
        parser = new Parser(grammar);
        parser.getFirst();
        HashMap<NonTerminal, Set<Token>> first = parser.first;
        assertEquals(new HashSet<>(Arrays.asList(new Token("a"), new Token("#"), new Token("b"))),
                first.get(new NonTerminal("S")), "first set fails.");

        assertEquals(new HashSet<>(Arrays.asList(new Token("a"), new Token("c"), new Token("b"))),
                first.get(new NonTerminal("C")), "first set fails.");
    }

    @Test
    void findFirstTest2() throws Exception {
        // Page 93
        String test = "E TA;A +TA;A #;T FB;B *FB;B #;F i;F (E)";
        Grammar grammar = new Grammar(new NonTerminal("E"), Production.translate(test));
        parser = new Parser(grammar);
        parser.getFirst();
        HashMap<NonTerminal, Set<Token>> first = parser.first;
        assertEquals(new HashSet<>(Arrays.asList(new Token("("), new Token("i"))), first.get(new NonTerminal("E")),
                "first set fails.");

        assertEquals(new HashSet<>(Arrays.asList(new Token("+"), new Token("#"))), first.get(new NonTerminal("A")),
                "first set fails.");

        assertEquals(new HashSet<>(Arrays.asList(new Token("("), new Token("i"))), first.get(new NonTerminal("T")),
                "first set fails.");
    }

    @Test
    void followTest() throws Exception {
        // Page 72 4.5
        String test = "S AB;S bC;A #;B #;A b;B aD;C AD;C b;D aS;D c";
        Grammar grammar = new Grammar(new NonTerminal("S"), Production.translate(test));
        parser = new Parser(grammar);
        parser.getFirst();
        parser.getFollow();
        HashMap<NonTerminal, Set<Token>> follow = parser.follow;
        assertEquals(new HashSet<>(Arrays.asList(new Token("!"))), follow.get(new NonTerminal("S")),
                "first set fails.");

        assertEquals(new HashSet<>(Arrays.asList(new Token("a"), new Token("!"), new Token("c"))),
                follow.get(new NonTerminal("A")), "first set fails.");

        assertEquals(new HashSet<>(Arrays.asList(new Token("!"))), follow.get(new NonTerminal("D")),
                "first set fails.");
    }

    @Test
    void followTest_2() throws Exception {
        // Page 93
        String test = "E TA;A +TA;A #;T FB;B *FB;B #;F i;F (E)";
        Grammar grammar = new Grammar(new NonTerminal("E"), Production.translate(test));
        parser = new Parser(grammar);
        parser.getFirst();
        parser.getFollow();
        HashMap<NonTerminal, Set<Token>> follow = parser.follow;
        assertEquals(new HashSet<>(Arrays.asList(new Token("!"), new Token(")"))), follow.get(new NonTerminal("E")),
                "first set fails.");

        assertEquals(new HashSet<>(Arrays.asList(new Token(")"), new Token("!"))), follow.get(new NonTerminal("A")),
                "first set fails.");

        assertEquals(new HashSet<>(Arrays.asList(new Token("+"), new Token("!"), new Token(")"))),
                follow.get(new NonTerminal("B")), "first set fails.");
    }

    @Test
    void LL() throws Exception, GrammarError {
        // Page 72 4.5
        String test = "E AIB;I FC;F D|N|(E);P +|-;M *|/;A P|#;B PIB|#;C MFC|#";
        Grammar grammar = new Grammar(new NonTerminal("E"), Production.translate(test));
        Lexer lexer == new Lexer
        parser = new Parser(grammar);
        parser.getFirst();
        parser.getFollow();
        parser.getSelect();
        parser.LL();
    }

    
    
    
}
