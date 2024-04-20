package Words;

public class SyscallWord extends Word {
    //      32-26           25-6             5-0
    //   Opcode of 0    CODE (Empty)   SYSCALL (001100)
    private final String codeAsHex;

    public SyscallWord(boolean[] w) {
        super(w);
        codeAsHex = Word.binaryToHex(w, 6, 25);
    }

    public String toString() {
        return "syscall {opcode: 00, code: " + codeAsHex + ", funct: 0c}";
    }
}
