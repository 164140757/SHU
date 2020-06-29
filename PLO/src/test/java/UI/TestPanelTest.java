package UI;

import Cores.Parser;
import Exceptions.GrammarError;
import Utils.Grammer.Production;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class TestPanelTest {

    @Test
    void translate() {
        Production.translate(new File("src/main/resources/Grammar_test.txt"));
        //identifiers CS
        //reservedWord IF
        //identifiers C
        //reservedWord THEN
        //identifiers S
        //Del semicolon
        //identifiers C
        //identifiers E
        //identifiers R
        //identifiers E
        //Del semicolon
        //identifiers R
        //OPE greaterThan
        //Del separator
        //OPE lessThan
        //Del separator
        //OPE equal
        //Del semicolon
        //identifiers S
        //identifiers ID
        //OPE becomes
        //identifiers E
        //Del semicolon
        //identifiers E
        //identifiers I
        //OPE plus
        //identifiers I
        //Del semicolon
        //identifiers E
        //identifiers I
        //OPE minus
        //identifiers I
        //Del semicolon
        //identifiers E
        //identifiers I
        //Del semicolon
        //identifiers I
        //identifiers F
        //OPE multiply
        //identifiers F
        //Del semicolon
        //identifiers I
        //identifiers F
        //OPE divide
        //identifiers F
        //Del semicolon
        //identifiers I
        //identifiers F
        //Del semicolon
        //identifiers F
        //identifiers E
        //Del semicolon
    }

    @Test
    void run() throws IOException, GrammarError {
        Parser parser =TestPanel.LLRun(new File("src/test/java/resources/IFtest/test2.txt"));
        parser.LL();
    }
}