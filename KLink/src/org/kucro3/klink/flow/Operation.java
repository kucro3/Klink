package org.kucro3.klink.flow;

import org.kucro3.klink.Executable;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Ref;
import org.kucro3.klink.Snapshot;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.syntax.Sequence;

public class Operation implements Executable {
	public Operation(ExpressionLibrary lib, String exp, Ref[] refs, Sequence seq, Flow codeBlock, Snapshot snapshot)
	{
		this.exp = lib.requireExpression(exp);
		this.seq = seq;
		this.instance = this.exp.compile(lib, refs, seq, codeBlock, snapshot);
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