package org.kucro3.klink;

import java.util.*;

import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.ExpressionLibrary;

public class Klink {
	public Klink()
	{
	}
	
	public ExpressionLibrary getExpressions()
	{
		return exprLibrary;
	}
	
	public Environment getEnv(String name)
	{
		return env.get(name);
	}
	
	public Environment requireEnv(String name)
	{
		Environment env;
		if((env = getEnv(name)) == null)
			throw NoSuchEnv(name);
		return env;
	}
	
	public Environment createEnv(String name)
	{
		if(forEnv(name))
			throw EnvAlreadyExist(name);
		Environment env = new Environment(name);
		this.env.put(name, env);
		return env;
	}
	
	public void putEnv(Environment env)
	{
		if(forEnv(env.getName()))
			throw EnvAlreadyExist(env.getName());
		setEnv(env);
	}
	
	public void setEnv(Environment env)
	{
		this.env.put(env.getName(), env);
	}
	
	public void putCurrentEnv(Environment env)
	{
		putEnv(env);
		currentEnv = env;
	}
	
	public void setCurrentEnv(Environment env)
	{
		setEnv(env);
		currentEnv = env;
	}
	
	public boolean forEnv(String name)
	{
		return env.containsKey(name);
	}
	
	public void destroyEnv(String name)
	{
		env.remove(name);
	}
	
	public Messenger getMessenger()
	{
		return messenger;
	}
	
	public void setMessenger(Messenger messenger)
	{
		if(messenger == null)
			messenger = NULL_MESSENGER;
		this.messenger = messenger;
	}
	
	public Environment currentEnv()
	{
		return currentEnv;
	}
	
	public void currentEnv(String name)
	{
		currentEnv = requireEnv(name);
	}
	
	public Environment systemEnv()
	{
		return systemEnv;
	}
	
	public void pushLoop()
	{
		loopFlags.addLast(true);
	}
	
	public boolean inLoop()
	{
		if(loopFlags.isEmpty())
			return false;
		return loopFlags.getLast();
	}
	
	public void popLoop()
	{
		if(loopFlags.isEmpty())
			throw NotInLoop();
		loopFlags.removeLast();
	}
	
	public void interruptLoop()
	{
		if(loopFlags.isEmpty())
			throw NotInLoop();
		loopFlags.removeLast();
		loopFlags.addLast(false);
	}
	
	public static ScriptException NoSuchEnv(String name)
	{
		return new ScriptException("No such env: " + name);
	}
	
	public static ScriptException EnvAlreadyExist(String name)
	{
		return new ScriptException("ENV already exist: " + name);
	}
	
	public static ScriptException NotInLoop()
	{
		return new ScriptException("Not in a loop");
	}
	
	public static Klink getDefault()
	{
		return DEFAULT;
	}
	
	private static final Klink DEFAULT = new Klink();
	
	private final Environment systemEnv = new Environment(null);
	
	private final ExpressionLibrary exprLibrary = new ExpressionLibrary();
	
	private final LinkedList<Boolean> loopFlags = new LinkedList<>();
	
	private Environment currentEnv;
	
	private final Map<String, Environment> env = new HashMap<>();
	
	private Messenger messenger;
	
	private static final Messenger NULL_MESSENGER = new Messenger() {
		@Override
		public void warn(String msg) {
		}

		@Override
		public void info(String msg) {
		}
	};
}