package MIPSSyntax;

import Words.Word;
import MIPSSyntax.memoryMap;
import config.config;

import java.util.Objects;
import java.util.Scanner;
import java.util.function.BiFunction;

// ENUMS for each supported operation. The opcode for R types if their funct code
public enum OP {
    add(    "add",    new boolean[]{true, false, false, false, false, false}, ITYPE.R, (a,b,c,d,e)  -> { return a.storedVal = b.storedVal + c.storedVal; }), // 100000 TODO: Handle overflow
    addu(   "addu",   new boolean[]{true, false, false, false, false, true},  ITYPE.R, (a,b,c,d,e)  -> { return a.storedVal = b.storedVal + c.storedVal; }), // 100000
    addi(   "addi",   new boolean[]{false, false, true, false, false, false}, ITYPE.I, (a,b,c,d,e)  -> { return a.storedVal = b.storedVal + d; }), // 001000 TODO: Handle overflow
    addiu(  "addiu",  new boolean[]{false, false, true, false, false, true},  ITYPE.I, (a,b,c,d,e)  -> { return a.storedVal = b.storedVal + d; }), // 001000
    and(    "and",    new boolean[]{true, false, false, true, false, false},  ITYPE.R, (a,b,c,d,e)  -> { return a.storedVal = b.storedVal & c.storedVal; }), // 100100
    andi(   "andi",   new boolean[]{false, false, true, true, false ,false},  ITYPE.I, (a,b,c,d,e)  -> { return a.storedVal = b.storedVal & d; }), // 001100
    j(      "j",      new boolean[]{false, false, false, false, true, false}, ITYPE.J, (a,b,c,d,e)  -> { return REG.PC.storedVal = (REG.PC.storedVal & 0xF0000000) + d*4-1; }), // 000010
    beq(    "beq",    new boolean[]{false, false, false, true, false, false}, ITYPE.I, (a,b,c,d,e)  -> { return a.storedVal == b.storedVal ? (REG.PC.storedVal += 4*d) : 0; }), // 000100
    bne(    "bne",    new boolean[]{false, false, false, true, false, true},  ITYPE.I, (a,b,c,d,e)  -> { return a.storedVal != b.storedVal ? (REG.PC.storedVal += 4*d) : 0; }), // 000101
    lw(     "lw",     new boolean[]{true, false, false, false, true, true},   ITYPE.I, (a,b,c,d,e)  -> { return a.storedVal = e.getWord(d).toInt(); }), // 100011
    lui(    "lui",    new boolean[]{false, false, true, true, true, true},    ITYPE.I, (a,b,c,d,e)  -> { return a.storedVal = (int) (d*Math.pow(2,16)); }), // 001111
    or(     "or",     new boolean[]{true, false, false, true, false, true},   ITYPE.R, (a,b,c,d,e)  -> { return a.storedVal = b.storedVal | c.storedVal; }), // 100101
    ori(    "ori",    new boolean[]{false, false, true, true, false, true},   ITYPE.I, (a,b,c,d,e)  -> { return a.storedVal = b.storedVal | d; }), // 001101
    slt(    "slt",    new boolean[]{true, false, true, false, true, false},   ITYPE.R, (a,b,c,d,e)  -> { return a.storedVal = b.storedVal < c.storedVal ? 1 : 0; }), // 101010
    slti(   "slti",   new boolean[]{false, false, true, false, true, false},  ITYPE.I, (a,b,c,d,e)  -> { return a.storedVal = b.storedVal < d ? 1 : 0; }), // 001010
    sub(    "sub",    new boolean[]{true, false, false, false, true, false},  ITYPE.R, (a,b,c,d,e)  -> { return a.storedVal = b.storedVal - c.storedVal; }), // 100010 TODO: Overflow
    sw(     "sw",     new boolean[]{true, false, true, false, true, true},    ITYPE.I, (a,b,c,d,e)  -> { return e.storeWord(d, new Word(a.storedVal)); }), // 101011
    syscall("syscall",new boolean[]{false, false, true, true, false, false},  ITYPE.SysCall, (a,b,c,d,e)  -> { return applySyscall(e); }),
    nop(    "nop",    new boolean[]{false, false, false, false, false, false, false}, null, (a,b,c,d,e)  -> { return null; }),
    debug(  "debug",  new boolean[]{true, true, true, true, true, true, true}, null, (a,b,c,d,e) -> { return null; });

    public final String name;
    public final boolean[] opcode;
    public final ITYPE type;

    private mips.PentaFunction<REG,REG,REG,Integer,memoryMap,Integer> operation;

    OP(String name, boolean[] opcode, ITYPE type, mips.PentaFunction<REG, REG ,REG, Integer, memoryMap, Integer> operation) {
        this.name = name;
        this.opcode = opcode;
        this.type = type;
        this.operation = operation;
    }

    /**
     *
     * @param a destination register
     * @param b source register 1
     * @param c source register 2
     * @param d immediate value
     * @param e memory map
     * @return
     */
    public Integer apply(REG a, REG b, REG c, Integer d, memoryMap e) {
        return this.operation.apply(a,b,c,d,e);
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



    private static int applySyscall(memoryMap mem) {
        if (config.verbose) System.out.println("\n[a0 value: " + REG.$a0.storedVal + "]     [PC: " + REG.PC.storedVal + "]    [Data Start: " + 0x10010000 + "]");

        if (REG.$v0.storedVal == 1) { // Print out an integer value
            System.out.print(REG.$a0.storedVal);
        } else if (REG.$v0.storedVal == 2) { // Print out a float value
            System.out.print((float) REG.$a0.storedVal);
        } else if (REG.$v0.storedVal == 3) { // Print out a double value
            System.out.print((double) REG.$a0.storedVal);
        } else if (REG.$v0.storedVal == 4) { // Print out a null terminated string
            while (Word.binaryToDecimal(mem.getByte(REG.$a0.storedVal),false) != (int) '\0') {
                System.out.print((char) Word.binaryToDecimal(mem.getByte(REG.$a0.storedVal),false));
                REG.$a0.storedVal++;
            }
        } else if (REG.$v0.storedVal == 5) { // read in an integer
            try {
                Scanner s = new Scanner(System.in);
                REG.$v0.storedVal = s.nextInt();
                s.close();
            } catch (Exception e) {
                if (config.verbose) System.out.println("ERROR GETTING USER INPUT WHEN $v0==5");
                REG.$v0.storedVal = 0;
            }
        } else if (REG.$v0.storedVal == 6) { // read in a float value

        } else if (REG.$v0.storedVal == 7) { // read in a double value

        } else if (REG.$v0.storedVal == 8) { // read in a string

        } else if (REG.$v0.storedVal == 9) { // allocate heap memory

        } else if (REG.$v0.storedVal == 10) { // exit

        } else if (REG.$v0.storedVal == 11) { // print character
            System.out.print((char) REG.$a0.storedVal);
        } else if (REG.$v0.storedVal == 12) { // read in a character

        } else if (REG.$v0.storedVal == 13) { // open file

        } else if (REG.$v0.storedVal == 14) { // read from file

        } else if (REG.$v0.storedVal == 15) { // write to file

        } else if (REG.$v0.storedVal == 16) { // close file

        } else if (REG.$v0.storedVal == 17) { // exit 2 (terminate with value)

        }
        if (config.verbose) System.out.println();
        return 1;
    }

}

