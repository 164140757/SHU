/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-05-09 16:53:02
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-24 10:18:40
 * @FilePath: \PLO\src\main\java\Utils\Token\NonTerminal.java
 * @Description: 
 */
/*  @Author: Haotian Bai @Github: https://github.com/164140757 @Date: 2020-05-09
 *  16:53:02 @LastEditors: Haotian Bai @LastEditTime: 2020-05-10 08:32:51 @FileP
 * ath: \PLO\src\main\java\Utils\Token\NonTerminal.java @Description:

 */
package Utils.Token;

import java.util.Objects;

/**
 * NonTerminal this is a class that explicitly declare its type as "NonTerminal",
 * which could be used to distinguish from the Terminal.
 */
public class NonTerminal extends Token {
    public NonTerminal(String letter) {
        super("NonTerminal", letter);
        String.
    }
    // same if letter equals with o
    @Override public boolean equals(Object o) {
        if (this == o){
            return true;
        } 
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        NonTerminal nonTerminal = (NonTerminal) o;
        return this.sign.equals(nonTerminal.sign);
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.sign);
    }
}
