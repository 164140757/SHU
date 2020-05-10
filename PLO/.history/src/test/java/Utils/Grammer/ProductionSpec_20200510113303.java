/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 19:02:34
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-10 11:33:00
 * @FilePath: \PLO\src\test\java\Utils\Grammer\ProductionSpec.java
 * @Description: 
 */
package Utils.Grammer;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductionSpec {
    List<Production> productions;
    
    @Test
    void translate() throws Exception {
        String l = "E AIB;I FC;F D|N|E;P +|-;M *|/;A P|#;B PIB|#;C MFC|#";
        productions = Production.translate(l);
        List<Production> res = new ArrayList()
    }
    
}