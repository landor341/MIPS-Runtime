package Words;

import MIPSSyntax.OP;

public class RWord extends Word {
    //      32-26               25-21           20-16           15-11           10-6        5-0
    //   Opcode of 0              rs              rt              rd            shamt     function

    private final String rs,rt,rd,shamt,functionString;
    private final OP function;

    public RWord(boolean[] w) {
        super(w);

        rs = Word.binaryToHex(w, 6, 10);
        rt = Word.binaryToHex(w, 11, 15);
        rd = Word.binaryToHex(w, 16, 20);
        shamt = Word.binaryToHex(w, 21, 25);
        functionString = Word.binaryToHex(w, 26, 31);


        boolean[] functionArr = new boolean[6];
        for (int i=0 ;i<6; i++) {
            functionArr[i] = this.bits[26+i];
        }
        function = OP.matchOp(functionArr, true);
    }

    public String toString() {
        // EX: sub {opcode: 00, rs: 1e, rt: 1a, rd: 1a, shmt: 00, funct: 22}
        return function.name + " {opcode: 00, rs: " + rs + ", rt: " + rt + ", rd: " + rd + ", shmt: " + shamt + ", funct: " + functionString + "}";
    }
}
