package Words;

import MIPSSyntax.OP;
import MIPSSyntax.memoryMap;

public class JWord extends Word {
    //      32-26               25-0
    //   Opcode (000010)     instr_index

    private final String instr_index;
    private final Integer off;

    public JWord(boolean[] w) {
        super(w);
        instr_index = Word.binaryToHex(w, 6, 31);
        off = Word.binaryToDecimal(w, 6, 31, true);
    }

    public String toString() {
        return "j {opcode: 02, index: " + instr_index + "}";
    }

    @Override
    public void execute(memoryMap mem) {
        OP.j.apply(null,null,null,off,mem);
    }
}
