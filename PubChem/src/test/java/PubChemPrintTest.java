import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.PubChemConnect;
import viper.sentinox.PubChemGet;
import viper.sentinox.PubChemPrint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class PubChemPrintTest {

    private PubChemPrint pubChemPrint;

    @BeforeEach
    void setUp() {
        PubChemConnect pubChemConnect = new PubChemConnect("ibuprofen");
        PubChemGet pubChemGet = new PubChemGet(pubChemConnect);
        pubChemPrint = new PubChemPrint(pubChemGet);
    }

    @Test
    void printAllInfo_printsSomething() throws IOException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            pubChemPrint.printAllInfo();
            String output = outContent.toString();

            assertNotNull(output);
            assertFalse(output.isBlank());
            assertTrue(output.contains("Record"));
        } finally {
            System.setOut(originalOut);
        }
    }

    @Test
    void printReactions_printsSomething() throws IOException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            pubChemPrint.printReactions();
            String output = outContent.toString();

            assertNotNull(output);
            assertFalse(output.isBlank());
        } finally {
            System.setOut(originalOut);
        }
    }
}//test//test