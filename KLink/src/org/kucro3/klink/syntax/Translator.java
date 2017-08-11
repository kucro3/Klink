package org.kucro3.klink.syntax;

import org.kucro3.klink.Klink;
import org.kucro3.klink.expression.ExpressionLibrary;

public class Translator {
	private Translator(Sequence globalSeq)
	{
		this.globalSeq = globalSeq;
	}
	
	public static Executable translate(Klink sys, Sequence globalSeq, TranslatorReference tRef)
	{
		Translator instance = new Translator(globalSeq);
		if(tRef != null)
			tRef.set(instance);
		
		ExpressionLibrary lib = sys.getExpressions();
		Flow body = new Flow();
		
		
		
		return body;
	}
	
	public Operation pullOperation()
	{
		
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
		
	}
	
	public Sequence getGlobal()
	{
		return globalSeq;
	}
	
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