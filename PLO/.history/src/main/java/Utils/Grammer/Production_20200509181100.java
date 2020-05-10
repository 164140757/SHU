/*  @Author: Haotian Bai @Github: https://github.com/164140757 @Date: 2020-05-09
 *  11:58:44 @LastEditors: Haotian Bai @LastEditTime: 2020-05-09 18:06:30 @FileP
 * ath: \PLO\src\main\java\Utils\Grammer\Production.java @Description: Productio
 * n in Grammer

 */
package Utils.Grammer;

import java.util.Iterator;
import java.util.Vector;

import Utils.Token.Token;

public class Production {
    Token index;
    Vector<Token> target;
    // index --> aAb...
    public Production(Token index, Vector<Token> target) {
        this.index = index;
        this.target = target;
    }

    Vector<Token> getTerminals() {
        Vector<Token> result = new 
        for (Iterator<Token> i = result.iterator(); i.hasNext();) {
            Token t = (Token) i.next();
            if(t.type.equals("Terminal")){
                i.remove();
            }
        }
        return result;
    }
}
