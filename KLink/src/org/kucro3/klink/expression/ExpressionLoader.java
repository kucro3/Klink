package org.kucro3.klink.expression;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.kucro3.klink.Klink;
import org.kucro3.klink.Ref;
import org.kucro3.klink.Variables.Var;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.syntax.Flow;
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
			if(params.length != 4
			|| params[0] != ExpressionLibrary.class
			|| params[1] != Var[].class
			|| params[2] != Sequence.class
			|| params[3] != Flow.class)
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
		public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock) 
		{
			try {
				return (ExpressionInstance) mthd.invoke(instance, lib, refs, seq, codeBlock);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// unused
				throw new ScriptException("Internal: Reflection Failure");
			}
		}
		
		private final Object instance;
		
		private final Method mthd;
	}
}