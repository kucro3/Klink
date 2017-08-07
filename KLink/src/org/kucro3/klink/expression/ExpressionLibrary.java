package org.kucro3.klink.expression;

import java.util.HashMap;
import java.util.Map;

import org.kucro3.klink.exception.ScriptException;

public class ExpressionLibrary {
	public ExpressionLibrary()
	{
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
	
	public static ScriptException NoSuchIdentifier(String name)
	{
		return new ScriptException("No such identifier: " + name);
	}
	
	private final Map<String, Expression> identifiers = new HashMap<>();
}