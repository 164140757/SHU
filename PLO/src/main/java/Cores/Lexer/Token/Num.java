package Cores.Lexer.Token;

public class Num extends Token{
    public final int value;
    public Num(int v) {
        super("NUM");value = v;
    }
}
