package org.kucro3.klink.syntax;

import java.util.ArrayList;

import org.kucro3.klink.Klink;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionLibrary;

public class Translator {
	public Translator(Sequence globalSeq, ExpressionLibrary lib)
	{
		this.globalSeq = globalSeq;
		this.lib = lib;
	}
	
	public static Executable translate(Klink sys, Sequence globalSeq, TranslatorReference tRef)
	{
		Translator instance = new Translator(globalSeq, sys.getExpressions());
		if(tRef != null)
			tRef.set(instance);
		
		Flow body = new Flow();
		
		while(instance.globalSeq.hasNext())
			body.append(instance.pull());
		
		return body;
	}
	
	public Executable pull()
	{
		String first = globalSeq.getNext();
		
		switch(first)
		{
		case ";":
			globalSeq.next();
			return new Empty();
		
		case "if":
		case "ifnot":
			return pullBranch();
			
		case "for":
			globalSeq.next();
			return pullFor();
			
		case "while":
			globalSeq.next();
			return pullWhile();
			
		case "do-while":
			globalSeq.next();
			return pullDoWhile();
			
		default:
			globalSeq.next();
			return pullOperation();
		}
	}
	
	public Operation pullOperation()
	{
		String first = globalSeq.getNext();
		String current = null;
		Flow codeblock = null;
		
		if(first.equals(";"))
		{
			globalSeq.next();
			return null;
		}
		
		Expression expression = lib.requireExpression(first);
		
		ArrayList<String> strs = new ArrayList<>();
		while(true) switch(current = globalSeq.next())
		{
		case ":":
			codeblock = pullCodeBlock();
			
		case ";":
			return new Operation(expression, new Sequence(strs.toArray(new String[0])), codeblock);
		
		default:
			strs.add(current);
			continue;
		}
	}
	
	public LoopFor pullFor()
	{
		Executable init = pullOperation();
		Judgable judgable = pullJudge();
		globalSeq.next();
		Executable control = pullOperation();
		switch(globalSeq.next())
		{
		case ";":	return new LoopFor(init, judgable, control, null);
		case ":":	return new LoopFor(init, judgable, control, pullCodeBlock());
		default:	throw new RuntimeException("Should not reach here");
		}
	}
	
	public LoopWhile pullWhile()
	{
		Judgable judgable = pullJudge();
		switch(globalSeq.next())
		{
		case ";":	return new LoopWhile(judgable, null);
		case ":":	return new LoopWhile(judgable, pullCodeBlock());
		default:	throw new RuntimeException("Should not reach here");
		}
	}
	
	public LoopDoWhile pullDoWhile()
	{
		Judgable judgable = pullJudge();
		switch(globalSeq.next())
		{
		case ";":	return new LoopDoWhile(judgable, null);
		case ":":	return new LoopDoWhile(judgable, pullCodeBlock());
		default:	throw new RuntimeException("Should not reach here");
		}
	}
	
	public Branch pullBranch()
	{
		Judgable judgable = pullJudge();
		switch(globalSeq.next())
		{
		case ";":	return new Branch(judgable, null);
		case ":":	return new Branch(judgable, pullCodeBlock());
		default:	throw new RuntimeException("Should not reach here");
		}
	}
	
	public Judgable pullJudge()
	{
		try {
			String current;
			Judgable judgable;
			Operation operation;
			
			boolean not , and = false;
			LOOP0: while(true) 
			{
				current = globalSeq.getNext();
				not = false;
				switch(current)
				{
				case "ifnot":
					not = true;
				case "if":
					globalSeq.next();
					current = globalSeq.getNext();
					globalSeq.next();
					Expression exp = lib.requireExpression(current);
					ArrayList<String> strs = new ArrayList<>();
					LOOP1: while(true) switch(current = globalSeq.getNext())
					{
					case ":":
					case ";":
						operation = new Operation(exp, new Sequence(strs.toArray(new String[0])), null);
						judgable = not ? new JudgeIfnot(operation) : new JudgeIf(operation);
						if(left == null)
							return judgable;
						else
							return and ? new JudgeAnd(left, judgable) : new JudgeOr(left, judgable);
							
					case "&":
						and = true;
					case "|":
						operation = new Operation(exp, new Sequence(strs.toArray(new String[0])), null);
						judgable = not ? new JudgeIfnot(operation) : new JudgeIf(operation);
						if(left == null)
							left = judgable;
						else
							left = and ? new JudgeAnd(left, judgable) : new JudgeOr(left, judgable);
						globalSeq.next();
						continue LOOP0;
					
					default:
						strs.add(current);
						globalSeq.next();
						continue LOOP1;
					}
					
				case ";":
				case ":":
					return new JudgeTrue();
				
				default:
					throw JudgableExpressionRequired();
				}
			}
		} finally {
			left = null;
		}
	}
	
	public Flow pullCodeBlock()
	{
		Flow codeblock = new Flow();
		LOOP: while(true)
		{
			String first = globalSeq.getNext();
			switch(first)
			{
			case ";":
				globalSeq.next();
				break LOOP;
				
			default:
				codeblock.append(pull());
				continue;
			}
		}
		return codeblock;
	}
	
	public Sequence getGlobal()
	{
		return globalSeq;
	}
	
	public static ScriptException JudgableExpressionRequired()
	{
		throw new ScriptException("Judgable expression required");
	}
	
	private Judgable left;
	
	private final ExpressionLibrary lib;
	
	private final Sequence globalSeq;
	
	public static class TranslatorReference
	{
		void set(Translator ref)
		{
			this.ref = ref;
		}
		
		public Translator get()
		{
			return ref;
		}
		
		Translator ref;
	}
}