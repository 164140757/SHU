/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-04-19 16:16:38
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-22 15:13:17
 * @FilePath: \PLO\src\main\java\Utils\Token\Del.java
 * @Description: 
 */
package Utils.Token;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;

public class Del extends Terminal{
    private static HashSet<Character> del;
    public Del(String context) {
        super("Del","",context);
    }
    public static void init(){
        del = new HashSet<>();
        del.addAll(Arrays.asList('（','）','，',',','；','.','(',')',';','|'));
    }
    public static boolean isDel(Character c){
        return del.contains(c);
    }
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Del del = (Del) o;
        return this.context.equals(del.context) ;
    }
    @Override public int hashCode() {
        return Objects.hash(this.context);
    }
}
