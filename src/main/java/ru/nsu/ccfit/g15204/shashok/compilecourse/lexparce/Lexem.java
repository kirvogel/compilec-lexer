package ru.nsu.ccfit.g15204.shashok.compilecourse.lexparce;

public class Lexem {
    private LexemType type;
    private String value;

    /**
     * Constructor of Lexem.
     * @param type  type of the lexem
     * @param value String lexem
     */
    public Lexem (LexemType type, String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Returns LexemType pf the current lexem.
     * @return      type of the lexem
     */
    public LexemType getType () {
        return this.type;
    }

    /**
     * Getting value of lexem.
     * @return      String value of the lexem
     */
    public String getValue() {
        return this.value;
    }

    /**
     * Comparator method.
     * @param obj   object to compare Lexem with
     * @return      true if Lexem is equal to obj, false if not.
     */
    public boolean equals(Object obj) {
        return obj instanceof Lexem
                && ((Lexem) obj).type.equals(this.type)
                && ((Lexem) obj).value.equals(this.value);
    }
}
