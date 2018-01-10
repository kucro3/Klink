package org.kucro3.klink.expression;

import org.kucro3.klink.Ref;
import org.kucro3.klink.Snapshot;
import org.kucro3.klink.flow.Flow;
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
	public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock, Snapshot snapshot)
	{
		return compiler.compile(lib, refs, seq, codeBlock, snapshot);
	}
	
	private ExpressionCompiler compiler;
	
	private final String name;
}