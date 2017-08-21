package org.kucro3.klink;

import java.io.File;
import java.util.*;

import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.syntax.Executable;
import org.kucro3.klink.syntax.Translator;

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
	
	public Namespace getNamespace()
	{
		return namespace;
	}
	
	public Executable compile(String filename)
	{
		return compile(null, filename);
	}
	
	public Executable compile(File file)
	{
		return compile(null, file);
	}
	
	public Executable compile(String name, String filename)
	{
		return compile(null, new File(filename));
	}
	
	public Executable compile(String name, File file)
	{
		return Translator.translate(this, SequenceUtil.readFrom(file), null);
	}
	
	public Executable execute(String filename)
	{
		return execute(null, filename);
	}
	
	public Executable execute(File file)
	{
		return execute(null, file);
	}
	
	public Executable execute(String name, String filename)
	{
		return execute(name, new File(filename));
	}
	
	public Executable execute(String name, File file)
	{
		Executable exec = compile(name, file);
		exec.execute(this);
		return exec;
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
	
	private final Namespace namespace = new Namespace("default");
	
	private final Environment systemEnv = new Environment(null);
	
	private final ExpressionLibrary exprLibrary = new ExpressionLibrary();
	
	private Environment currentEnv = systemEnv;
	
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