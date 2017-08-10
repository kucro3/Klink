package org.kucro3.klink.expression;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Variables.Variable;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.Expression.ReturnType;
import org.kucro3.klink.syntax.Sequence;

public class ExpressionLoader {
	public static void load(ExpressionLibrary lib, Class<?> clz)
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
					Klink.getMessenger().warn("In class: " + clz.getCanonicalName() +
							", Ignored: " + mthd.toGenericString() + ", Cause: Nonstatic method in static class");
					continue;
				}
			
			// check argument
			Class<?>[] params = mthd.getParameterTypes();
			if(params.length != 3
			|| params[0] != Environment.class
			|| params[1] != Variable.class
			|| params[2] != Sequence.class)
			{
				Klink.getMessenger().warn("In class: " + clz.getCanonicalName() +
						", Ignored: " + mthd.toGenericString() + ", Cause: Illegal Argument Type");
				continue;
			}
			
			ReturnType rt;
			Class<?> crt = mthd.getReturnType();
			if(crt == boolean.class)
				rt = ReturnType.BOOLEAN;
			else if(crt == void.class)
				rt = ReturnType.VOID;
			else
				rt = ReturnType.VARIABLE;
			
			ReflectionInvoker invoker = new ReflectionInvoker(instance, mthd);
			for(ExpressionFunction ef : efs)
				lib.putExpression(new Expression(ef.name(), invoker, rt));
		}
	}
	
	static class ReflectionInvoker implements ExpressionInvoker
	{
		ReflectionInvoker(Object instance, Method mthd)
		{
			this.mthd = mthd;
			this.instance = instance;
		}
		
		@Override
		public Object call(Environment env, Variable var, Sequence seq) 
		{
			try {
				return mthd.invoke(instance, env, var, seq);
			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				// unused
				throw new ScriptException("Internal: Reflection Failure");
			}
		}
		
		private final Object instance;
		
		private final Method mthd;
	}
}