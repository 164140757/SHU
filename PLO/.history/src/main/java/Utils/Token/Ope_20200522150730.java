/*
 * @Author: your name
 * @Date: 2020-04-19 15:47:59
 * @LastEditTime: 2020-05-22 15:07:26
 * @LastEditors: Haotian Bai
 * @Description: In User Settings Edit
 * @FilePath: \PLO\src\main\java\Utils\Token\Ope.java
 */
package Utils.Token;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class Ope extends Terminal {
    // operators (single character)
    private static HashSet<Character> opes;

    public Ope(String name) {
        super("OPE",name);
    }

    public Ope(String name,String context) {
        super("OPE",name,context);
    }

    public static void init() {
        opes = new HashSet<>();
        opes.addAll(Arrays.asList('+', '-', '*', '/', '=', '#', '<','>', ':'));
    }

    public static boolean isOpe(Character c) {
        return opes.contains(c);
    }
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Ope ope = (Ope) o;
        return this
            .context
            .equals(token.context) &&;
    }
    @Override public int hashCode() {
        return Objects.hash(this.context);
    }
}
