package org.dementhium.util;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * @author 'Mystic Flow
 */
public final class ServerLogger extends PrintStream {

    public ServerLogger(OutputStream out) {
        super(out);
    }
    
    @Override
    public void print(String message) {
        Throwable throwable = new Throwable();
        String name = throwable.getStackTrace()[2].getFileName().replace(".java", "");
        log(name, message);
    }

    @Override
    public void print(boolean message) {
        Throwable throwable = new Throwable();
        String name = throwable.getStackTrace()[2].getFileName().replace(".java", "");
        log(name, message);
    }

    @Override
    public void print(int message) {
        Throwable throwable = new Throwable();
        String name = throwable.getStackTrace()[2].getFileName().replace(".java", "");
        log(name, message);
    }

    public void log(String classnam, Object message) {
    	super.print("[" + classnam + "] " + message);
    }
}
