package Words;

import MIPSSyntax.OP;
import MIPSSyntax.REG;
import MIPSSyntax.memoryMap;

public class RWord extends Word {
    //      32-26               25-21           20-16           15-11           10-6        5-0
    //   Opcode of 0              rs              rt              rd            shamt     function

    private final String rs,rt,rd,shamt,functionString;
    private final REG d,s,t;
    private final Integer off;
    private final OP function;

    public RWord(boolean[] w) {
        super(w);


        rs = Word.binaryToHex(w, 6, 10);
        s = REG.matchValue(Word.binaryToDecimal(w, 6, 10, false));
        rt = Word.binaryToHex(w, 11, 15);
        t = REG.matchValue(Word.binaryToDecimal(w, 11, 15, false));
        rd = Word.binaryToHex(w, 16, 20);
        d = REG.matchValue(Word.binaryToDecimal(w, 16, 20, false));
        shamt = Word.binaryToHex(w, 21, 25);
        off = Word.binaryToDecimal(w, 21, 25, true);
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

    public void execute(memoryMap mem) {
        this.function.apply(d,s,t,off,mem);
    }
}
