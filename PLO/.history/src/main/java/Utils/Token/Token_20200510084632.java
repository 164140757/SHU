/*
 * @Author: your name
 * @Date: 2020-04-12 14:12:33
 * @LastEditTime: 2020-05-10 08:46:27
 * @LastEditors: Haotian Bai
 * @Description: In User Settings Edit
 * @FilePath: \PLO\src\main\java\Utils\Token\Token.java
 */
package Utils.Token;

public class Token {
    public String context;
    public final String type;
    public Token(String type,String letter) {
        this.type = type;
        this.context = letter;
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
        return this.context.equals(nonTerminal.context);
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.context);
    }
}
