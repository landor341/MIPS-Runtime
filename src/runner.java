import MIPSSyntax.REG;
import MIPSSyntax.memoryMap;
import Words.Word;
import Words.WordMatcher;
import config.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class runner {

    public static final int DATA_START_ADDRESS = 0x10010000;
    public static final int TEXT_START_ADDRESS = 0x00400000;

    public static void main(String[] args) throws IOException {

        // Test inputs: ["src/tests/st_lw_test/st_lw_test.data", "src/tests/st_lw_test/st_lw_test.text"]
        //              ["src/tests/fibonacci/fibonacci.data", "src/tests/fibonacci/fibonacci.text"]
        //              ["src/tests/EvenOrOdd/EvenOrOdd.data", "src/tests/EvenOrOdd/EvenOrOdd.text"]
//        String[] tests = new String[] {"EvenOrOdd", "fibonacci", "st_lw_test"};
//        String curTest = tests[1];
//        milestone2(new String[] {"src/tests/" + curTest + "/" + curTest + ".data","src/tests/" + curTest + "/" + curTest + ".text"});

                milestone2(args);
    }

    public static void milestone2(String[] args) throws IOException {
        // Create new memory map
        memoryMap mem = new memoryMap();

        if (config.verbose) System.out.println("------------------------- Loading MEM ---------------------\n");
        try {
            // Go through the data file to map the memory location
            BufferedReader data = new BufferedReader(new FileReader(args[0]));
            int curAddress = DATA_START_ADDRESS;
            while (data.ready()) {
                String cur = data.readLine();
                mem.storeWord(curAddress, new Word(cur));
                if (config.verbose) System.out.println(curAddress + ":  " + mem.getWord(curAddress) + "     " + cur);
                curAddress+=4;
            }
        } catch (Exception e) {
            System.out.println("Error loading data");
        }
        if (config.verbose) System.out.println("\n\n------------------------- Loading Code ---------------------\n\n");

        try {
            // Go through the text file to create an indexed array of commands (indexed for the purpose of jumping and managing PC)
            BufferedReader text = new BufferedReader(new FileReader(args[1]));
            int curAddress = TEXT_START_ADDRESS;
            while (text.ready()) {
                String cur = text.readLine();
                mem.storeWord(curAddress, WordMatcher.matchWord(cur));
                if (config.verbose) System.out.println(mem.getWord(curAddress).toString() + "    " + cur); // TODO: LINE 10 OF EVENORODD.txt WAS CHANGED THE THIRD INDEX SHOULD HAVE A 1
                curAddress+=4;
            }
            if (config.verbose) System.out.println("\n\n------------------------- EXECUTING ---------------------\n\n");

        } catch (Exception e) {
            throw e;
        }

        // Go through each of those commands executing them one at a time.
        REG.PC.storedVal = TEXT_START_ADDRESS;
        while (mem.getWord(REG.PC.storedVal) != null) {
            if (config.verbose) System.out.println(REG.PC.storedVal + ": " + mem.getWord(REG.PC.storedVal).toString());
            mem.getWord(REG.PC.storedVal).execute(mem);
            REG.PC.storedVal+=4;
        }

    }

    public static void milestone1(String[] args) {
        for (String a: args) {
            Word result = WordMatcher.matchWord(a);
            System.out.println(result.toString());
        }
    }

    public static void bigMilestone1Tests() {
        try {
            BufferedReader tests = new BufferedReader(new FileReader("src/tests.txt"));

            int i=0;
            while (tests.ready()) {
                String line = tests.readLine();
                String word = line.split(" ", 2)[0];
                String testCase = line.split(" ", 2)[1];


//                System.out.println("Test " + i++ + " With testCase: " + testCase);

                Word result = WordMatcher.matchWord(word);
                if (config.verbose || !testCase.equals(result.toString())) {
                    System.out.println("Expected: " + testCase + "         Actual: " + result.toString());
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
