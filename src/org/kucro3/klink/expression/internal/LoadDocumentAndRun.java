package org.kucro3.klink.expression.internal;

import org.kucro3.klink.Executable;
import org.kucro3.klink.Namespace;
import org.kucro3.klink.Ref;
import org.kucro3.klink.SequenceUtil;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.syntax.Sequence;

public class LoadDocumentAndRun implements ExpressionCompiler {
	@Override
	public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock) 
	{
		final String file = seq.next();
		final String name = seq.hasNext() ? seq.next() : file;
		return (sys, env) -> {
			if(sys.getNamespace().contains(name))
				throw Namespace.DocumentRedefinition(sys.getNamespace().getName(), file);
			
			Sequence _seq = SequenceUtil.readFrom(file);
			_seq.setName(name);
			Executable exec = sys.compile(_seq);
			sys.getNamespace().put(name, exec);
			sys.getMessenger().info("Loaded script document: " + name + " (" + file + ")");
			exec.execute(sys, env);
		};
	}
	
	public static Expression instance()
	{
		return new Expression("loadscript&run", new LoadDocumentAndRun());
	}
}