/*
 * @Author: your name
 * @Date: 2020-04-12 14:12:33
 * @LastEditTime: 2020-05-09 17:53:06
 * @LastEditors: Haotian Bai
 * @Description: In User Settings Edit
 * @FilePath: \PLO\src\main\java\Utils\Token\Token.java
 */
package Utils.Token;

public class Token {
    public char context;
    public final String type;
    public Token(String type,char letter) {
        this.type = type;
        this.context = letter;
    }
}
