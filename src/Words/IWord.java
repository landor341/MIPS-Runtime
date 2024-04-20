package Words;

import MIPSSyntax.OP;

public class IWord extends Word {
    //      32-26               25-21           20-16           15-0
    //      Opcode               rs               rt             imm

    private final String rs,rt,imm,opCodeString;
    private final OP op;

    public IWord(boolean[] w) {
        super(w);

        opCodeString = Word.binaryToHex(w, 0, 5);
        rs = Word.binaryToHex(w, 6, 10);
        rt = Word.binaryToHex(w, 11, 15);
        imm = Word.binaryToHex(w, 16, 31);


        boolean[] functionArr = new boolean[6];
        for (int i=0 ;i<6; i++) {
            functionArr[i] = this.bits[i];
        }
        op = OP.matchOp(functionArr, false);
    }

    public String toString() {
        // EX:  ori {opcode: 0d, rs(base): 07, rt: 17, immediate(offset): ff84}
        return op.name + " {opcode: " + opCodeString + ", rs(base): " + rs + ", rt: " + rt + ", immediate(offset): " + imm + "}";
    }
}
