package org.kucro3.klink;

import java.io.InputStream;
import java.io.PrintStream;

public class SystemSource implements Source {
	@Override
	public PrintStream getErr() 
	{
		return System.err;
	}

	@Override
	public PrintStream getOut() 
	{
		return System.out;
	}

	@Override
	public InputStream getIn() 
	{
		return System.in;
	}
}