package Words;

import MIPSSyntax.OP;
import MIPSSyntax.REG;
import MIPSSyntax.memoryMap;

public class IWord extends Word {
    //      32-26               25-21           20-16           15-0
    //      Opcode               rs               rt             imm

    private final String rs,rt,imm,opCodeString;
    private final OP op;
    private final REG s, t;
    private final Integer off;

    public IWord(boolean[] w) {
        super(w);

        opCodeString = Word.binaryToHex(w, 0, 5);
        rs = Word.binaryToHex(w, 6, 10);
        s = REG.matchValue(Word.binaryToDecimal(w, 6, 10, false));
        rt = Word.binaryToHex(w, 11, 15);
        t = REG.matchValue(Word.binaryToDecimal(w, 11, 15, false));
        imm = Word.binaryToHex(w, 16, 31);
        off = Word.binaryToDecimal(w, 16, 31, true);


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

    @Override
    public void execute(memoryMap mem) {
        op.apply(t,s,null,off,mem);
    }
}
