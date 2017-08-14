package org.kucro3.klink.expression.internal;

import java.util.HashMap;
import java.util.Map;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Ref;
import org.kucro3.klink.Registers.*;
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
		
		map.put("OR0", () -> new Register(new AdapterForOR(), 0));
		map.put("OR1", () -> new Register(new AdapterForOR(), 1));
		map.put("OR2", () -> new Register(new AdapterForOR(), 2));
		map.put("OR3", () -> new Register(new AdapterForOR(), 3));
		map.put("OR4", () -> new Register(new AdapterForOR(), 4));
		map.put("OR5", () -> new Register(new AdapterForOR(), 5));
		map.put("OR6", () -> new Register(new AdapterForOR(), 6));
		map.put("OR7", () -> new Register(new AdapterForOR(), 7));
		map.put("OR8", () -> new Register(new AdapterForOR(), 8));
		map.put("OR9", () -> new Register(new AdapterForOR(), 9));
		map.put("ORA", () -> new Register(new AdapterForOR(), 10));
		map.put("ORB", () -> new Register(new AdapterForOR(), 11));
		map.put("ORC", () -> new Register(new AdapterForOR(), 12));
		map.put("ORD", () -> new Register(new AdapterForOR(), 13));
		map.put("ORE", () -> new Register(new AdapterForOR(), 14));
		map.put("ORF", () -> new Register(new AdapterForOR(), 15));
		
		map.put("I08A", () -> new Register(new AdapterForI08(), 0));
		map.put("I08B", () -> new Register(new AdapterForI08(), 1));
		map.put("I08C", () -> new Register(new AdapterForI08(), 2));
		map.put("I08D", () -> new Register(new AdapterForI08(), 3));
		map.put("I08E", () -> new Register(new AdapterForI08(), 4));
		map.put("I08F", () -> new Register(new AdapterForI08(), 5));
		
		map.put("I16A", () -> new Register(new AdapterForI16(), 0));
		map.put("I16B", () -> new Register(new AdapterForI16(), 1));
		map.put("I16C", () -> new Register(new AdapterForI16(), 2));
		map.put("I16D", () -> new Register(new AdapterForI16(), 3));
		map.put("I16E", () -> new Register(new AdapterForI16(), 4));
		map.put("I16F", () -> new Register(new AdapterForI16(), 5));
		
		map.put("I32A", () -> new Register(new AdapterForI32(), 0));
		map.put("I32B", () -> new Register(new AdapterForI32(), 1));
		map.put("I32C", () -> new Register(new AdapterForI32(), 2));
		map.put("I32D", () -> new Register(new AdapterForI32(), 3));
		map.put("I32E", () -> new Register(new AdapterForI32(), 4));
		map.put("I32F", () -> new Register(new AdapterForI32(), 5));
		
		map.put("I64A", () -> new Register(new AdapterForI64(), 0));
		map.put("I64B", () -> new Register(new AdapterForI64(), 1));
		map.put("I64C", () -> new Register(new AdapterForI64(), 2));
		map.put("I64D", () -> new Register(new AdapterForI64(), 3));
		map.put("I64E", () -> new Register(new AdapterForI64(), 4));
		map.put("I64F", () -> new Register(new AdapterForI64(), 5));
		
		map.put("F32A", () -> new Register(new AdapterForF32(), 0));
		map.put("F32B", () -> new Register(new AdapterForF32(), 1));
		map.put("F32C", () -> new Register(new AdapterForF32(), 2));
		map.put("F32D", () -> new Register(new AdapterForF32(), 3));
		map.put("F32E", () -> new Register(new AdapterForF32(), 4));
		map.put("F32F", () -> new Register(new AdapterForF32(), 5));
		
		map.put("F64A", () -> new Register(new AdapterForF64(), 0));
		map.put("F64B", () -> new Register(new AdapterForF64(), 1));
		map.put("F64C", () -> new Register(new AdapterForF64(), 2));
		map.put("F64D", () -> new Register(new AdapterForF64(), 3));
		map.put("F64E", () -> new Register(new AdapterForF64(), 4));
		map.put("F64F", () -> new Register(new AdapterForF64(), 5));
		
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