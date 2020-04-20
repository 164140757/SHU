package Cores.Lexer.Token;

import java.util.Objects;

public class Word extends Token{
    public final String lexeme;
    public Word(String type, String lexeme) {
        super(type);
        this.lexeme = lexeme;
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
