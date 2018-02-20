import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import ru.nsu.ccfit.g15204.shashok.compilecourse.lexparce.Parcer;

/**
 * Created by kirafogel on 13.02.18.
 */
public class TestParcer {

    @Test
    public void file () throws Exception {

        Charset inputCharset = Charset.forName("ISO-8859-1");
        BufferedReader r = new BufferedReader(
                new InputStreamReader(
                        new FileInputStream("src/test/test.txt"),
                        inputCharset
                ));
        BufferedReader reader =  new BufferedReader( new InputStreamReader(
                new FileInputStream("src/test/answers.txt"),
                inputCharset
        ));
        Parcer p = new Parcer(r);

        while (!p.isEnd()) {
            p.newLine();
            double d = Double.parseDouble(reader.readLine());
            double d2 = p.ParceExpression();
            System.out.println(d + " ?= " + d2);
            Assert.assertEquals(d, d2, 0.01);
        }
        p.closeReader();
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void cbrmiss() throws Exception {
        thrown.expect(Exception.class);
        thrown.expectMessage("Can't find closing bracket");

        Charset inputCharset = Charset.forName("ISO-8859-1");

        try (
            BufferedReader r = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream("src/test/exc.txt"),
                            inputCharset
                    ));
        ) {
            Parcer pp = new Parcer(r);
            pp.newLine();
            pp.ParceExpression();
        } catch (Exception e) {
            throw e;
        }
    }
}
