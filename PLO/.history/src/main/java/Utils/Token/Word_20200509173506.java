/*
 * @Author: Haotian Bai
 * @Github: https://github.com/164140757
 * @Date: 2020-04-12 14:12:33
 * @LastEditors: Haotian Bai
 * @LastEditTime: 2020-05-09 17:35:04
 * @FilePath: \PLO\src\main\java\Utils\Token\Word.java
 * @Description: 
 */
package Utils.Token;

import java.util.Objects;

public class Word extends Terminal{
    public Word(String type, String lexeme) {
        super(type,lexeme);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return lexeme.equals(word.lexeme) && type.equals(word.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexeme, type);
    }
}
