package org.kucro3.klink.expression;

import java.util.HashMap;
import java.util.Map;

import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.internal.False;
import org.kucro3.klink.expression.internal.True;

public class ExpressionLibrary {
	public ExpressionLibrary()
	{
		init();
	}
	
	private final void init()
	{
		forceExpression(True.instance());
		forceExpression(False.instance());
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