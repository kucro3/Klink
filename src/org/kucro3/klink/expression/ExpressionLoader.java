package org.kucro3.klink.expression;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.kucro3.klink.Klink;
import org.kucro3.klink.Ref;
import org.kucro3.klink.Snapshot;
import org.kucro3.klink.Util;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.syntax.Sequence;

public class ExpressionLoader {
	public static void load(Klink sys, ExpressionLibrary lib, Class<?> clz)
	{
		boolean nonstatic =
				clz.getAnnotation(Nonstatic.class) != null;
		
		Object instance = null;
		if(nonstatic)
			try {
				instance = clz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				// unused
				throw new ScriptException("Internal: Construction Reflection Failure");
			}
		
		Method[] mthds = clz.getMethods();
		for(Method mthd : mthds)
		{
			ExpressionFunction[] efs = mthd.getAnnotationsByType(ExpressionFunction.class);
			
			if(efs.length == 0)
				continue;
			
			// check modifier
			if(!nonstatic)
				if(!Modifier.isStatic(mthd.getModifiers()))
				{
					sys.getMessenger().warn("In class: " + clz.getCanonicalName() +
							", Ignored: " + mthd.toGenericString() + ", Cause: Nonstatic method in static class");
					continue;
				}

			
			// check argument
			Class<?>[] params = mthd.getParameterTypes();
			if(params.length == 2
			|| params[0] == ExpressionLibrary.class
			|| params[1] == Sequence.class)
				;
			else if(params.length == 3
			|| params[0] == ExpressionLibrary.class
			|| params[1] == Ref[].class
			|| params[2] == Sequence.class)
				;
			else if(params.length == 4
			|| params[0] == ExpressionLibrary.class
			|| params[1] == Ref[].class
			|| params[2] == Sequence.class
			|| params[3] == Flow.class)
				;
			else if(params.length == 5
			|| params[0] == ExpressionLibrary.class
			|| params[1] == Ref[].class
			|| params[2] == Sequence.class
			|| params[3] == Flow.class
			|| params[4] == Snapshot.class)
				;
			else
			{
				sys.getMessenger().warn("In class: " + clz.getCanonicalName() +
						", Ignored: " + mthd.toGenericString() + ", Cause: Illegal Argument Type");
				continue;
			}
			
			ReflectionInvoker invoker = new ReflectionInvoker(instance, mthd);
			for(ExpressionFunction ef : efs)
				lib.putExpression(new Expression(ef.name(), invoker));
		}
	}
	
	static class ReflectionInvoker implements ExpressionCompiler
	{
		ReflectionInvoker(Object instance, Method mthd)
		{
			this.mthd = mthd;
			this.instance = instance;
		}
		
		@Override
		public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock, Snapshot snapshot) 
		{
			try {
				switch(mthd.getParameterCount())
				{
				case 2:
					return (ExpressionInstance) mthd.invoke(instance, lib, seq);
				case 3:
					return (ExpressionInstance) mthd.invoke(instance, lib, refs, seq);
				case 4:
					return (ExpressionInstance) mthd.invoke(instance, lib, refs, seq, codeBlock);
				case 5:
					return (ExpressionInstance) mthd.invoke(instance, lib, refs, seq, codeBlock, snapshot);
				default:
					throw Util.ShouldNotReachHere();
				}
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				if(e.getCause() instanceof ScriptException)
					throw (ScriptException) e.getCause();
				// unused
				throw new ScriptException("Internal: Reflection Failure", e);
			}
		}
		
		private final Object instance;
		
		private final Method mthd;
	}
}