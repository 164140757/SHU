package Cores.Lexer;

import Cores.Lexer.Token.Tag;
import Cores.Lexer.Token.Token;
import Cores.Lexer.Token.Word;
import Exceptions.SyntaxException;
import Utils.IO.Writer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LexerTest {

    @Test
    void scanOutputIdentifierStatistics() throws IOException {
        File folder = new File("src\\test\\java\\resources\\IdentifiersTest");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].isFile()) {
                File file = listOfFiles[i];
                String filePath = file.getParent().toString();
                Writer writer = null;
                try {
                    writer = new Writer(filePath + "\\result\\" + file.getName().split("\\.")[0] + "Result.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Lexer lexer = new Lexer();
                try {
                    lexer.input(filePath + "\\" + file.getName());
                    lexer.scan();
                } catch (IOException | SyntaxException e) {
                    e.printStackTrace();
                }
                HashMap<Word, Integer> statisticMap = lexer.getIDNums();
                Vector<String> context = new Vector<>();
                statisticMap.forEach((k, v) -> {
                    context.addAll(Arrays.asList("(", k.lexeme,": ", Integer.toString(v), ")", "\n"));
                });
                assert writer != null;
                writer.write(context);
            }
        }
    }

    @Test
    void outputFormat() {
        File file = new File("src\\test\\java\\resources\\IdentifiersTest\\case01.txt");
        System.out.println(file.getName().split("\\.")[0] + "Result.txt");
    }

    @Test
    void whiteSpace() {
        System.out.println(Character.isWhitespace(' '));
    }

    @Test
    void stringBufferClean() {
        StringBuffer sBuf = new StringBuffer();
        sBuf.append('1');
        sBuf.append('1');
        sBuf.append('1');
        System.out.println(sBuf.toString());
        sBuf.append('2');
        System.out.println(sBuf.toString());
// don't clean itself
    }

    @Test
    void StringBuilderTest() {
        StringBuilder builder = new StringBuilder("BHT6666");
        builder.delete(3, 5);
        assertEquals(builder.toString(), "BHT66");
    }


    @Test
    void scanTest() throws IOException, SyntaxException {
        Lexer lexer = new Lexer();
        lexer.input("src\\test\\java\\resources\\IdentifiersTest\\case01.txt");
        lexer.scan();
    }
}