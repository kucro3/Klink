package org.kucro3.klink.syntax;

import java.util.ArrayList;

import org.kucro3.klink.Klink;
import org.kucro3.klink.Ref;
import org.kucro3.klink.Util;
import org.kucro3.klink.exception.ScriptException;
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
			return pullOperation();
		}
	}
	
	public Operation pullOperation()
	{
		return pullOperation(Util.NULL_REFS);
	}
	
	public Operation pullOperation(Ref[] refs)
	{
		String first = globalSeq.getNext();
		String current = null;
		Flow codeblock = null;
		
		if(first.equals(";"))
		{
			globalSeq.next();
			return null;
		}
		
		String expression = first;
		
		globalSeq.next();
		
		ArrayList<String> strs = new ArrayList<>();
		while(true) switch(current = globalSeq.next())
		{
		case ":":
			codeblock = pullCodeBlock();
			
		case ";":
			return LinedOperation.construct(lib, expression, refs,
					new Sequence(strs.toArray(new String[0]), globalSeq.currentRow(), 0), codeblock, globalSeq.currentRow());
		
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
					String exp = current;
					ArrayList<String> strs = new ArrayList<>();
					LOOP1: while(true) switch(current = globalSeq.getNext())
					{
					case ":":
					case ";":
						operation = LinedOperation.construct(lib, exp, Util.NULL_REFS,
								new Sequence(strs.toArray(new String[0]), globalSeq.currentRow() - 1, 0), null, globalSeq.currentRow());
						judgable = not ? new LinedJudgeIfnot(operation, globalSeq.currentRow()) : new LinedJudgeIf(operation, globalSeq.currentRow());
						if(left == null)
							return judgable;
						else
							return and ? new JudgeAnd(left, judgable) : new JudgeOr(left, judgable);
							
					case "&":
						and = true;
					case "|":
						operation = LinedOperation.construct(lib, exp, Util.NULL_REFS,
								new Sequence(strs.toArray(new String[0]), globalSeq.currentRow() - 1, 0), null, globalSeq.currentRow());
						judgable = not ? new LinedJudgeIfnot(operation, globalSeq.currentRow()) : new LinedJudgeIf(operation, globalSeq.currentRow());
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
					throw JudgableExpressionRequired(globalSeq.currentRow());
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
	
	public static ScriptException JudgableExpressionRequired(int line)
	{
		ScriptException e = new ScriptException("Judgable expression required");
		e.addLineInfo(line);
		return e;
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
	
	public static class LinedOperation extends Operation implements Lined
	{
		public static LinedOperation construct(ExpressionLibrary lib, String exp, Ref[] refs, Sequence seq, Flow codeBlock, int line)
		{
			try {
				return new LinedOperation(lib, exp, refs, seq, codeBlock, line);
			} catch (ScriptException e) {
				e.addLineInfo(line);
				throw e;
			}
		}
		
		private LinedOperation(ExpressionLibrary lib, String exp, Ref[] refs, Sequence seq, Flow codeBlock, int line) 
		{
			super(lib, exp, refs, seq, codeBlock);
			this.line = line;
		}
		
		@Override
		public void execute(Klink sys)
		{
			try {
				super.execute(sys);
			} catch (ScriptException e) {
				e.addLineInfo(line);
				throw e;
			}
		}

		@Override
		public int line()
		{
			return line;
		}
		
		private final int line;
	}
	
	public static class LinedJudgeIf extends JudgeIf implements Lined
	{
		public LinedJudgeIf(Operation operation, int line)
		{
			super(operation);
			this.line = line;
		}

		@Override
		public int line()
		{
			return line;
		}
		
		private final int line;
	}
	
	public static class LinedJudgeIfnot extends JudgeIfnot implements Lined
	{
		public LinedJudgeIfnot(Operation operation, int line) 
		{
			super(operation);
			this.line = line;
		}

		@Override
		public int line()
		{
			return line;
		}
		
		private final int line;
	}
}