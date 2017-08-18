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
}