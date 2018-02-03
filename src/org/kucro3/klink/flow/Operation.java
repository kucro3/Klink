package org.kucro3.klink.flow;

import org.kucro3.klink.*;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.syntax.Sequence;

public class Operation implements Executable {
	public Operation(ExpressionLibrary lib, String name, Ref[] refs, Sequence seq, Flow codeBlock, Snapshot snapshot, CompileMode compileMode)
	{
		this.compileMode = compileMode;
		this.seq = seq;

		this.lib = lib;
		this.name = name;
		this.refs = refs;
		this.codeBlock = codeBlock;
		this.snapshot = snapshot;

		if(beforeExecute())
			compile(false);
	}

	private void compile(boolean dynamic)
	{
		if(!dynamic && compiled)
			throw AlreadyCompiled();

		this.exp = lib.requireExpression(name);
		this.instance = this.exp.compile(lib, refs, seq, codeBlock, snapshot);

		if(!dynamic)
		{
			this.lib = null;
			this.name = null;
			this.refs = null;
			this.codeBlock = null;
			this.snapshot = null;
		}

		compiled = true;
	}

	private void dynamicCompile()
	{
		compile(true);
	}

	private void lazyCompile()
	{
		if(!compiled)
			compile(false);
	}

	private void stub()
	{
		if(firstExecute())
			lazyCompile();
		else if(dynamic())
			dynamicCompile();
	}
	
	public void execute(Klink sys, Environment env)
	{
		stub();
		instance.call(sys, env);
	}
	
	public Expression getExpression()
	{
		stub();
		return exp;
	}

	private boolean dynamic()
	{
		return compileMode.equals(CompileMode.DYNAMIC);
	}

	private boolean beforeExecute()
	{
		return compileMode.equals(CompileMode.BEFORE_EXECUTE);
	}

	private boolean firstExecute()
	{
		return compileMode.equals(CompileMode.FIRST_EXECUTE);
	}
	
	public Sequence getSequence()
	{
		return seq;
	}
	
	private ExpressionInstance instance;
	
	private Expression exp;
	
	private final Sequence seq;

	private final CompileMode compileMode;

	private ExpressionLibrary lib;

	private Ref[] refs;

	private String name;

	private Flow codeBlock;

	private Snapshot snapshot;

	private boolean compiled;

	public static ScriptException AlreadyCompiled()
	{
		throw new ScriptException("Expression already compiled");
	}
}