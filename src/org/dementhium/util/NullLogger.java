package org.dementhium.util;

import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Used when running without debug, this disables printing of messages.
 * @author Emperor
 *
 */
public class NullLogger extends PrintStream {

	/**
	 * Constructs a new {@code NullLogger} {@code Object}.
	 * @param out The output stream.
	 */
	public NullLogger(OutputStream out) {
		super(out);
	}

	@Override
	public void println(String s) {
    	/*
    	 * empty
    	 */
	}

	@Override
	public void println(Object o) {
    	/*
    	 * empty
    	 */
	}

	@Override
	public void println(int i) {
    	/*
    	 * empty
    	 */
	}

	@Override
	public void println(boolean b) {
    	/*
    	 * empty
    	 */
	}

	@Override
	public void println(double d) {
    	/*
    	 * empty
    	 */
	}
	
	@Override
    public void print(String message) {
    	/*
    	 * empty
    	 */
    }

    @Override
    public void print(boolean message) {
    	/*
    	 * empty
    	 */
    }

    @Override
    public void print(int message) {
    	/*
    	 * empty
    	 */
    }
}
