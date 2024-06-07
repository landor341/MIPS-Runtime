package Words;

import MIPSSyntax.memoryMap;
import MIPSSyntax.OP;


public class nopWord extends Word {

    public nopWord() {

    }

    @Override
    public void execute(memoryMap mem) {
        OP.nop.apply(null, null, null, null, null);
    }
}
