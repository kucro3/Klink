package org.kucro3.klink.identifiers;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Variables.Variable;
import org.kucro3.klink.syntax.Sequence;

public class Identifier implements IdentifierInvoker {
	public Identifier(String name, IdentifierInvoker invoker)
	{
		this.name = name;
		this.invoker = invoker;
		this.available = true;
	}
	
	public boolean available()
	{
		return available;
	}
	
	public String getName()
	{
		return name;
	}
	
	public IdentifierInvoker getInvoker()
	{
		return invoker;
	}
	
	public void destroy()
	{
		this.available = false;
		this.invoker = null;
	}
	
	@Override
	public Object call(Environment env, Variable var, Sequence seq)
	{
		return invoker.call(env, var, seq);
	}
	
	private boolean available;
	
	private IdentifierInvoker invoker;
	
	private final String name;
}