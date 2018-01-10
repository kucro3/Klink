package org.kucro3.klink;

import java.util.Optional;
import java.util.function.Consumer;

import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.flow.*;
import org.kucro3.klink.syntax.Sequence;

public interface Translator {
	public boolean finished();
	
	public Flow getContext();
	
	public void setContext(Flow context);
	
	public void resetContext();
	
	public Sequence getGlobal();
	
	void setGlobal(Sequence seq);
	
	public Executable pull();
	
	public Branch pullBranch();
	
	public Flow pullCodeBlock();
	
	public LoopWhile pullWhile();
	
	public LoopDoWhile pullDoWhile();
	
	public LoopFor pullFor();
	
	public Predicatable pullPredicate();
	
	public default Optional<Operation> pullOperation() 
	{
		return pullOperation(Util.NULL_REFS);
	}
	
	public Optional<Operation> pullOperation(Ref[] refs);
	
	public Optional<Operation> pullOperation(Ref[] refs, Flow defaultCodeBlock);
	
	public Flow pullAll();

	void setLibrary(ExpressionLibrary lib);

	ExpressionLibrary getLibrary();

	default Flow temporary(Flow flow, Sequence seq, Consumer<Translator> trans)
	{
		Flow old = getContext();
		Sequence oldSeq = getGlobal();
		setContext(flow);
		setGlobal(seq);
		trans.accept(this);
		setContext(old);
		setGlobal(oldSeq);
		return flow;
	}

	default Flow temporary(Sequence seq, Consumer<Translator> trans)
	{
		return temporary(new Flow(), seq, trans);
	}
	
	default Flow temporary(Consumer<Translator> trans)
	{
		return temporary(new Flow(), new Sequence(), trans);
	}

	Snapshot snapshot();
}