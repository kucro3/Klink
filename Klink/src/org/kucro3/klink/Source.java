package org.kucro3.klink;

import java.io.InputStream;
import java.io.PrintStream;

public interface Source {
	public PrintStream getErr();
	
	public PrintStream getOut();
	
	public InputStream getIn();
}