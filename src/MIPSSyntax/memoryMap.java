package MIPSSyntax;

import Words.Word;

import java.util.HashMap;

public class memoryMap {

    HashMap<Integer, Word> map = new HashMap<Integer, Word>();


    /**
     *
     * @param loc memory location of word you want to load
     * @return The word stored at loc, or null if there is no associated word.
     */
    public Word getWord(int loc) {
        return map.get(loc-loc%4);
    }

    public boolean[] getByte(int loc) { return this.getWord(loc).getByte(loc % 4); }

    public int storeWord(int loc, Word w) {
        if (loc%4 != 0) throw new IllegalArgumentException();
        map.put(loc, w);
        return 1;
    }

    public int storeByte(int loc, boolean[] w) {
        if (!map.containsKey(loc-loc%4))
            map.put(loc-loc%4, new Word());
        map.get(loc).setByte(w, loc%4);
        return 1;
    }
}
