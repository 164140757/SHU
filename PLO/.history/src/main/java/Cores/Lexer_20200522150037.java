package Cores;

import Utils.IO.Reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.io.StringReader;
import java.util.HashMap;

import Utils.Token.*;


public class Lexer {
    private PushbackReader stream;
    public int line = 1;
    private char peek = ' ';
    // words for reserved words and ID
    private HashMap<String, Word> words;
    private HashMap<Word, Integer> IDNums;

    public Lexer() {
        init();
    }

    private void init() {
        words = new HashMap<>();
        IDNums = new HashMap<>();
        // reversed words
        reserve("begin", "call", "const", "do", "end"
                , "if", "else", "odd", "procedure", "read", "then",
                "var", "while", "write");
        Ope.init();
        Del.init();

    }

    public void input(String filePath) throws IOException {
        Reader reader = new Reader(filePath);
        stream = new PushbackReader(reader.getReader());
    }

    public void inputString(String s){
        StringReader reader = new StringReader(s);
        stream = new PushbackReader(reader);
    }

    public void reserve(String... lexemes) {
        for (String l : lexemes) {
            Word word = new Word(l.toUpperCase(), l);
            words.put(word.type, word);
        }
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
            } while (Character.isLetterOrDigit(peek));
            // push back
            stream.unread(peek);
            String s = sBuf.toString().toUpperCase();
            // uppercase
            Word w = words.get(s);
            if (w != null) {
                // ID numbers
                if (w.type.equals("ID")) {
                    IDNums.put(w, IDNums.get(w) + 1);
                }
                return w;
            }
            w = new Word("ID", s);
            words.put(s, w);
            IDNums.put(w, 1);
            return w;
        }
        // operators
        if (isOpe(peek)) {
            Ope ope = new Ope(" ");
            ope.context = String.valueOf(peek);
            // single
            switch (peek) {
                case '+' -> {
                    ope.name = "plus";
                    return ope;
                }
                case '-' -> {
                    ope.name = "minus";
                    return ope;
                }
                case '*' -> {
                    ope.name = "multiply";
                    return ope;
                }
                case '/' -> {
                    ope.name = "divide";
                    return ope;
                }
                case '=' -> {
                    ope.name = "equal";
                    return ope;
                }
                case '#' -> {
                    ope.name = "unequal";
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
                }
                else{
                    // push back
                    stream.unread(peek);
                }
                String s = b.toString();
                switch (s) {
                    case ">" -> {
                        ope.name = "greaterThan";
                        return ope;
                    }
                    case ">=" -> {
                        ope.name = "greaterThanOrEqual";
                        return ope;
                    }
                    case "<" -> {
                        ope.name = "lessThan";
                        return ope;
                    }
                    case "<=" -> {
                        ope.name = "lessThanOrEqual";
                        return ope;
                    }
                    case ":=" -> {
                        ope.name = "becomes";
                        return ope;
                    }
                }
            }
        }
        // delimiters
        if (isDel(peek)) {
            Del del = new Del(" ");
            switch (peek) {
                case '(', '（' -> {
                    del.name = "lParen";
                    return del;
                }
                case ')', '）' -> {
                    del.name = "rParen";
                    return del;
                }
                case ',', '，' -> {
                    del.name = "comma";
                    return del;
                }
                case ';', '；' -> {
                    del.name = "semicolon";
                    return del;
                }
                case '.' -> {
                    del.name = "period";
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
                }

            }
        }

    }

    public HashMap<String, Word> getWords() {
        return words;
    }

    public HashMap<Word, Integer> getIDNums() {
        return IDNums;
    }
}
