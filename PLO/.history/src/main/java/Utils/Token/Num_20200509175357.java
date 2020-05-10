/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-04-12 14:12:33
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-09 17:53:57
 * @FilePath: \PLO\src\main\java\Utils\Token\Num.java
 * @Description: 
 */
package Utils.Token;

public class Num extends Terminal{
    public final int value;
    public Num(int v) {
        super("NUM",Char);
        value = v;
    }
}
