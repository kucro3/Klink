package org.kucro3.klink.syntax;

import org.kucro3.klink.Klink;
import org.kucro3.klink.Variables.Var;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;

public class Operation implements Executable {
	public Operation(ExpressionLibrary lib, String exp, Var[] vars, Sequence seq, Flow codeBlock)
	{
		try {
			this.exp = lib.requireExpression(exp);
			this.seq = seq;
			this.instance = this.exp.compile(lib, vars, seq, codeBlock);
		} catch (ScriptException e) {
			if(this instanceof Lined)
				e.addLineInfo(((Lined) e).line());
			throw e;
		}
	}
	
	public void execute(Klink sys)
	{
		try {
			instance.call(sys, sys.currentEnv());
		} catch (ScriptException e) {
			if(this instanceof Lined)
				e.addLineInfo(((Lined) e).line());
			throw e;
		}
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