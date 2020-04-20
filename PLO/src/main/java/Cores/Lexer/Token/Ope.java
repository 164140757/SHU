package Cores.Lexer.Token;

import java.util.Arrays;
import java.util.HashSet;

public class Ope extends Token {
    // operators (single character)
    private static HashSet<Character> opes;
    public String name;

    public Ope() {
        super("OPE");
    }

    public static void init() {
        opes = new HashSet<>();
        opes.addAll(Arrays.asList('+', '-', '*', '/', '=', '#', '<','>', ':'));
    }

    public static boolean isOpe(Character c) {
        return opes.contains(c);
    }
}
