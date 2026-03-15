package gregtech.api.util;

import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * Just a simple Logging Function. If on Server, then this will point to System.out and System.err
 */
public class GTLog {

    public static PrintStream out = System.out;
    public static PrintStream err = System.err;
    public static PrintStream ore = new LogBuffer();
    public static PrintStream exp = new LogBuffer();
    public static PrintStream ico = new LogBuffer();
    public static File mLogFile;
    public static File mOreDictLogFile;
    public static File mExplosionLog;
    public static File mRegisterIconsLog;

    public static class LogBuffer extends PrintStream {

        public final List<String> lineBuffer = new ArrayList<>();

        public LogBuffer() {
            super(new OutputStream() {

                @Override
                public void write(int arg0) {
                    /* Do nothing */
                }
            });
        }

        @Override
        public void println(String aString) {
            lineBuffer.add(aString);
        }
    }
}
