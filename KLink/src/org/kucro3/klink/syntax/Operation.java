package org.kucro3.klink.syntax;

import org.kucro3.klink.Klink;
import org.kucro3.klink.expression.Expression;

public class Operation implements Executable {
	public Operation(Expression exp, Sequence seq)
	{
		this.exp = exp;
		this.seq = seq;
	}
	
	public void execute(Klink sys)
	{
		exp.call(sys.currentEnv(), sys.currentEnv().popVarSlot(), seq);
	}
	
	public Expression getExpression()
	{
		return exp;
	}
	
	public Sequence getSequence()
	{
		return seq;
	}
	
	private final Expression exp;
	
	private final Sequence seq;
}