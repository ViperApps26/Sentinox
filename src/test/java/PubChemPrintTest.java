import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import viper.sentinox.PubChemConnect;
import viper.sentinox.PubChemPrint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class PubChemPrintTest {

    @BeforeEach
    void setUp() {
        PubChemConnect.setMedicine("ibuprofen");
    }

    @Test
    void printAllInfo_printsSomething() throws IOException {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outContent));

        try {
            PubChemPrint.printAllInfo();
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
            PubChemPrint.printReactions();
            String output = outContent.toString();

            assertNotNull(output);
            assertFalse(output.isBlank());
        } finally {
            System.setOut(originalOut);
        }
    }
}
