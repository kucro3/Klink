package org.kucro3.klink.expression;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Variables.Variable;
import org.kucro3.klink.syntax.Flow;
import org.kucro3.klink.syntax.Sequence;

public class Expression implements ExpressionInvoker {
	public Expression(String name, ExpressionInvoker invoker, ReturnType rt)
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
	
	public ReturnType getReturnType()
	{
		return rt;
	}
	
	@Override
	public Object call(Klink sys, Environment env, Variable[] var, Sequence seq, Flow codeBlock)
	{
		try {
			Object ret = invoker.call(sys, env, var, seq, codeBlock);
			switch(rt)
			{
			case BOOLEAN:
				env.setBooleanSlot((boolean) ret);
				break;
				
			case VARIABLE:
				env.setReturnSlot(ret);
				break;
				
			case VOID:
				break;
			}
			return ret;
		} finally {
			seq.reset();
		}
	}
	
	private ExpressionInvoker invoker;
	
	private ReturnType rt;
	
	private final String name;
	
	public static enum ReturnType
	{
		BOOLEAN,
		VARIABLE,
		VOID
	}
}