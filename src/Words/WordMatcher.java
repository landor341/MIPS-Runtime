package Words;

import MIPSSyntax.OP;

public class WordMatcher {
    public static Word matchWord(String hexWord) {
        return matchWord(Word.hexToBinary(hexWord));
    }


    public static Word matchWord(boolean[] w) {
        boolean[] opCode = new boolean[6];
        boolean foundOneTrue=false;
        for (int i=0; i<6; i++) {
            if (w[i]) foundOneTrue = true;
            opCode[i] = w[i];
        }

        if (foundOneTrue) { // I or J type instruction
            if (boolArrsEqual(opCode, OP.j.opcode)) return new JWord(w);
            return new IWord(w);
        } else { // R or syscall type instruction or nop
            for (int i=0; i<6; i++) {
                if (w[i+26]) foundOneTrue = true;
                opCode[i] = w[i+26];
            }
            if (foundOneTrue) { // R type instruction
                if (boolArrsEqual(opCode, OP.syscall.opcode)) return new SyscallWord(w);
                return new RWord(w);
            }

        }

        // Return a nop
        return new Word(true);
    }


    private static boolean boolArrsEqual(boolean[] a, boolean[] b) {
        if (a.length != b.length) return false;
        for (int i=0; i<a.length; i++) {
            if (a[i] != b[i]) return false;
         }
        return true;
    }

}
