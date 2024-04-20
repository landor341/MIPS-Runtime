import Words.Word;
import Words.WordMatcher;

import java.io.BufferedReader;
import java.io.FileReader;

public class runner {

    public static void main(String[] args) {

//        bigTests();
        milestone1(args);




//        String test = "11010101111"; // In hex, it represents 6af
//        System.out.println(Word.binaryToHex(Word.binaryStringToArray(test), 0, test.length()-1));
    }

    public static void milestone1(String[] args) {
        for (String a: args) {
            Word result = WordMatcher.matchWord(a);
            System.out.println(result.toString());
        }
    }

    public static void bigTests() {
        try {
            BufferedReader tests = new BufferedReader(new FileReader("src/tests.txt"));

            int i=0;
            while (tests.ready()) {
                String line = tests.readLine();
                String word = line.split(" ", 2)[0];
                String testCase = line.split(" ", 2)[1];


//                System.out.println("Test " + i++ + " With testCase: " + testCase);

                Word result = WordMatcher.matchWord(word);
                if (!testCase.equals(result.toString())) {
                    System.out.println("Expected: " + testCase + "         Actual: " + result.toString());
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void tester() {
        String[][] testInputs = new String[][] {
                {"020ed020", "add {opcode: 00, rs: 10, rt: 0e, rd: 1a, shmt: 00, funct: 20}"},
                {"246600e0", "addiu {opcode: 09, rs(base): 03, rt: 06, immediate(offset): 00e0}"},
                {"03116024", "and {opcode: 00, rs: 18, rt: 11, rd: 0c, shmt: 00, funct: 24}"},
                {"3279ff12", "andi {opcode: 0c, rs(base): 13, rt: 19, immediate(offset): ff12}"},
                {"11190030", "beq {opcode: 04, rs(base): 08, rt: 19, immediate(offset): 0030}"},
                {"1725001d", "bne {opcode: 05, rs(base): 19, rt: 05, immediate(offset): 001d}"},
                {"080000fa", "j {opcode: 02, index: 00000fa}"},
                {"3c050016", "lui {opcode: 0f, rs(base): 00, rt: 05, immediate(offset): 0016}"},
                {"8f6600c8", "lw {opcode: 23, rs(base): 1b, rt: 06, immediate(offset): 00c8}"},
                {"0141b025", "or {opcode: 00, rs: 0a, rt: 01, rd: 16, shmt: 00, funct: 25}"},
                {"36d2008d", "ori {opcode: 0d, rs(base): 16, rt: 12, immediate(offset): 008d}"},
                {"005ae82a", "slt {opcode: 00, rs: 02, rt: 1a, rd: 1d, shmt: 00, funct: 2a}"},
                {"01623022", "sub {opcode: 00, rs: 0b, rt: 02, rd: 06, shmt: 00, funct: 22}"},
                {"afcb0000", "sw {opcode: 2b, rs(base): 1e, rt: 0b, immediate(offset): 0000}"},
                {"0000000c", "syscall {opcode: 00, code: 000000, funct: 0c}"}, // TODO: Let him know his test data was wrong
                {"003f2a0c", "syscall {opcode: 00, code: 0fca8, funct: 0c}"}
        };

        for (String[] a: testInputs) {
            Word result = WordMatcher.matchWord(a[0]);
            if (!a[1].equals(result.toString())) {
                System.out.println("Expected: " + a[1] + "         Actual: " + result.toString());
            }
        }
    }
}
