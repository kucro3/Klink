package org.kucro3.klink.flow;

import org.kucro3.klink.*;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.syntax.Sequence;

public class Operation implements Executable {
	public Operation(ExpressionLibrary lib, String name, Ref[] refs, Sequence seq, Flow codeBlock, Snapshot snapshot, boolean dynamic)
	{
		this.dynamic = dynamic;
		this.seq = seq;

		this.lib = lib;
		this.name = name;
		this.refs = refs;
		this.codeBlock = codeBlock;
		this.snapshot = snapshot;

		if(!dynamic)
			compile();
	}

	private void compile()
	{
		if(compiled)
			throw AlreadyCompiled();

		this.exp = lib.requireExpression(name);
		this.instance = this.exp.compile(lib, refs, seq, codeBlock, snapshot);

		this.lib = null;
		this.name = null;
		this.refs = null;
		this.codeBlock = null;
		this.snapshot = null;

		compiled = true;
	}

	private void lazyCompile()
	{
		if(!compiled)
			compile();
	}
	
	public void execute(Klink sys, Environment env)
	{
		if(dynamic)
			lazyCompile();
		instance.call(sys, env);
	}
	
	public Expression getExpression()
	{
		if(dynamic)
			lazyCompile();
		return exp;
	}
	
	public Sequence getSequence()
	{
		return seq;
	}
	
	private ExpressionInstance instance;
	
	private Expression exp;
	
	private final Sequence seq;

	private final boolean dynamic;

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