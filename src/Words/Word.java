package Words;

import MIPSSyntax.OP;
import MIPSSyntax.REG;
import MIPSSyntax.memoryMap;

import java.security.Key;
import java.util.Arrays;

// Represents a word in memory
public class Word {

    protected boolean[] bits = new boolean[32];

    public Word() { }

    // Debug constructor. Mainly to be used for creating words that are broken and stored as 0xFFFFFFFF
    public Word(boolean debug) { if (debug) Arrays.fill(bits, true); }

    public Word(boolean[] word) { System.arraycopy(word, 0, bits, 0, 32); }

    public Word(int val) {
        this(Word.decimalToBinary(val, 32));
    }

    public Word(String hexNum) { System.arraycopy(hexToBinary(hexNum), 0, bits, 0, 32); }


    public String toHex() { // Similar to toString method except it converts it to the hexadecimal bytecode (This will be the final output for this assignment)
        StringBuilder res = new StringBuilder();
        for (int i=0; i<bits.length; i+=4) {
            boolean[] cur = {bits[i],bits[i+1],bits[i+2],bits[i+3]};
            for (int j=0; j<binary.length; j++) {
                boolean match = true;
                for (int k=0; k<4; k++)  {
                    if (binary[j][k] != cur[k])  match = false;
                }

                if (match) {
                    res.append(hex[j]);
                    break;
                }
            }
        }
        return res.toString();
    }

    public String toBinary() {
        StringBuilder sb = new StringBuilder();
        for (boolean bit: this.bits) sb.append(bit ? '1' : '0');
        return sb.toString();
    }

    public int toInt() {
        return Word.binaryToDecimal(this.bits, 0, this.bits.length-1, true); // TODO Is this right?
    }

    public void setByte(boolean[] b, int byteNum) {
        for (int i=0; i<8; i++) {
            this.bits[i+8*byteNum] = b[i];
        }
    }

    /**
     *
     * @param byteNum selects byte 0 to 3
     */
    public boolean[] getByte(int byteNum) {
        boolean[] res = new boolean[8];
        for (int i=0; i<8; i++) {
            res[i] = this.bits[i+8*(3-byteNum)];
        }
        return res;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (boolean b: bits) sb.append(b ? '1' : '0');
        return sb.toString();
    }

    public void execute(memoryMap mem) {
        throw new IllegalStateException("This word is not an instruction!");
    }



    // #######################################################################################################
    // #                                     Static Helper Methods                                           #
    // #######################################################################################################

    public static int binaryToDecimal(boolean[] w, int startIndex, int endIndex, boolean enableNegatives) {
        int res = 0;

        for (int i=0; i<=endIndex-startIndex; i++)
            if ((enableNegatives && w[startIndex]) != w[endIndex-i]) res += Math.pow(2,i);
        if (enableNegatives && w[startIndex]) // Twos complement logic
            res = -(res+1);

        return res;
    }

    public static int binaryToDecimal(boolean[] w, boolean enableNegatives) {
        return binaryToDecimal(w, 0, w.length-1, enableNegatives);
    }

    public static String binaryToHex(boolean[] w, int startIndex, int endIndex) {
        StringBuilder res = new StringBuilder();

        boolean[] temp = new boolean[4];

        // do the initial 4 bits, accounting for if endIndex-startIndex isnt a multiple of 4
        int numInitChars = (endIndex-startIndex+1)%4;
        for (int i=0; i<numInitChars; i++) {
            temp[i+(4-numInitChars)] = w[startIndex+i];
        }
        if (numInitChars != 0) res.append(binaryToHexChar(temp));

        // do the other sections 4 bits at a time
        for (int i=numInitChars; i+startIndex<=endIndex; i+=4) {
            for (int j=0; j<4; j++) {
                temp[j] = w[startIndex+i+j];
            }
            res.append( binaryToHexChar(temp));
        }

        return res.toString();
    }

    public static String binaryToHex(boolean[] w) {
        return binaryToHex(w, 0, w.length-1);
    }

    public static boolean[] decimalToBinary(int num, int length) {
        boolean needsTwosComplement = num < 0;
        boolean[] res = new boolean[length];
        int og = num;
        int index = res.length-1;
        while (index >= 0 && Math.abs(num) >= 1) {
            if (num % 2 == 1 || num % 2 == -1) res[index] = true;
            num /= 2;
            index--;
        }

        if (needsTwosComplement) { //Find twos complement
            for (int i=0; i<res.length; i++) { res[i] = !res[i]; }

            boolean carry = true; // add 1
            for (int i=res.length-1; i>=0; i--) {
                res[i] = res[i] ^ carry;
                carry = !res[i] && carry; // some weird logic since res[i] was just changed.
            }
        }

        return res;
    }

    public static boolean[] hexToBinary(String num) {
        boolean[] res = new boolean[num.length()*4];

        for (int i=0; i<num.length(); i++) {
            char curChar = Character.isUpperCase(num.charAt(i)) ? Character.toLowerCase(num.charAt(i)) : num.charAt(i);
            for (int j=0; j<hex.length; j++) {
                if (curChar == hex[j]) {
                    for (int k=0; k<binary[j].length; k++) {
                        res[i*4+k] = binary[j][k];
                    }
                }
            }
        }

        return res;
    }

    public static boolean[] binaryStringToArray(String b) {
        boolean[] res = new boolean[b.length()];
        for (int i=0; i<b.length(); i++) {
            res[i] = b.charAt(i) == '1';
        }

        return res;

    }

    // LUT is probably an efficient implementation from hex to binary and vice versa
    protected static final char[] hex = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
    protected static final boolean[][] binary = {
            {false, false, false, false}, {false, false, false, true}, {false, false, true, false}, {false, false, true, true},
            {false, true, false, false}, {false, true, false, true}, {false, true, true, false}, {false, true, true, true},
            {true, false, false, false}, {true, false, false, true}, {true, false, true, false}, {true, false, true, true},
            {true, true, false, false}, {true, true, false, true}, {true, true, true, false}, {true, true, true, true}
    };

    private static char binaryToHexChar(boolean[] w) {
        if (w.length != 4) return hex[0];
        for (int i=0; i<binary.length; i++) {
            boolean notSame = false;
            for (int j=0; j<4; j++) {
                if (binary[i][j] != w[j]) {
                    notSame = true;
                    break;
                }
            }
            if (!notSame) return hex[i];
        }
        return hex[0];
    }
}
