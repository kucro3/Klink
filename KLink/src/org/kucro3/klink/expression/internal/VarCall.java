package org.kucro3.klink.expression.internal;

import java.util.HashMap;
import java.util.Map;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Ref;
import org.kucro3.klink.Registers.Register;
import org.kucro3.klink.Variables.Var;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.syntax.Flow;
import org.kucro3.klink.syntax.Operation;
import org.kucro3.klink.syntax.Sequence;
import org.kucro3.klink.syntax.Translator;

public class VarCall implements ExpressionCompiler {
	@Override
	public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock) 
	{
		String vars = seq.next();
		Ref[] v;
		
		if(vars.startsWith("(") && vars.endsWith(")"))
		{
			String[] svar = vars.substring(1, vars.length() - 1).split(",");
			v = new Ref[svar.length];
			for(int i = 0; i < svar.length; i++)
				v[i] = toRef(svar[i]);
		}
		else
			(v = new Ref[1])[0] = toRef(vars);
		
		Translator translator = new Translator(Sequence.appendTail(seq, ";"), lib);
		Operation operation = translator.pullOperation(v);
		
		return new Compiled(operation);
	}
	
	public static Ref toRef(String var)
	{
		if(var.startsWith("@")) // register
		{
			var = var.substring(1);
			RegParser parser;
			if((parser = PARSERS.get(var)) == null)
				throw UnknownRegister(var);
			return parser.regiser();
		}
		else if(var.startsWith("$")) // variable
			return new Var(var.substring(1));
		else
			throw InvalidReference(var);
	}
	
	public static ScriptException InvalidReference(String ref)
	{
		return new ScriptException("Invalid reference: " + ref);
	}
	
	public static ScriptException UnknownRegister(String ref)
	{
		return new ScriptException("Unknown register: " + ref);
	}
	
	public static Expression instance()
	{
		return new Expression("#", new VarCall());
	}
	
	private static final Map<String, RegParser> PARSERS;
	
	static {
		HashMap<String, RegParser> map = new HashMap<>();
		PARSERS = map;
	}
	
	public static interface RegParser
	{
		Register regiser();
	}
	
	public static class Compiled implements ExpressionInstance
	{
		public Compiled(Operation operation)
		{
			this.operation = operation;
		}
		
		@Override
		public void call(Klink sys, Environment env) 
		{
			if(operation != null)
				this.operation.execute(sys);
		}
		
		private final Operation operation;
	}
}