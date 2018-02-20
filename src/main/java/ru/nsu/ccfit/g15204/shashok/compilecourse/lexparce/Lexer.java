package ru.nsu.ccfit.g15204.shashok.compilecourse.lexparce;

import java.io.Reader;
public class Lexer {
    private Reader reader;
    private int current = ' ';

    private String mode = "textmode";
    /**
     * Constructor of Lexer, which can divide one string, ending in \n or EOF, to algebraic lexems.
     * @param reader        Reader to read symbols from.
     */
    public Lexer (Reader reader) throws Exception {
        this.reader = reader;
        while (current == ' ') {
            current = this.reader.read();
            //System.out.println(current);
        }
    }

    /**
     * Sets Lexer to terminal mode. Please, use that first if you're
     * using System.in as descriptor with your Reader,
     * otherwise the results are not the ones you could expect.
     */
    public void testMode() {
        mode = "termode";
    }

    /**
     * Gets next Lexem from Reader, symbol by symbol.
     * @return              returns Lexem of some LexemType.
     */
    public Lexem getNextLexem () throws Exception {
        StringBuilder b = new StringBuilder();
        if (current == -1) {
            return new Lexem(LexemType.FEOF, "");
        }
        while (current == ' ' || current == '\n') {
            Lexem toRet = null;
            if (current == '\n') {
                toRet = new Lexem(LexemType.EOF, "\n");
                if (mode.equals("termode")) {
                    current = ' ';
                    return toRet;
                } else {
                    while (current == ' ' || current == '\n') {
                        current = this.reader.read();
                    }
                    return toRet;
                }
            }
            current = this.reader.read();
        }

        boolean numread = false;
        boolean done = false;
        LexemType type = null;
        while (true) {
            switch (current) {

                case (' '):
                case ('\n'):
                case (-1):
                    done = true;
                    break;
                case ('0'):
                case ('1'):
                case ('2'):
                case ('3'):
                case ('4'):
                case ('5'):
                case ('6'):
                case ('7'):
                case ('8'):
                case ('9'):
                    numread = true;
                    type = LexemType.NUM;
                    b.append((char) current);
                    break;
                case ('('):
                    if (type == null)
                        type = LexemType.OBR;
                case (')'):
                    if (type == null) {
                        type = LexemType.CBR;
                    }
                case ('*'):
                    if (type == null)
                        type = LexemType.MUL;
                case ('/'):
                    if (type == null)
                        type = LexemType.DIV;
                case ('^'):
                    if (type == null)
                        type = LexemType.POW;
                case ('+'):
                    if (type == null)
                        type = LexemType.PLUS;
                case ('-'):
                    if (type == null)
                        type = LexemType.MINUS;

                    done = true;
                    if (numread) {
                        break;
                    }
                    b.append((char) current);
                    break;
                default:
                    throw new Exception("Unsupported lexem! Read: " + (char) current + " " + current);
            }
            if ((current != -1 && current != '\n')) {
                if (!done) {
                    current = this.reader.read();
                }
                if (!numread) {
                    current = this.reader.read();
                }
            }
            if (done) {
                String s = b.toString();
                return new Lexem(type, s);
            }
        }
    }

    /**
     * Closes reader. Please call it after you're done with reading from Reader.
     */
    public void closeReader () throws Exception {
        reader.close();
    }
}
