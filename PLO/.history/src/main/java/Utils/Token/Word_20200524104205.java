/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-04-12 14:12:33
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-24 10:42:05
 * @FilePath: \PLO\src\main\java\Utils\Token\Word.java
 * @Description: 
 */
package Utils.Token;

import java.util.Objects;

public class Word extends Terminal{
    private double val;
    public Word(String type, String lexeme) {
        super(type,"identifiers",lexeme);
    }
    public Word(String type, String lexeme) {
        super(type,"identifiers",lexeme);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return this.context == word.context && type.equals(word.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.context, type);
    }
}
