package org.kucro3.klink;

import java.util.HashMap;
import java.util.Map;

import org.kucro3.klink.exception.ScriptException;

public class Namespace {
	public Namespace(String name)
	{
		this.name = name;
	}
	
	public Executable get(String name)
	{
		return map.get(name);
	}
	
	public Executable require(String name)
	{
		Executable rt;
		if((rt = map.get(name)) == null)
			throw NoSuchDocument(this.name, name);
		return rt;
	}
	
	public void remove(String name)
	{
		map.remove(name);
	}
	
	public void delete(String name)
	{
		if(map.remove(name) == null)
			throw NoSuchDocument(this.name, name);
	}
	
	public boolean contains(String name)
	{
		return map.containsKey(name);
	}
	
	public void put(String name, Executable e)
	{
		if(map.putIfAbsent(name, e) != null)
			throw DocumentRedefinition(this.name, name);
	}
	
	public void set(String name, Executable e)
	{
		map.put(name, e);
	}
	
	public String getName()
	{
		return name;
	}
	
	private final String name; // unused
	
	public static ScriptException NoSuchDocument(String ns, String name)
	{
		return new ScriptException("No such document: \"" + name + "\" in namespace: \"" + ns + "\"");
	}
	
	public static ScriptException DocumentRedefinition(String ns, String name)
	{
		return new ScriptException("Document redefinition: \"" + name + "\" in namespace: \"" + ns + "\"");
	}
	
	private final Map<String, Executable> map = new HashMap<>();
}