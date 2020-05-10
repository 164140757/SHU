/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-10 09:02:43
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-10 15:21:32
 * @FilePath: \PLO\src\test\java\Cores\ParserTest.java
 * @Description: parserTest 
 */
package Cores;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Utils.Token.*;
import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.IO.Writer;
public class ParserTest {
    @BeforeEach
    void init(){
        //expression
        final String l = "E AIB;I FC;F D|N|E;P +|-;M *|/;A P|#;B PIB|#;C MFC|#";
        productions = Production.translate(l);
        Grammar g = new Grammar(null, null);
    }
    void findFirstTest(){

    }
}
