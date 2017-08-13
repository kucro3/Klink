package org.kucro3.klink.exception;

public class ScriptException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2934281618950012808L;
	
	public ScriptException(String name)
	{
		super(name);
	}
	
	public static ScriptException VarNotCompatible()
	{
		throw new ScriptException("Variable not compatible");
	}
	
	public static ScriptException VarNotCompatible(int requiredCount, int currentCount)
	{
		throw new ScriptException("Variable not compatible: " + currentCount + " provided, " + requiredCount + " required");
	}
}