package Cores.Lexer.Token;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

class TagTest {
    @Test
    void patternsTest() {
        // NUM
        assertTrue(Pattern.matches(Tag.NUM.pattern, "555666600"));
        assertFalse(Pattern.matches(Tag.NUM.pattern, "555xe2"));
        // ID
        assertTrue(Pattern.matches(Tag.ID.pattern, "u123x"));
        assertFalse(Pattern.matches(Tag.ID.pattern, "12Uddd"));
        // OPE
        assertTrue(Pattern.matches(Tag.OPE.pattern, "+"));
        assertTrue(Pattern.matches(Tag.OPE.pattern, "-"));
        assertTrue(Pattern.matches(Tag.OPE.pattern, "*"));
        assertTrue(Pattern.matches(Tag.OPE.pattern, "/"));
        assertTrue(Pattern.matches(Tag.OPE.pattern, "="));
        assertTrue(Pattern.matches(Tag.OPE.pattern, ":="));
        assertTrue(Pattern.matches(Tag.OPE.pattern, "#"));
        assertTrue(Pattern.matches(Tag.OPE.pattern, "<="));
        assertTrue(Pattern.matches(Tag.OPE.pattern, ">="));
        // BASIC
        assertTrue(Pattern.matches(Tag.BASIC.pattern, "begin"));
        // DELIMITER
        assertTrue(Pattern.matches(Tag.DELIMITER.pattern, "）"));
        assertTrue(Pattern.matches(Tag.DELIMITER.pattern, "("));
        assertTrue(Pattern.matches(Tag.DELIMITER.pattern, ")"));
        assertTrue(Pattern.matches(Tag.DELIMITER.pattern, "（"));
        assertTrue(Pattern.matches(Tag.DELIMITER.pattern, ","));
        assertTrue(Pattern.matches(Tag.DELIMITER.pattern, ";"));
        assertTrue(Pattern.matches(Tag.DELIMITER.pattern, "."));


    }

}