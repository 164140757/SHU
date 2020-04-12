package Cores.Lexer.Token;

public enum Tag {
    ID("[a-zA-Z][[0-9]|[a-zA-Z]]*"),
    NUM("[0-9]+"),
    OPE("\\+|-|\\*|/|=|#|<|<=|>|>=|:="),
    DELIMITER("\\(|\\)|（|）|,|;|\\."),
    BASIC("begin|call|const|do|end|if|else|odd|procedure|read|then|var|while|write");

    public final String pattern;

    Tag(String pattern) {
        this.pattern = pattern;
    }
}
