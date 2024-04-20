package Words;

public class JWord extends Word {
    //      32-26               25-0
    //   Opcode (000010)     instr_index

    private final String instr_index;

    public JWord(boolean[] w) {
        super(w);
        instr_index = Word.binaryToHex(w, 6, 31);
    }

    public String toString() {
        return "j {opcode: 02, index: " + instr_index + "}";
    }

}
