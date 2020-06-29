package UI;

import Cores.Lexer;
import Cores.Parser;
import Utils.Grammer.Grammar;
import Utils.Grammer.Production;
import Utils.Token.NonTerminal;
import Utils.Token.Num;
import Utils.Token.Token;
import Utils.Token.Word;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestPanel extends JPanel {
    public static File selectedFile;
    private JButton browseButton;
    private JScrollPane ResultPanel;
    private final int INIT_W = 900;
    private final int INIT_H = 650;

    TestPanel(MigLayout layout) {
        super(layout);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("验收界面");

        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        TestPanel rootPanel = new TestPanel(new MigLayout("wrap 1", "[grow]", "[grow]"));
        frame.setBounds(p.x - rootPanel.getINIT_W() / 2, p.y - rootPanel.getINIT_H() / 2, rootPanel.getINIT_W(), rootPanel.getINIT_H());
        JPanel browsePanel = new JPanel(new MigLayout("wrap 3", "grow", "20%"));
        JButton browseButton = new JButton("浏览");
        JButton runButton = new JButton("运行");
        JLabel message = new JLabel("请老师选择测试文件，然后点击运行查看结果");

        JTable predictTable =new JTable();
        predictTable.setFillsViewportHeight(true);
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        predictTable.setDefaultRenderer(Object.class, r);
        JScrollPane predictPane = new JScrollPane(predictTable);
        predictPane.setBorder(new TitledBorder("预测分析表"));

        JTable analyseTable = new JTable();
        analyseTable.setFillsViewportHeight(true);
        DefaultTableCellRenderer r1 = new DefaultTableCellRenderer();
        r1.setHorizontalAlignment(JLabel.CENTER);
        analyseTable.setDefaultRenderer(Object.class,r1);
        JScrollPane analysePane = new JScrollPane(analyseTable);
        analysePane.setBorder(new TitledBorder("分析过程"));

        JFileChooser fileChooser = new JFileChooser();
        File workingDirectory = new File(System.getProperty("user.dir")+"/src/test/java/resources/IFtest");
        fileChooser.setCurrentDirectory(workingDirectory);
        browseButton.addActionListener(actionEvent -> {
            int status = fileChooser.showOpenDialog(null);
            if (status == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
            }
        });
        runButton.addActionListener(e -> {
            if (selectedFile != null) {
                try {
                    Parser parser = LLRun(selectedFile);
                    fillPredictTable(parser, predictTable);
                    int res = fillAnalyseTable(parser, analyseTable);
                    if(res == 1){
                        analysePane.setBorder(new TitledBorder("分析结果为正确"+"过程如下"));
                    }
                    else if (res == -1){
                        analysePane.setBorder(new TitledBorder("分析结果为错位"+"LL(1)文法无法分析，上表相同索引存在交集"));
                    }
                    else{
                        analysePane.setBorder(new TitledBorder("分析结果为错误"+"LL(1)文法无法分析，语法错误"));
                    }
                    analysePane.revalidate();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
        browsePanel.add(message);
        browsePanel.add(browseButton);
        browsePanel.add(runButton);

        rootPanel.add(browsePanel, "grow");
        rootPanel.add(predictPane,"grow");
        rootPanel.add(analysePane,"grow");


        frame.setContentPane(rootPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static int fillAnalyseTable(Parser parser, JTable analyseTable) {
        String[] headers = {"步骤","分析栈","当前分析字符","所用产生式"};
        int res = 0;
        try {
            res = parser.LL();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // create table
        DefaultTableModel mode = (DefaultTableModel) analyseTable.getModel();
        mode.setColumnCount(4);
        // clean Table
        mode.setRowCount(0);
        mode.addRow(headers);
        Vector<String[]> data = parser.getAnalyseTable();
        for (String[] datum : data) {
            mode.addRow(datum);
        }
        analyseTable.repaint();
        return res;
    }

    private static void fillPredictTable(Parser parser, JTable jTable) {
        HashMap<Production, Set<Token>> table = parser.getSelectMap();
        DefaultTableModel tableModel = new DefaultTableModel();
        jTable.setModel(tableModel);
        DefaultTableModel mode = (DefaultTableModel) jTable.getModel();
        HashSet<Token> tokens = new HashSet<>();
        table.values().parallelStream().forEach(tokens::addAll);
        HashMap<String,Integer> colMap = new HashMap<>();
        // headers
        int j =1;
        mode.addColumn(" ");
        for (Token token : tokens) {
            colMap.put(token.context,j++);
            mode.addColumn(token.context);
        }
        // clean Table
        mode.setRowCount(0);
        int i =0;
        Vector<String[]> data = new Vector<>();
        // add rows
        for (Map.Entry<Production, Set<Token>> entry : table.entrySet()) {
            Production p = entry.getKey();
            Set<Token> t = entry.getValue();
            String[] row = new String[colMap.size()+1];
            row[0] = p.index.context;
            StringBuffer bf = new StringBuffer();
            p.target.get(0).forEach(t1-> bf.append(t1.context).append(" "));
            Set<Token> s = entry.getValue();
            for (Token token : t) {
                row[colMap.get(token.context)] ="→"+bf.toString() ;
            }
            data.add(row);
        }
        for (String[] datum : data) {
            mode.addRow(datum);
        }
        // refresh
        jTable.repaint();
    }


    public int getINIT_H() {
        return INIT_H;
    }

    public int getINIT_W() {
        return INIT_W;
    }

    public static void analyse(Parser parser,JTable jTable){


    }
    public static Parser LLRun(File testFile) throws IOException {
        // add definition
        Production.addDefinition("identifiers","ID");
        Production.addDefinition("NUM","N" );
        Grammar grammar = new Grammar(new NonTerminal("J"), Production.translate(new File("src/main/resources/Grammar_test.txt")));
        Lexer lexer = new Lexer();
        lexer.input(testFile);
        Parser parser = new Parser(grammar, lexer);
        parser.getFirst();
        parser.getFollow();
        parser.getSelect();
//        parser.printFirst();
//        parser.printFollow();
//        parser.printSelect();
        return parser;
    }
    //J->{S}:<ID >
    //S->{ID:=E}:<ID >
    //I->{F*F}:<( ID N >
    //CS->{IFCTHENS}:<IF >
    //J->{CS}:<IF >
    //I->{F}:<( ID N >
    //C->{ERE}:<( ID N >
    //F->{(E)}:<( >
    //F->{ID}:<ID >
    //I->{F/F}:<( ID N >
    //R->{<}:<< >
    //E->{I}:<( ID N >
    //R->{=}:<= >
    //E->{I-I}:<( ID N >
    //E->{I+I}:<( ID N >
    //R->{>}:<> >
    //F->{N}:<N >
    //J->{C}:<>
}
