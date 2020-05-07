package Cores.Lexer.Token;

import java.util.Arrays;
import java.util.HashSet;

public class Del extends Terminal{
    public String name;
    private static HashSet<Character> del;
    public Del() {
        super("Del");
    }
    public static void init(){
        del = new HashSet<>();
        del.addAll(Arrays.asList('（','）','，',',','；','.','(',')',';'));
    }
    public static boolean isDel(Character c){
        return del.contains(c);
    }
}
