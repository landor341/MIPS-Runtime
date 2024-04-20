package MIPSSyntax;

import Words.Word;

// ENUMS for each supported operation. The opcode for R types if their funct code
public enum OP {
    add(    "add",    new boolean[]{true, false, false, false, false, false}, ITYPE.R), // 100000
    addu(   "addu",   new boolean[]{true, false, false, false, false, true},  ITYPE.R), // 100000
    addi(   "addi",   new boolean[]{false, false, true, false, false, false}, ITYPE.I), // 001000
    addiu(  "addiu",  new boolean[]{false, false, true, false, false, true},  ITYPE.I), // 001000
    and(    "and",    new boolean[]{true, false, false, true, false, false},  ITYPE.R), // 100100
    andi(   "andi",   new boolean[]{false, false, true, true, false ,false},  ITYPE.I), // 001100
    beq(    "beq",    new boolean[]{false, false, false, true, false, false}, ITYPE.I), // 000100
    bne(    "bne",    new boolean[]{false, false, false, true, false, true},  ITYPE.I), // 000101
    lw(     "lw",     new boolean[]{true, false, false, false, true, true},   ITYPE.I), // 100011
    lui(    "lui",    new boolean[]{false, false, true, true, true, true},    ITYPE.I), // 001111
    j(      "j",      new boolean[]{false, false, false, false, true, false}, ITYPE.J), // 000010
    or(     "or",     new boolean[]{true, false, false, true, false, true},   ITYPE.R), // 100101
    ori(    "ori",    new boolean[]{false, false, true, true, false, true},   ITYPE.I), // 001101
    slt(    "slt",    new boolean[]{true, false, true, false, true, false},   ITYPE.R), // 101010
    slti(   "slti",   new boolean[]{false, false, true, false, true, false},  ITYPE.I), // 001010
    sub(    "sub",    new boolean[]{true, false, false, false, true, false},  ITYPE.R), // 100010
    sw(     "sw",     new boolean[]{true, false, true, false, true, true},    ITYPE.I), // 101011
    syscall("syscall",new boolean[]{false, false, true, true, false, false},  ITYPE.SysCall),
    nop(    "nop",    new boolean[]{false, false, false, false, false, false, false}, null),
    debug(  "debug",  new boolean[]{true, true, true, true, true, true, true}, null);

    public final String name;
    public final boolean[] opcode;
    public final ITYPE type;

    OP(String name, boolean[] opcode, ITYPE type) {
        this.name = name;
        this.opcode = opcode;
        this.type = type;
    }

    public static OP matchOp(boolean[] opcode, boolean isRType) {
        for (OP o : OP.values())
            if (isRType && o.type == ITYPE.R && arrEquals(opcode, o.opcode))
                return o;
            else if (!isRType && o.type != ITYPE.R && arrEquals(opcode, o.opcode))
                return o;
        return nop;
    }

    private static boolean arrEquals(boolean[] a, boolean[] b) {
        if (a.length != b.length) return false;
        for (int i=0; i<a.length; i++)
            if (a[i] != b[i]) return false;
        return true;
    }

}

