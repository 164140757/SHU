package Cores;

//import Utils.IO.Reader;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import Utils.Token.*;


public class Lexer {
    private PushbackReader stream;
    public int line = 1;
    private char peek = ' ';
    private HashSet<String> funcs;
    // words for reserved words and ID
    private HashMap<String, Word> words;
    private final int pushBackBufSize = 2;

    public Lexer() {
        init();
    }

    public Lexer(File file) {
        init();
        try {
            stream = new PushbackReader(new FileReader(file),pushBackBufSize);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        words = new HashMap<>();
        funcs = new HashSet<>(Arrays.asList("sin", "cos", "tan", "sqrt"));
        // reversed words
        reserve("begin", "call", "const", "do", "end"
                , "if", "else", "odd", "procedure", "read", "then",
                "var", "while", "write");
        // reserve functions
        reserve(funcs);
        Ope.init();
        Del.init();

    }
//
//    public void input(String filePath) throws IOException {
//        Reader reader = new Reader(filePath);
//        stream = new PushbackReader(reader.getReader(),pushBackBufSize);
//    }
    public void input(File test) throws IOException {
        stream = new PushbackReader(new FileReader(test),pushBackBufSize);
    }
    public void inputString(String s) {
        StringReader reader = new StringReader(s);
        stream = new PushbackReader(reader,pushBackBufSize);
    }

    private void reserve(String... lexemes) {
        for (String l : lexemes) {
            Word word = new Word(l);
            word.subType ="reservedWord" ;
            words.put(l, word);
            Word upper = new Word(l.toUpperCase());
            upper.subType="reservedWord";
            words.put(l.toUpperCase(),upper);
        }
    }

    private void reserve(Collection<String> lexemes) {
        lexemes.forEach(e -> {
            Word word = new Word(e);
            word.subType ="reservedWord" ;
            words.put(e, word);
            Word upper = new Word(e.toUpperCase());
            upper.subType="reservedWord";
            words.put(e.toUpperCase(),upper);
        });
    }


    // scan a Token
    public Token scan() throws IOException {
        if (stream == null) {
            throw new IOException("Input is null.");
        }
        // white space
        do {
            int p = stream.read();
            if (p == -1 || !Character.isDefined((char) p)) {
                // end of file or invalid
                return null;
            }
            peek = (char) p;
        } while (Character.isWhitespace(peek));
        // handle comments
        handleComments();
        // check digits, identifier, reserved words, delimiters
        // digits
        if (Character.isDigit(peek)) {
            int v = 0;
            do {
                v = 10 * v + Character.digit(peek, 10);
                peek = (char) stream.read();
            } while (Character.isDigit(peek));
            stream.unread(peek);
            return new Num(v);
        }
        // identifier, reserved words
        if (Character.isLetter(peek)) {
            StringBuilder sBuf = new StringBuilder();
            do {
                sBuf.append(peek);
                peek = (char) stream.read();
            } while (Character.isLetterOrDigit(peek)||peek == '_');
            // push back
            stream.unread(peek);
            String s = sBuf.toString();
            Word w = words.get(s);
            if (w != null) {
                return w;
            }
            w = new Word(s);
            words.put(s, w);
            return w;
        }
        // operators
        if (isOpe(peek)) {
            Ope ope = new Ope(String.valueOf(peek));
            // single
            switch (peek) {
                case '+' :{
                    ope.subType = "plus";
                    return ope;
                }
                case '-' : {
                    ope.subType = "minus";
                    return ope;
                }
                case '*' : {
                    ope.subType = "multiply";
                    return ope;
                }
                case '/' : {
                    ope.subType = "divide";
                    return ope;
                }
                case '=' :{
                    ope.subType = "equal";
                    return ope;
                }
                case '#' : {
                    ope.subType = "unequal";
                    return ope;
                }
            }
            // double
            if (peek == '>' || peek == '<' || peek == ':') {
                StringBuilder b = new StringBuilder();
                b.append(peek);
                peek = (char) stream.read();
                if (peek == '=') {
                    b.append(peek);
                } else {
                    // push back
                    stream.unread(peek);
                }
                String s = b.toString();
                ope = new Ope(s);
                switch (s) {
                    case ">" : {
                        ope.subType = "greaterThan";
                        return ope;
                    }
                    case ">=" : {
                        ope.subType = "greaterThanOrEqual";
                        return ope;
                    }
                    case "<" : {
                        ope.subType = "lessThan";
                        return ope;
                    }
                    case "<=" : {
                        ope.subType = "lessThanOrEqual";
                        return ope;
                    }
                    case ":=" : {
                        ope.subType = "becomes";
                        return ope;
                    }
                }
            }
        }
        // delimiters
        if (isDel(peek)) {
            Del del = new Del(String.valueOf(peek));
            switch (peek) {
                case '(': {
                    del.subType = "lParen";
                    return del;
                }
                case ')': {
                    del.subType = "rParen";
                    return del;
                }
                case ',' : {
                    del.subType = "comma";
                    return del;
                }
                case ';': {
                    del.subType = "semicolon";
                    return del;
                }
                case '.': {
                    del.subType = "period";
                    return del;
                }
                // for production
                case '|' : {
                    del.subType = "separator";
                    return del;
                }
            }
        }
        return null;
    }

    private boolean isDel(char peek) {
        return Del.isDel(peek);
    }

    private boolean isOpe(char peek) {
        return Ope.isOpe(peek);
    }

    private void handleComments() throws IOException {
        if (!Character.isWhitespace(peek)) {
            // handle comments
            if (peek == '/') {
                peek = (char) stream.read();
                if (peek == '/') {
                    // single line comment
                    for (; ; peek = (char) stream.read()) {
                        if (peek == '\n') {
                            peek = (char) stream.read();
                            break;
                        }
                    }
                } else if (peek == '*') {
                    // block comment
                    char prevPeek = ' ';
                    for (; ; prevPeek = peek, peek = (char) stream.read()) {
                        if (prevPeek == '*' && peek == '/') {
                            peek = (char) stream.read();
                            break;
                        }
                    }
                } else {
                    stream.unread(peek);
                    peek = '/';
                }

            }
        }

    }

    public HashMap<String, Word> getWords() {
        return words;
    }


    public HashSet<String> getFuncs() {
        return funcs;
    }
}
