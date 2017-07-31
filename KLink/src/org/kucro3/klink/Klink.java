package org.kucro3.klink;

import java.util.*;

import org.kucro3.klink.exception.ScriptException;

public class Klink {
	public static Environment getEnv(String name)
	{
		return env.get(name);
	}
	
	public static Environment createEnv(String name)
	{
		if(forEnv(name))
			throw new ScriptException("ENV already exist: " + name);
		Environment env = new Environment(name);
		Klink.env.put(name, env);
		return env;
	}
	
	public static boolean forEnv(String name)
	{
		return env.containsKey(name);
	}
	
	public static void destroyEnv(String name)
	{
		env.remove(name);
	}
	
	private static final Map<String, Environment> env = new HashMap<>();
}