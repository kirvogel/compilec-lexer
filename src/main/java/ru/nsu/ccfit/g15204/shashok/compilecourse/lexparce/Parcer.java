package ru.nsu.ccfit.g15204.shashok.compilecourse.lexparce;

import java.io.Reader;
import java.util.LinkedList;

/**
 * Created by kirafogel on 12.02.18.
 */
public class Parcer {

    private Lexer lexer;
    private Lexem current;
    private boolean end = false;

    public boolean isEnd () {
        return end;
    }

    LinkedList<Lexem> list = new LinkedList<Lexem>();

    /**
     * Constructor.
     * @param reader        reader from where to read symbols.
     * @param test          enables terminal mode if true.
     */
    public Parcer (Reader reader, boolean test) throws Exception {
        lexer = new Lexer(reader);
        if (test) {
            test();
        }
        //newLine();
    }

    /**
     * Constructor which always set Parcer to file mode.
     * @param reader        reader from where to read symbols.
     */
    public Parcer (Reader reader) throws Exception {
        lexer = new Lexer(reader);
        //newLine();
    }

    /**
     * Enables terminal mode for Lexer.
     */
    private void test() {
        lexer.testMode();
    }

    /**
     * Reads Lexems from Lexer untill EOF of FEOF met. Call it before calling parceExpression().
     */
    public void newLine() throws Exception {
        Lexem lexem;
        do {
            lexem = lexer.getNextLexem();
            if (lexem.getType() != LexemType.EOF && lexem.getType() != LexemType.FEOF) {
                list.add(lexem);
            }
        } while (lexem.getType() != LexemType.EOF && lexem.getType() != LexemType.FEOF);
        if (lexem.getType() == LexemType.FEOF && list.size() == 0) {
            list.add(lexem);
        }
    }

    /**
     * Gets Lexem from list.
     * @return      next lexem from current line.
     */
    private Lexem getNextLexem() throws Exception {
        if (list.size() != 0) {
            Lexem l = list.remove(0);
            return l;
        } else {
            return null;
        }
    }

    /**
     * Parses current line of lexems from list. Please call it after newLine().
     * All next function do the same, but from different levels of algebraic layers.
     * expr -> term +- term +-...
     * term -> factor * / factor * / ...
     * factor -> power ^ factor | power
     * power -> -atom | atom
     * atom -> (expr) | number | (-expr)
     * @return      resulting value
     */

    public double ParceExpression () throws Exception {
        if (list.size() == 0) return 0;
        double temp = ParceTerm();
        while (current != null && ( current.getType() == LexemType.PLUS || current.getType() == LexemType.MINUS)) {
            if (current.getType() == LexemType.PLUS) {
                current = getNextLexem();
                temp += ParceTerm();
            } else {
                current = getNextLexem();
                temp -= ParceTerm();
            }
        }
        return temp;
    }

    private double ParceTerm () throws Exception {
        double temp = ParceFact();
        while (current != null && (current.getType() == LexemType.MUL || current.getType() == LexemType.DIV)) {
            if (current.getType() == LexemType.MUL) {
                current = getNextLexem();
                temp *= ParceFact();
            } else {
                current = getNextLexem();
                temp /= ParceFact();
            }
        }
        return temp;
    }

    private double ParceFact () throws Exception {
        double temp = ParcePower();
        while (current != null && (current.getType() == LexemType.POW)) {
            current = getNextLexem();
            if (current == null) {
                throw new Exception("Sudden end of the line");
            }
            temp = Math.pow(temp, ParceFact());
        }
        return temp;
    }

    private double ParcePower () throws Exception {
        double temp = 1;
        if (current == null) {
            current = getNextLexem();
        }
        if (current == null) {
            throw new Exception("Sudden end of the line");
        }
        if (current.getType() == LexemType.FEOF) {
            end = true;
            return 0;
        }
        boolean min = false;
        if (current.getType() == LexemType.MINUS) {
            current = getNextLexem();
            min = true;
            if (current == null) {
                throw new Exception("Sudden end of the line");
            }
        }
        temp = ParceAtom();
        if (min) {
            temp = 0 - temp;
        }
        return temp;
    }

    private double ParceAtom () throws Exception {
        double temp = 0;
        boolean minus = false;
        if (current.getType() != LexemType.OBR && current.getType() != LexemType.NUM) {
            throw new Exception("Sudden end of the line");
        }
        if (current != null && current.getType() == LexemType.NUM) {
            temp = Integer.parseInt(current.getValue());
            current = getNextLexem();
            return temp;
        }
        while (current != null && (current.getType() == LexemType.OBR)) {
            current = getNextLexem();
            if (current != null && current.getType() == LexemType.MINUS) {
                current = getNextLexem();
                minus = true;
            }
            temp = ParceExpression();
            if (minus) {
                temp = 0 - temp;
            }
            if (current != null && current.getType() == LexemType.CBR) {
                current = getNextLexem();
            } else {
                throw new Exception ("Can't find closing bracket");
            }
        }
        return temp;
    }

    public void closeReader () throws Exception {
        lexer.closeReader();
    }

}
