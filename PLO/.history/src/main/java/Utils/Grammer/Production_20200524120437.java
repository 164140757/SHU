/*  @Author: Haotian Bai @Github: https://github.com/164140757 @Date: 2020-05-09
 *  11:58:44 @LastEditors: Haotian Bai @LastEditTime: 2020-05-10 12:00:30 @FileP
 * ath: \PLO\src\main\java\Utils\Grammer\Production.java @Description:

 */

package Utils.Grammer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hibernate.validator.internal.util.privilegedactions.NewInstance;

import Utils.Token.NonTerminal;
import Utils.Token.Terminal;
import Utils.Token.Token;
import Utils.Token.Word;

public class Production {
    public NonTerminal index;
    public List<Vector<Token>> target;
    // preDefine is a map for the previous defined Terminals
    public static HashMap<Character, Class<? extends Terminal>> preDefined;

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
     * @description define grammar, # means empty set
     * @param productions String representation of the productions, like "A BaMC;M Bd; B #"
     * @return
     */
    public static List<Production> translate(String productions)throws Exception {
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
                    case '#' -> {
                            Vector<Token> tmp = new Vector<>();
                            tmp.add(new Token("#"));
                            Production pt = new Production(new NonTerminal(index), tmp);
                            production.or(pt);
                        }
                        // or
                    case '|' -> {
                            Production pt = new Production(new NonTerminal(index), toAdd);
                            production.or(pt);
                            toAdd = new Vector<>();
                        }

                    default -> {
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
            List<Vector<Token>> target = production.target;
            for (Vector<Token> ts : target) {
                for(Token t:ts){
                    String name = t.sign;
                    if(indexes.contains(new NonTerminal(name))){
                        t.type = "NonTerminal";
                    }
                }
            }
        });
        return res;
    }

    public static void addDefinition(Character c, Class<? extends Terminal> defineClass) {
        if (preDefined == null) {
            preDefined = new HashMap<>();
        }
        preDefined.put(c, defineClass);
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
    public static double eval(final String str,Map<Word,Double> variables) {

        return new Object() {
            int pos = -1, ch;
    
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
                if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
                return x;
            }
    
            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor
    
            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }
    
            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }
    
            double parseFactor() {
                if (eat('+')) return parseFactor(); // unary plus
                if (eat('-')) return -parseFactor(); // unary minus
    
                double x = ;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if(Character.isLetterOrDigit(ch)){ // words predefined
                    int tmp = this.pos;
                    while(Character.isLetterOrDigit(ch)) nextChar();
                    String words = str.substring(startPos, this.pos);
                    //find 
                    if(variables.containsKey(new Word(words))){
                        x = variables.get(new Word(words));
                    }
                    // back
                    else{
                        pos = tmp;
                    }
                }else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    else throw new RuntimeException("Unknown function: " + func);
                } 
                
                else {
                    throw new RuntimeException("Unexpected: " + (char)ch);
                }
    
                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation
    
                return x;
            }
        }.parse();
    }

    @Override public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Production p = (Production)obj;
        return this
            .index
            .equals(p.index) && this
            .target
            .containsAll(p.target)&& p.target.containsAll(this.target);
    }
}
