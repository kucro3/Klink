package org.kucro3.klink;

import java.util.*;

import org.kucro3.klink.exception.ScriptException;

public class Klink {
	public static Environment getEnv(String name)
	{
		return env.get(name);
	}
	
	public static Environment requireEnv(String name)
	{
		Environment env;
		if((env = getEnv(name)) == null)
			throw NoSuchEnv(name);
		return env;
	}
	
	public static Environment createEnv(String name)
	{
		if(forEnv(name))
			throw EnvAlreadyExist(name);
		Environment env = new Environment(name);
		Klink.env.put(name, env);
		return env;
	}
	
	public static void putEnv(Environment env)
	{
		if(forEnv(env.getName()))
			throw EnvAlreadyExist(env.getName());
		setEnv(env);
	}
	
	public static void setEnv(Environment env)
	{
		Klink.env.put(env.getName(), env);
	}
	
	public static void putCurrentEnv(Environment env)
	{
		putEnv(env);
		currentEnv = env;
	}
	
	public static void setCurrentEnv(Environment env)
	{
		setEnv(env);
		currentEnv = env;
	}
	
	public static boolean forEnv(String name)
	{
		return env.containsKey(name);
	}
	
	public static void destroyEnv(String name)
	{
		env.remove(name);
	}
	
	public static Messenger getMessenger()
	{
		return messenger;
	}
	
	public static void setMessenger(Messenger messenger)
	{
		if(messenger == null)
			messenger = NULL_MESSENGER;
		Klink.messenger = messenger;
	}
	
	public static Environment currentEnv()
	{
		return currentEnv;
	}
	
	public static void currentEnv(String name)
	{
		currentEnv = requireEnv(name);
	}
	
	public static Environment systemEnv()
	{
		return systemEnv;
	}
	
	public static ScriptException NoSuchEnv(String name)
	{
		return new ScriptException("No such env: " + name);
	}
	
	public static ScriptException EnvAlreadyExist(String name)
	{
		return new ScriptException("ENV already exist: " + name);
	}
	
	private static final Environment systemEnv = new Environment(null);
	
	private static Environment currentEnv;
	
	private static final Map<String, Environment> env = new HashMap<>();
	
	private static Messenger messenger;
	
	private static final Messenger NULL_MESSENGER = new Messenger() {
		@Override
		public void warn(String msg) {
		}

		@Override
		public void info(String msg) {
		}
	};
}