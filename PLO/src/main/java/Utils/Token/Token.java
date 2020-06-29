/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-04-12 14:12:33
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-22 15:52:39
 * @FilePath: \PLO\src\main\java\Utils\Token\Token.java
 * @Description: 
 */
/*  @Author: Haotian Bai @Github: https://github.com/164140757 @Date: 2020-04-12
 *  14:12:33 @LastEditors: Haotian Bai @LastEditTime: 2020-05-10 08:46:48 @FileP
 * ath: \PLO\src\main\java\Utils\Token\Token.java @Description:

 */

package Utils.Token;

import java.util.Objects;

public class Token {
    public String subType;
    public String type;
    public String context;

    public Token(String type,String subType,String context) {
        this.type = type;
        this.subType = subType;
        this.context = context;
    }

    public Token (String context) {
        this.context = context;
    }
    public NonTerminal toNonTerminal(){
        return new NonTerminal(subType,context);
    }

    // same if letter equals with o
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Token token = (Token) o;
        return this.context.equals(token.context);
    }
    @Override public int hashCode() {
        return Objects.hash(this.context);
    }
}
