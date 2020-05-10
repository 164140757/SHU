/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 19:02:34
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-10 15:01:07
 * @FilePath: \PLO\src\test\java\Utils\Grammer\ProductionSpec.java
 * @Description: 
 */
package Utils.Grammer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.core.util.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductionSpec {
    List<Production> productions;
    
    @Test
    void translate() throws Exception {
        String l = "E AIB;I FC;F D|N|E;P +|-;M *|/;A P|#;B PIB|#;C MFC|#";
        productions = Production.translate(l);
        List<Production> res = new ArrayList<>();
        res.add(new Production("E", "AIB"));
        res.add(new Production("I", "FC"));
        res.add(new Production("F", "D").or('N').or('E'));
        res.add(new Production("P", "+").or('-'));
        res.add(new Production("M", "*").or('/'));
        res.add(new Production("A", "P").or('#'));
        res.add(new Production("B", "PIB").or('#'));
        res.add(new Production("C", "MFC").or('#'));
        assertEquals(res, productions, "Translation fails");
    }

    @Test
    void other(){
        assertEquals(new Production("E","AIB"), new Production("E","AIB"), "=");
    }
    
}