package org.kucro3.klink.syntax;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

import org.kucro3.klink.*;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.flow.*;

public class KlinkTranslator implements Translator {
	public KlinkTranslator(Sequence globalSeq, ExpressionLibrary lib)
	{
		this.globalSeq = globalSeq;
		this.lib = lib;
	}
	
	public KlinkTranslator(ExpressionLibrary lib)
	{
		this(new Sequence(), lib);
	}
	
	public KlinkTranslator()
	{
		this(new ExpressionLibrary());
	}
	
	@Deprecated
	public static Executable translate(Klink sys, Sequence globalSeq, TranslatorReference tRef)
	{
		KlinkTranslator instance = new KlinkTranslator(globalSeq, sys.getExpressions());
		if(tRef != null)
			tRef.set(instance);
		
		while(instance.globalSeq.hasNext())
			instance.pull();
		
		return instance.getContext();
	}
	
	@Override
	public Snapshot snapshot()
	{
		return new Snapshot(this, this.getContext().snapshot());
	}
	
	@Override
	public Executable pull()
	{
		String first = globalSeq.getNext();
		
		switch(first)
		{
		case ";":
			globalSeq.next();
			return new Empty();
	
		case "else":
			globalSeq.next();
			elseBranch = true;
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
			return pullOperation().get(); // non-null
		}
	}
	
	@Override
	public Optional<Operation> pullOperation(Ref[] refs)
	{
		return record(pullOperation0(refs, null));
	}
	
	@Override
	public Optional<Operation> pullOperation(Ref[] refs, Flow defaultCodeBlock)
	{
		return record(pullOperation0(refs, defaultCodeBlock));
	}
	
	Optional<Operation> pullOperation0(Ref[] refs, Flow defaultCodeBlock)
	{
		String first = globalSeq.getNext();
		String current = null;
		Flow codeblock = null;
		
		if(first.equals(";"))
		{
			globalSeq.next();
			return Optional.empty();
		}
		
		String expression = first;
		
		globalSeq.next();
		
		ArrayList<String> strs = new ArrayList<>();
		while(true) switch(current = globalSeq.next())
		{
		case ":":
			codeblock = pullCodeBlock0();
			
		case ";":
			return Optional.of(LinedOperation.construct(lib, expression, refs,
					new Sequence(strs.toArray(new String[0]), globalSeq.currentRow() - 1, 0, globalSeq.getName())
				, codeblock == null ? defaultCodeBlock : codeblock, snapshot(), globalSeq.currentRow()));
		
		default:
			strs.add(current);
			continue;
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public LoopFor pullFor()
	{
		Optional<Executable> init = (Optional) pullOperation0(Util.NULL_REFS, null);
		Predicatable judgable = pullJudge0();
		globalSeq.next();
		Optional<Executable> control = (Optional) pullOperation0(Util.NULL_REFS, null);
		switch(globalSeq.next())
		{
		case ";":	return record(new LoopFor(init, judgable, control, new Empty()));
		case ":":	return record(new LoopFor(init, judgable, control, pullCodeBlock0()));
		default:	throw new RuntimeException("Should not reach here");
		}
	}
	
	@Override
	public LoopWhile pullWhile()
	{
		Predicatable judgable = pullJudge0();
		switch(globalSeq.next())
		{
		case ";":	return record(new LoopWhile(judgable, new Empty()));
		case ":":	return record(new LoopWhile(judgable, pullCodeBlock0()));
		default:	throw new RuntimeException("Should not reach here");
		}
	}
	
	@Override
	public LoopDoWhile pullDoWhile()
	{
		Predicatable judgable = pullJudge0();
		switch(globalSeq.next())
		{
		case ";":	return record(new LoopDoWhile(judgable, new Empty()));
		case ":":	return record(new LoopDoWhile(judgable, pullCodeBlock0()));
		default:	throw new RuntimeException("Should not reach here");
		}
	}
	
	@Override
	public Branch pullBranch()
	{
		try {
			Predicatable judgable = pullJudge0();
			switch(globalSeq.next())
			{
			case ";":	
				return record(elseBranch ? new BranchElse(getContext().tail().orElse(new Empty()), 
						judgable, new Empty()) : new Branch(judgable, null));
			case ":":	
				return record(elseBranch ? new BranchElse(getContext().tail().orElse(new Empty()), 
						judgable, pullCodeBlock0()) : new Branch(judgable, pullCodeBlock0()));
			default:	throw new RuntimeException("Should not reach here");
			}
		} finally {
			elseBranch = false;
		}
	}
	
	@Override
	public Predicatable pullPredicate()
	{
		return record(pullJudge0());
	}
	
	Predicatable pullJudge0()
	{
		try {
			String current;
			Predicatable judgable;
			Operation operation;
			
			boolean not, and = false;
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
								new Sequence(strs.toArray(new String[0]), globalSeq.currentRow() - 1, 0, globalSeq.getName())
							, null, snapshot(), globalSeq.currentRow());
						judgable = not ? new LinedJudgeIfnot(operation, globalSeq.currentRow()) : new LinedJudgeIf(operation, globalSeq.currentRow());
						if(left == null)
							return judgable;
						else
							return and ? new PredicateAnd(left, judgable) : new PredicateOr(left, judgable);
							
					case "&":
						and = true;
					case "|":
						operation = LinedOperation.construct(lib, exp, Util.NULL_REFS,
								new Sequence(strs.toArray(new String[0]), globalSeq.currentRow() - 1, 0, globalSeq.getName())
							, null, snapshot(), globalSeq.currentRow());
						judgable = not ? new LinedJudgeIfnot(operation, globalSeq.currentRow()) : new LinedJudgeIf(operation, globalSeq.currentRow());
						if(left == null)
							left = judgable;
						else
							left = and ? new PredicateAnd(left, judgable) : new PredicateOr(left, judgable);
						globalSeq.next();
						continue LOOP0;
					
					default:
						strs.add(current);
						globalSeq.next();
						continue LOOP1;
					}
					
				case ";":
				case ":":
					return new PredicateTrue();
				
				default:
					throw JudgableExpressionRequired(globalSeq.currentRow());
				}
			}
		} finally {
			left = null;
		}
	}
	
	@Override
	public Flow pullCodeBlock()
	{
		return record(Optional.of(pullCodeBlock0())).get();
	}
	
	Flow pullCodeBlock0()
	{
		Flow codeblock = new Flow();
		temporary(codeblock, globalSeq, (trans) ->
		{
			LOOP: while(true)
			{
				String first = globalSeq.getNext();
				switch(first)
				{
				case ";":
					globalSeq.next();
					break LOOP;
					
				default:
					trans.pull();
					continue;
				}
			}
		});
		return codeblock;
	}
	
	@Override
	public void setLibrary(ExpressionLibrary lib)
	{
		this.lib = Objects.requireNonNull(lib);
	}
	
	@Override
	public ExpressionLibrary getLibrary()
	{
		return lib;
	}
	
	@Override
	public Sequence getGlobal()
	{
		return globalSeq;
	}
	
	@Override
	public void setGlobal(Sequence seq)
	{
		this.globalSeq = Objects.requireNonNull(seq);
	}
	
	public static ScriptException JudgableExpressionRequired(int line)
	{
		ScriptException e = new ScriptException("Judgable expression required");
		e.addLineInfo(line);
		return e;
	}
	
	@Override
	public Flow pullAll()
	{
		while(globalSeq.hasNext())
			pull();
		return this.currentFlow;
	}
	
	@Override
	public boolean finished()
	{
		return !globalSeq.hasNext();
	}
	
	@Override
	public void resetContext()
	{
		this.currentFlow.clear();
	}
	
	@Override
	public Flow getContext()
	{
		return currentFlow;
	}
	
	@Override
	public void setContext(Flow flow)
	{
		this.currentFlow = flow;
	}
	
	@SuppressWarnings("unchecked")
	<T extends Executable> Optional<T> record(Optional<T> t)
	{
		currentFlow.append((Optional<Executable>) t);
		return t;
	}
	
	<T extends Executable> T record(T t)
	{
		return record(Optional.of(t)).get();
	}
	
	private boolean elseBranch;
	
	private Predicatable left;
	
	private ExpressionLibrary lib;
	
	private Sequence globalSeq;
	
	private Flow currentFlow = new Flow();
	
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
		public static LinedOperation construct(ExpressionLibrary lib, String exp, Ref[] refs, Sequence seq, Flow codeBlock, Snapshot snapshot, int line)
		{
			try {
				return new LinedOperation(lib, exp, refs, seq, codeBlock, snapshot, line);
			} catch (ScriptException e) {
				e.addLineInfo(line);
				if(seq.getName() != null)
					e.addNameInfo(seq.getName());
				throw e;
			}
		}
		
		private LinedOperation(ExpressionLibrary lib, String exp, Ref[] refs, Sequence seq, Flow codeBlock, Snapshot snapshot, int line) 
		{
			super(lib, exp, refs, seq, codeBlock, snapshot);
			this.line = line;
		}
		
		@Override
		public void execute(Klink sys)
		{
			try {
				super.execute(sys);
			} catch (ScriptException e) {
				e.addLineInfo(line);
				if(super.getSequence().getName() != null)
					e.addNameInfo(super.getSequence().getName());
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
	
	public static class LinedJudgeIf extends PredicateIf implements Lined
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
	
	public static class LinedJudgeIfnot extends PredicateIfnot implements Lined
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