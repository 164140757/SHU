package Cores.Lexer.Token;

import java.util.Objects;

public class Word extends Token{
    public final String lexeme;
    public Word(Tag tag, String lexeme) {
        super(tag);
        this.lexeme = lexeme;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return lexeme.equals(word.lexeme) && tag.equals(word.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lexeme,tag);
    }
}
