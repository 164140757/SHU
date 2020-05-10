package Utils.Token;

public class Num extends Terminal{
    public final int value;
    public Num(int v) {
        super("NUM");value = v;
    }
}
