/*
 * @Author: your name
 * @Date: 2020-04-19 15:47:59
 * @LastEditTime: 2020-05-09 17:53:48
 * @LastEditors: Haotian Bai
 * @Description: In User Settings Edit
 * @FilePath: \PLO\src\main\java\Utils\Token\Ope.java
 */
package Utils.Token;

import java.util.Arrays;
import java.util.HashSet;

public class Ope extends Terminal {
    // operators (single character)
    private static HashSet<Character> opes;

    public Ope(char letter) {
        super("OPE",letter);
    }

    public static void init() {
        opes = new HashSet<>();
        opes.addAll(Arrays.asList('+', '-', '*', '/', '=', '#', '<','>', ':'));
    }

    public static boolean isOpe(Character c) {
        return opes.contains(c);
    }
}
