package org.kucro3.klink.syntax;

import org.kucro3.klink.Klink;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionInstance;

public class Operation implements Executable {
	public Operation(Expression exp, Sequence seq, Flow codeBlock)
	{
		this.exp = exp;
		this.seq = seq;
		this.instance = exp.compile(null, seq, codeBlock);
	}
	
	public void execute(Klink sys)
	{
		instance.call(sys, sys.currentEnv());
	}
	
	public Expression getExpression()
	{
		return exp;
	}
	
	public Sequence getSequence()
	{
		return seq;
	}
	
	private final ExpressionInstance instance;
	
	private final Expression exp;
	
	private final Sequence seq;
}