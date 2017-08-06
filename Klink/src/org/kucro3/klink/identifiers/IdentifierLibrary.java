package org.kucro3.klink.identifiers;

import java.util.HashMap;
import java.util.Map;

import org.kucro3.klink.exception.ScriptException;

public class IdentifierLibrary {
	public IdentifierLibrary()
	{
	}
	
	public void removeIdentifier(String name)
	{
		identifiers.remove(name);
	}
	
	public Identifier getIdentifier(String name)
	{
		return identifiers.get(name);
	}
	
	public Identifier requireIdentifier(String name)
	{
		Identifier identifier;
		if((identifier = getIdentifier(name)) == null)
			throw NoSuchIdentifier(name);
		return identifier;
	}
	
	public static ScriptException NoSuchIdentifier(String name)
	{
		return new ScriptException("No such identifier: " + name);
	}
	
	private final Map<String, Identifier> identifiers = new HashMap<>();
}