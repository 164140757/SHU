/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-04-12 14:12:33
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-22 15:10:55
 * @FilePath: \PLO\src\main\java\Utils\Token\Num.java
 * @Description: 
 */
package Utils.Token;

public class Num extends Terminal{
    public final int value;
    public Num(int v) {
        super("NUM","Integer",Integer.toString(v));
        value = v;
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
            .equals(ope.context) && this.name.equals(ope.name);
    }
    @Override public int hashCode() {
        return Objects.hash(this.context,this.name);
    }
}
