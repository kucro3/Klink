package org.kucro3.klink.expression;

import org.kucro3.klink.Variables.Var;
import org.kucro3.klink.syntax.Flow;
import org.kucro3.klink.syntax.Sequence;

public class Expression implements ExpressionCompiler {
	public Expression(String name, ExpressionCompiler compiler)
	{
		this.name = name;
		this.compiler = compiler;
	}
	
	public String getName()
	{
		return name;
	}
	
	public ExpressionCompiler getCompiler()
	{
		return compiler;
	}
	
	public void destroy()
	{
		this.compiler = null;
	}
	
	@Override
	public ExpressionInstance compile(Var[] var, Sequence seq, Flow codeBlock)
	{
		return compiler.compile(var, seq, codeBlock);
	}
	
	private ExpressionCompiler compiler;
	
	private final String name;
}