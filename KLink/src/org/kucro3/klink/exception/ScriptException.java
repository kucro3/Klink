package org.kucro3.klink.exception;

public class ScriptException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2934281618950012808L;
	
	public ScriptException(String message)
	{
		this.message = message;
	}
	
	public ScriptException(String message, Throwable cause)
	{
		this.message = message;
		super.initCause(cause);
	}
	
	public void addNameInfo(String name)
	{
		if(this.nameInfo)
			return;
		
		this.setMessage(new StringBuilder()
				.append("[")
				.append(name)
				.append("] ")
				.append(this.getMessage())
				.toString());
		this.nameInfo = true;
	}
	
	public void addLineInfo(int line)
	{
		if(this.lineInfo)
			return;
		
		this.setMessage(new StringBuilder()
				.append("[")
				.append("At line ")
				.append(line)
				.append("] ")
				.append(this.getMessage())
				.toString());
		this.lineInfo = true;
	}
	
	public void setMessage(String message)
	{
		this.message = message;
	}
	
	@Override
	public String getMessage()
	{
		return message;
	}
	
	protected String message;
	
	private boolean lineInfo;
	
	private boolean nameInfo;
}