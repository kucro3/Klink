package org.kucro3.klink.expression;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Variables.Variable;
import org.kucro3.klink.syntax.Sequence;

public class Expression implements ExpressionInvoker {
	public Expression(String name, ExpressionInvoker invoker)
	{
		this.name = name;
		this.invoker = invoker;
	}
	
	public String getName()
	{
		return name;
	}
	
	public ExpressionInvoker getInvoker()
	{
		return invoker;
	}
	
	public void destroy()
	{
		this.invoker = null;
	}
	
	@Override
	public Object call(Environment env, Variable var, Sequence seq)
	{
		return invoker.call(env, var, seq);
	}
	
	private ExpressionInvoker invoker;
	
	private final String name;
}