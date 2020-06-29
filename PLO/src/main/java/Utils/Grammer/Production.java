/*  @Author: Haotian Bai @Github: https://github.com/164140757 @Date: 2020-05-09
 *  11:58:44 @LastEditors: Haotian Bai @LastEditTime: 2020-05-10 12:00:30 @FileP
 * ath: \PLO\src\main\java\Utils\Grammer\Production.java @Description:

 */

package Utils.Grammer;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import Cores.Lexer;
import Utils.Token.NonTerminal;
import Utils.Token.Terminal;
import Utils.Token.Token;
import Utils.Token.Word;

public class Production {
    public NonTerminal index;
    public List<Vector<Token>> target;
    // preDefine is a map : type,syntax
    public static HashMap<String, String> preDefined;

    // index --> aAb...
    public Production(NonTerminal index, List<Vector<Token>> target) {
        this.index = index;
        this.target = target;
    }


    public Production(NonTerminal index, Vector<Token> target) {
        this.index = index;
        this.target = Arrays.asList(target);
    }

    public Production(NonTerminal index) {
        this.index = index;
        this.target = new ArrayList<>();
    }

    public Production(String index) {
        this.index = new NonTerminal(index);
        this.target = new ArrayList<>();
    }

    /**
     * A -> BMsf no '|'
     */
    public Production(String index, String target) {
        this.index = new NonTerminal(index);
        List<Vector<Token>> res = new ArrayList<>();
        char[] charArray = target.toCharArray();
        Vector<Token> tmp = new Vector<>();
        for (int i = 0; i < charArray.length; i++) {
            char c = charArray[i];
            tmp.add(new Token(String.valueOf(c)));
        }
        res.add(tmp);
        this.target = res;
    }

    /**
     * @param productions String representation of the productions, like "A BaMC;M Bd; B #"
     * @return
     * @description define grammar, # means empty set
     */
    public static List<Production> translate(String productions) throws Exception {
        List<Production> res = new ArrayList<>();
        // create index set
        Set<Token> indexes = new HashSet<>();
        Pattern p = Pattern.compile("[.*;]+");
        Matcher matcher = p.matcher(productions);
        // check input format is right
        if (!matcher.find()) {
            throw new Exception("Your grammar input format is not valid.");
        }
        String[] pStrings = productions.split(";");
        for (String pString : pStrings) {
            String[] splits = pString.split("\\s+");
            String index = splits[0];
            String target = splits[1].split(";")[0];
            char[] charArray = target.toCharArray();
            Production production = new Production(new NonTerminal(index));
            indexes.add(new NonTerminal(index));
            Vector<Token> toAdd = new Vector<>();
            for (char c : charArray) {
                switch (c) {
                    // handle operator empty set
                    case '#' : {
                        Vector<Token> tmp = new Vector<>();
                        tmp.add(new Token("#"));
                        Production pt = new Production(new NonTerminal(index), tmp);
                        production.or(pt);
                    }
                    // or
                    case '|' : {
                        Production pt = new Production(new NonTerminal(index), toAdd);
                        production.or(pt);
                        toAdd = new Vector<>();
                    }

                    default : {
                        // upper one
                        toAdd.add(new Token(String.valueOf(c)));
                    }
                }
            }

            // if not empty add
            if (toAdd.size() != 0) {
                Production pt = new Production(new NonTerminal(index), toAdd);
                production.or(pt);
            }
            res.add(production);
        }
        // for nonTerminals targets
        res.forEach(production -> {
            RecordNonTerm(indexes, production);
        });
        return res;
    }

    private static void RecordNonTerm(Set<Token> indexes, Production production) {
        List<Vector<Token>> target = production.target;
        for (Vector<Token> ts : target) {
            for (Token t : ts) {
                String name = t.context;
                if (indexes.contains(new NonTerminal(name))) {
                    t.type = "NonTerminal";
                }
            }
        }
    }

    public static List<Production> translate(File grammarFile) {
        Set<Token> indexes = new HashSet<>();
        Lexer lexer = new Lexer(grammarFile);
        List<Production> res = new ArrayList<>();
        Vector<Token> lineBuf = new Vector<>();
        try {
            Token t = lexer.scan();
            while (t != null) {
                if (!t.subType.equals("semicolon")) {
                    lineBuf.add(t);
                } else {
                    res.add(handleProduction(indexes,lineBuf));
                    lineBuf = new Vector<>();
                }
//                System.out.println(t.type+" "+t.sign);
                t = lexer.scan();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // for nonTerminals targets
        res.forEach(production -> {
            RecordNonTerm(indexes, production);
        });
        return res;

    }

    private static Production handleProduction(Set<Token> indexes,Vector<Token> lineBuf) {
        Production production = null;
        Vector<Token> toAdd = new Vector<>();
        for (int i = 0; i < lineBuf.size(); i++) {
            Token t = lineBuf.get(i);
            String s = t.context;
            if (i == 0) {
                production = new Production(new NonTerminal(t.context));
                indexes.add(new NonTerminal(t.context));
            } else {
                switch (s) {
                    case "#" : {
                        Vector<Token> tmp = new Vector<>();
                        tmp.add(new Token("#"));
                        Production pt = new Production(new NonTerminal(production.index), tmp);
                        production.or(pt);
                        break;
                    }
                    case "|" : {
                        Production pt = new Production(new NonTerminal(production.index), toAdd);
                        production.or(pt);
                        toAdd = new Vector<>();
                        break;
                    }
                    default : {
                        // upper one
                        toAdd.add(t);
                    }
                }
            }
        }
        if (toAdd.size() != 0) {
            Production pt = new Production(new NonTerminal(production.index), toAdd);
            production.or(pt);
        }
        return production;
    }

    public static void addDefinition(String type,String syntax ) {
        if (preDefined == null) {
            preDefined = new HashMap<>();
        }
        preDefined.put(type, syntax);
    }

    public boolean or(Production p) {
        // same index
        assert this
                .index
                .equals(p.index);
        return target.addAll(p.target);
    }

    public Production or(char c) {
        Token t = new Token(String.valueOf(c));
        Vector<Token> nv = new Vector<>();
        nv.add(t);
        target.add(nv);
        return this;
    }

    public Production or(String s) {
        Vector<Token> nv = new Vector<>();
        for (char c : s.toCharArray()) {
            Token t = new Token(String.valueOf(c));
            nv.add(t);
        }
        target.add(nv);
        return this;
    }

    public static double eval(final String str, Map<Word, Double> variables, Vector<Vector<String>> quaternary) {

        return new Object() {
            int pos = -1, ch;
            int row = 0;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+')) {
                        quaternary.get(row).add(0, "+");
                        x += parseTerm();
                        quaternary.get(row).add("t" + row++);
                        quaternary.add(new Vector<>());

                    } // addition
                    else if (eat('-')) {
                        quaternary.get(row).add(0, "-");
                        x -= parseTerm(); // subtraction
                        quaternary.get(row).add("t" + row++);
                        quaternary.add(new Vector<>());

                    } else return x;
                }
            }

            double parseTerm() {

                double x = parseFactor();
                for (; ; ) {
                    if (eat('*')) {
                        quaternary.get(row).add(0, "*");
                        x *= parseFactor(); // multiplication
                        quaternary.get(row).add("t" + row++);
                        quaternary.add(new Vector<>());


                    } else if (eat('/')) {
                        quaternary.get(row).add(0, "/");
                        x /= parseFactor(); // division
                        quaternary.get(row).add("t" + row++);
                        quaternary.add(new Vector<>());

                    } else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus
                if (row != 0) {
                    quaternary.get(row).add("t" + (row - 1));
                }
                double x = 0;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                    quaternary.get(row).add(String.valueOf(x));
                } else if (Character.isLetterOrDigit(ch)) { // words predefined
                    int tmp = this.pos;
                    while (Character.isLetterOrDigit(ch)) nextChar();
                    String words = str.substring(startPos, this.pos);
                    quaternary.get(row).add(words);
                    //find
                    if (variables != null && variables.containsKey(new Word(words))) {
                        x = variables.get(new Word(words));
                    }
                    // back
                    else {
                        pos = tmp;
                        ch = str.charAt(pos);
                        if (ch >= 'a' && ch <= 'z') { // functions
                            while (ch >= 'a' && ch <= 'z') nextChar();
                            String func = str.substring(startPos, this.pos);
                            x = parseFactor();
                            if (func.equals("sqrt")) x = Math.sqrt(x);
                            else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                            else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                            else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                            else throw new RuntimeException("Unknown function: " + func);
                        }
                    }

                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Production p = (Production) obj;
        return this
                .index
                .equals(p.index) && this
                .target
                .containsAll(p.target) && p.target.containsAll(this.target);
    }
}
