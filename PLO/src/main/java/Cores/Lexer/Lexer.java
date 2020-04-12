package Cores.Lexer;

import Cores.Lexer.Token.Tag;
import Cores.Lexer.Token.Token;
import Cores.Lexer.Token.Word;
import Exceptions.SyntaxException;
import Utils.IO.Reader;

import java.io.IOException;
import java.io.PushbackReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Lexer {
    private PushbackReader stream;
    private char peek = ' ';
    private String s = null;
    // words for reserved words and ID
    private HashMap<String, Token> words;
    // only for ID nums
    private HashMap<Word, Integer> IDNums;

    public Lexer() {
        words = new HashMap<>();
        IDNums = new HashMap<>();
    }

    public void input(String filePath) throws IOException {
        Reader reader = new Reader(filePath);
        stream = new PushbackReader(reader.getReader());
    }


    // scan to words
    public void scan() throws IOException, SyntaxException {
        if (stream == null) {
            throw new IOException("Input is null.");
        }
        // handle whitespace
        int r;
        while ((r = stream.read()) != -1 && Character.isDefined((char)r)) {
            peek = (char) r;
            // handle comments
            handleComments();
            // ignore white space || end of file
            while (Character.isWhitespace(peek)) {
                peek = (char) stream.read();
            }

            // check digits, identifier, reserved words, delimiters, basics
            StringBuilder sBuf = new StringBuilder();
            // get a buffer to store a continuous input
            // isDefined for the safety purpose:
            // the buffer inside PushbackReader is of type char[],
            // so the integer -1 will get converted to char 0xFFFF(illegal
            while (Character.isDefined(peek)&&!Character.isWhitespace(peek)) {
                sBuf.append(peek);
                peek = (char) stream.read();
            }

            s = sBuf.toString();
            // handle tokens of different types
            handleTokens();
        }
    }

    private void handleTokens() {
        // getNums();
        // basic first , ID second
        getBASIC();
        getID();
        // getOPE();
        // getDELIMITER();
    }

    private void handleComments() throws IOException, SyntaxException {
        if (!Character.isWhitespace(peek)) {
            // handle comments
            if (peek == '/') {
                peek = (char) stream.read();
                if (peek == '/') {
                    // single line comment
                    for (; ; peek = (char) stream.read()) {
                        if (peek == '\n') {
                            break;
                        }
                    }
                } else if (peek == '*') {
                    // block comment
                    char prevPeek = ' ';
                    for (; ; prevPeek = peek, peek = (char) stream.read()) {
                        if (prevPeek == '*' && peek == '/') {
                            break;
                        }
                    }
                }
            }
        }

    }

    private void getNums() {
        if (s.length() == 0) {
            return;
        }
        Pattern pattern = Pattern.compile(Tag.NUM.pattern);
        matchCheck(pattern, Tag.NUM);
    }

    private void getID() {
        if (s.length() == 0) {
            return;
        }
        Pattern pattern = Pattern.compile(Tag.ID.pattern, Pattern.CASE_INSENSITIVE);
        matchCheck(pattern, Tag.ID);
    }

    private void getOPE() {
        if (s.length() == 0) {
            return;
        }
        Pattern pattern = Pattern.compile(Tag.OPE.pattern);
        matchCheck(pattern, Tag.OPE);
    }

    private void getBASIC() {
        if (s.length() == 0) {
            return;
        }
        Pattern pattern = Pattern.compile(Tag.BASIC.pattern, Pattern.CASE_INSENSITIVE);
        matchCheck(pattern, Tag.BASIC);
    }

    private void getDELIMITER() {
        if (s.length() == 0) {
            return;
        }
        Pattern pattern = Pattern.compile(Tag.DELIMITER.pattern);
        matchCheck(pattern, Tag.DELIMITER);
    }

    private void matchCheck(Pattern pattern, Tag tag) {
        Matcher matcher = pattern.matcher(s);
        // find groups and concat left
        // back up
        String s_ = s;
        while (matcher.find()) {
            // source string s : start and end
            int start = matcher.start();
            int end = matcher.end();
            String sub = s.substring(start, end);
            if (tag == Tag.NUM) {
                //TODO add variable table
            } else {
                Word word = new Word(tag, sub);
                words.put(sub, word);
                // ID nums
                if (tag == Tag.ID) {
                    IDNums.merge(word, 1, Integer::sum);
                }
            }
            // concat
            s_ = s_.replace(sub, "");
        }
        // After a specific type of token has been handle, shrink the string s
        s = s_;
        // to make it more efficient
    }

    public HashMap<Word, Integer> getIDNums() {
        return IDNums;
    }

    public HashMap<String, Token> getWords() {
        return words;
    }
}
