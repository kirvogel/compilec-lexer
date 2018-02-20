package ru.nsu.ccfit.g15204.shashok.compilecourse.lexparce;

import java.io.*;
import java.nio.charset.Charset;

/**
 * Created by kirafogel on 12.02.18.
 */
public class Main {
    /**
     * Reads from console. Type some unsupported symbol to end program
     * via Exception or just send SIGKILL.
     * @param args          command line arguments, aren't used.
     */
    public static void main (String... args) throws Exception {
        Charset inputCharset = Charset.forName("ISO-8859-1");
        BufferedReader r = new BufferedReader(
                new InputStreamReader(
                        System.in,
                        inputCharset
                ));
        Parcer p = new Parcer(r, true);
        while (!p.isEnd()) {
            p.newLine();
            System.out.println(p.ParceExpression());
        }
    }
}
