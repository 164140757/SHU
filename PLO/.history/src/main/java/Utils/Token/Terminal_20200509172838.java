/*
 * @Author: your name
 * @Date: 2020-05-07 16:18:40
 * @LastEditTime: 2020-05-09 17:28:38
 * @LastEditors: Haotian Bai
 * @Description: In User Settings Edit
 * @FilePath: \PLO\src\main\java\Utils\Token\Terminal.java
 */
package Utils.Token;

public class Terminal extends Token {
    final String letter;
    public Terminal(final String letter) {
        super("Terminal");
        this.letter = letter;
    }
    
}