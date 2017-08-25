package org.kucro3.klink.exception;

public class Interruption extends ScriptException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7316399853777106717L;
	
	public Interruption(int code)
	{
		super("Interruption code: " + code);
		this.code = code;
	}
	
	public int code()
	{
		return code;
	}
	
	private final int code;
}