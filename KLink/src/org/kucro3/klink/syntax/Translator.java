package org.kucro3.klink.syntax;

import java.util.ArrayList;

import org.kucro3.klink.Klink;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionLibrary;

public class Translator {
	private Translator(Sequence globalSeq, ExpressionLibrary lib)
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
			body.appendOperation(instance.pullOperation());
		
		return body;
	}
	
	public Executable pullOperation()
	{
		String first = globalSeq.next();
		String current = null;
		Flow codeblock = null;
		
		switch(first)
		{
		case "if":
			return pullIf();
			
		case "ifnot":
			return pullIfnot();
			
		case "for":
			return pullFor();
			
		case "while":
			return pullWhile();
			
		case "do-while":
			return pullDoWhile();
		
		default:
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
	}
	
	public LoopFor pullFor()
	{
		
	}
	
	public LoopWhile pullWhile()
	{
		
	}
	
	public LoopDoWhile pullDoWhile()
	{
		
	}
	
	public JudgeIf pullIf()
	{
		
	}
	
	public JudgeIfnot pullIfnot()
	{
		
	}
	
	public JudgeAnd pullAnd()
	{
		
	}
	
	public JudgeOr pullOr()
	{
		
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
				break LOOP;
				
			default:
				codeblock.appendOperation(pullOperation());
				continue;
			}
		}
		return codeblock;
	}
	
	public Sequence getGlobal()
	{
		return globalSeq;
	}
	
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