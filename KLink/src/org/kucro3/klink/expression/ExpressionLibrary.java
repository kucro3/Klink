package org.kucro3.klink.expression;

import java.util.HashMap;
import java.util.Map;

import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.internal.Break;
import org.kucro3.klink.expression.internal.Escape;
import org.kucro3.klink.expression.internal.Exit;
import org.kucro3.klink.expression.internal.False;
import org.kucro3.klink.expression.internal.PrintVar;
import org.kucro3.klink.expression.internal.PrintlnVar;
import org.kucro3.klink.expression.internal.True;
import org.kucro3.klink.expression.internal.VarCall;
import org.kucro3.klink.expression.internal.VarControl;

public class ExpressionLibrary {
	public ExpressionLibrary()
	{
		this(true);
	}
	
	public ExpressionLibrary(boolean sys)
	{
		if(sys)
			init();
	}
	
	private final void init()
	{
		forceExpression(True.instance());
		forceExpression(False.instance());
		forceExpression(VarControl.instance());
		forceExpression(VarCall.instance());
		forceExpression(PrintVar.instance());
		forceExpression(PrintlnVar.instance());
		forceExpression(Exit.instance());
		forceExpression(Escape.instance());
		forceExpression(Break.instance());
	}
	
	public void removeExpression(String name)
	{
		identifiers.remove(name);
	}
	
	public Expression getExpression(String name)
	{
		return identifiers.get(name);
	}
	
	public Expression requireExpression(String name)
	{
		Expression identifier;
		if((identifier = getExpression(name)) == null)
			throw NoSuchIdentifier(name);
		return identifier;
	}
	
	public void forceExpression(Expression expression)
	{
		String name = expression.getName();
		identifiers.put(name, expression);
	}
	
	public void putExpression(Expression expression)
	{
		String name = expression.getName();
		if(identifiers.containsKey(name))
			throw IdentifierRedefinition(name);
		identifiers.put(name, expression);
	}
	
	public static ScriptException NoSuchIdentifier(String name)
	{
		return new ScriptException("No such identifier: " + name);
	}
	
	public static ScriptException IdentifierRedefinition(String name)
	{
		return new ScriptException("Identifier Redefinition: " + name);
	}
	
	private final Map<String, Expression> identifiers = new HashMap<>();
}