package org.kucro3.klink;

import org.kucro3.klink.exception.ScriptException;

public class Environment {
	public Environment(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Source getSource()
	{
		return source;
	}
	
	public void setSource(Source source)
	{
		this.source = source;
	}
	
	public Variables getVars()
	{
		return varRoot;
	}
	
	public void setVars(Variables vars)
	{
		this.varRoot = vars;
	}
	
	public Variables pushVars()
	{
		Variables child = new Variables(varRoot);
		this.varRoot = child;
		return child;
	}
	
	public Variables popVars()
	{
		if(varRoot.getParent() == null)
			throw VarsStackUnderflow();
		this.varRoot = varRoot.getParent();
		return this.varRoot;
	}
	
	public static ScriptException VarsStackUnderflow()
	{
		return new ScriptException("Variable container stack underflow");
	}
	
	private Variables varRoot = new Variables();
	
	private Source source;
	
	private final String name;
}