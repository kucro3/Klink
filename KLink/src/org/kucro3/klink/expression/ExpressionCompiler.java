package org.kucro3.klink.expression;

import org.kucro3.klink.Ref;
import org.kucro3.klink.Snapshot;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.syntax.Sequence;

public interface ExpressionCompiler {
	public default ExpressionInstance compile(ExpressionLibrary lib, Sequence seq)
	{
		throw new AbstractMethodError();
	}
	
	public default ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq)
	{
		return compile(lib, seq);
	}
	
	public default ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock)
	{
		return compile(lib, refs, seq);
	}
	
	public default ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock, Snapshot context)
	{
		return compile(lib, refs, seq, codeBlock);
	}
	
	public interface Level0 extends ExpressionCompiler
	{
		public ExpressionInstance compile(ExpressionLibrary lib, Sequence seq);
	}
	
	public interface Level1 extends ExpressionCompiler
	{
		public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq);
	}
	
	public interface Level2 extends ExpressionCompiler
	{
		public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock);
	}
	
	public interface Level3 extends ExpressionCompiler
	{
		public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock, Snapshot context);
	}
}