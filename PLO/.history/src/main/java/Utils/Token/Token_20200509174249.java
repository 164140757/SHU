/*
 * @Author: your name
 * @Date: 2020-04-12 14:12:33
 * @LastEditTime: 2020-05-09 17:36:39
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
}
