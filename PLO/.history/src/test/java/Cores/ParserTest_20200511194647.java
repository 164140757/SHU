/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-10 09:02:43
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-11 19:46:47
 * @FilePath: \PLO\src\test\java\Cores\ParserTest.java
 * @Description: parserTest 
 */
package Cores;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Utils.Token.*;
import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.IO.Writer;

public class ParserTest {
    
    private Grammar grammar;
    private Parser parser ;
    @BeforeEach
    void init() throws Exception {
        // expression
        final String l = "E AIB;I FC;F D|N|(E);P +|-;M *|/;A P|#;B PIB|#;C MFC|#";
        List<Production> productions = Production.translate(l);
        grammar = new Grammar(new NonTerminal("E"), productions);
        new String("i+i*2");
        parse 
    }
    void findFirstTest(){
        
    }
}
