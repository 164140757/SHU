/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 11:58:44
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-09 18:05:59
 * @FilePath: \PLO\src\main\java\Utils\Grammer\Production.java
 * @Description: Production in Grammer 
 */
package Utils.Grammer;

import java.util.Iterator;
import java.util.Vector;

import Utils.Token.Token;

public class Production {
    Token index;
    Vector<Token> target;
    // index --> aAb...
    public Production(final Token index, final Vector<Token> target) {
        this.index = index;
        this.target = target;
    }

    Vector<Token> getTerminals(){
        for(final Iterator i:target.iterator();i.hasNext();){

        }
    }
}
