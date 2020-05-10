package Cores;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Vector;

import org.junit.jupiter.api.Test;

import Cores.*;
import Utils.Token.*;
import Utils.IO.Writer;

class LexerTest {

    @Test
    void scanOutputIdentifierStatistics() {
        File folder = new File("src\\test\\java\\resources\\IdentifiersTest");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].isFile()) {
                File file = listOfFiles[i];
                String filePath = file.getParent();
                Writer writer = null;
                try {
                    writer = new Writer(filePath + "\\Statistic\\" + file.getName().split("\\.")[0] + "Result.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Lexer lexer = new Lexer();
                try {
                    lexer.input(filePath + "\\" + file.getName());
                    Token t;
                    do{
                        t = lexer.scan();
                    }while(t!=null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                HashMap<Word, Integer> statisticMap = lexer.getIDNums();
                Vector<String> context = new Vector<>();
                statisticMap.forEach((k, v) -> {
                    context.addAll(Arrays.asList("(", k.context,": ", Integer.toString(v), ")", "\n"));
                });
                assert writer != null;
                writer.write(context);
            }
        }
    }

    @Test
    void outPutTypeTest(){
        File folder = new File("src\\test\\java\\resources\\IdentifiersTest");
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].isFile()) {
                File file = listOfFiles[i];
                String filePath = file.getParent();
                Writer writer = null;
                try {
                    writer = new Writer(filePath + "\\Statistic\\" + file.getName().split("\\.")[0] + "Result.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Lexer lexer = new Lexer();
                try {
                    assert writer != null;
                    lexer.input(filePath + "\\" + file.getName());
                    Vector<String> context = new Vector<>();
                    Token t;
                    do {
                        t = lexer.scan();
                        if(t instanceof Word){
                            Word w = (Word) t;
                            context.add("("+w.type+", "+w.context+")"+"\n");
                        }
                        if(t instanceof Num){
                            Num n = (Num) t;
                            context.add("("+n.type+", "+n.value+")"+"\n");
                        }
                        if(t instanceof Ope){
                            Ope n = (Ope) t;
                            context.add("("+n.type+", "+n.context+")"+"\n");
                        }
                        if(t instanceof Del){
                            Del n = (Del) t;
                            context.add("("+n.type+", "+n.context+")"+"\n");
                        }
                    } while (t != null);
                    writer.write(context);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Test
    void specificFileTest() throws IOException {
        Writer writer = null;
        try {
            writer = new Writer("src\\test\\java\\resources\\IdentifiersTest" + "\\result\\" + "case02Result.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Lexer lexer = new Lexer();
        lexer.input("src\\test\\java\\resources\\IdentifiersTest\\case02.txt");
        Token t;
        do{
            t = lexer.scan();
        }while(t!=null);
        HashMap<Word, Integer> statisticMap = lexer.getIDNums();
        Vector<String> context = new Vector<>();
        statisticMap.forEach((k, v) -> context.addAll(Arrays.asList("(", k.context,": ", Integer.toString(v), ")", "\n")));
        assert writer != null;
        writer.write(context);
    }
}