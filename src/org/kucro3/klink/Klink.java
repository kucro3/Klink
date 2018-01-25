package org.kucro3.klink;

import java.io.File;
import java.util.*;

import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.expression.ExpressionPackLoader;
import org.kucro3.klink.syntax.KlinkTranslator;
import org.kucro3.klink.syntax.Sequence;

public class Klink {
	public Klink()
	{
	}
	
	public ExpressionLibrary getExpressions()
	{
		return exprLibrary;
	}
	
	public Optional<Environment> getEnv(String name)
	{
		return Optional.ofNullable(env.get(name));
	}
	
	public Environment requireEnv(String name)
	{
		Optional<Environment> env;
		if(!(env = getEnv(name)).isPresent())
			throw NoSuchEnv(name);
		return env.get();
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
		if(currentEnv == env.remove(name))
			currentEnv = systemEnv;
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
		if(name == null)
			currentEnv = systemEnv;
		else
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
		return compile(name, SequenceUtil.readFrom(file));
	}
	
	public Executable compile(Sequence seq)
	{
		return compile(seq.getName(), seq);
	}
	
	public Executable compile(String name, Sequence seq)
	{
		Translator translator = getTranslatorProviderWithLibrary().provide();
		translator.setGlobal(seq);
		return translator.pullAll();
	}

	public Executable execute(String filename)
	{
		return execute(filename, currentEnv());
	}
	
	public Executable execute(String filename, Environment env)
	{
		return execute(null, filename, env);
	}

	public Executable execute(File file)
	{
		return execute(file);
	}

	public Executable execute(File file, Environment env)
	{
		return execute(null, file, env);
	}

	public Executable execute(String name, String filename)
	{
		return execute(name, filename, currentEnv());
	}

	public Executable execute(String name, String filename, Environment env)
	{
		return execute(name, new File(filename), env);
	}

	public Executable execute(String name, File file)
	{
		return execute(name, file, currentEnv());
	}
	
	public Executable execute(String name, File file, Environment env)
	{
		return execute(name, SequenceUtil.readFrom(file), env);
	}

	public Executable execute(Sequence seq)
	{
		return execute(seq, currentEnv());
	}
	
	public Executable execute(Sequence seq, Environment env)
	{
		return execute(seq.getName(), seq);
	}

	public Executable execute(String name, Sequence seq)
	{
		return execute(name, seq, currentEnv());
	}

	public Executable execute(String name, Sequence seq, Environment env)
	{
		Executable exec = compile(name, seq);
		exec.execute(this, currentEnv());
		return exec;
	}
	
	public PackLoader getPackLoader()
	{
		return packloader;
	}
	
	public void setTranslatorProvider(TranslatorProvider provider)
	{
		this.translatorProvider = Objects.requireNonNull(provider);
	}
	
	public TranslatorProvider getTranslatorProvider()
	{
		return this.translatorProvider;
	}
	
	public TranslatorProvider getTranslatorProviderWithLibrary()
	{
		return () -> {
			Translator provided = this.translatorProvider.provide();
			provided.setLibrary(exprLibrary);
			return provided;
		};
	}
	
	public void setPackLoader(PackLoader loader)
	{
		packloader = Objects.requireNonNull(loader);
	}

	@SuppressWarnings("unchecked")
	public <T> Optional<T> getService(Class<T> type)
	{
		Objects.requireNonNull(type, "type");

		return Optional.ofNullable((T) services.get(type));
	}

	public <T> void provideService(Class<T> type, T service)
	{
		Objects.requireNonNull(type, "type");
		Objects.requireNonNull(service, "service");

		services.put(type, service);
	}

	public static ScriptException NoSuchEnv(String name)
	{
		return new ScriptException("No such env: " + name);
	}
	
	public static ScriptException EnvAlreadyExist(String name)
	{
		return new ScriptException("ENV already exist: " + name);
	}
	
	public static Klink getDefault()
	{
		return DEFAULT;
	}

	private TranslatorProvider translatorProvider = () -> new KlinkTranslator();

	private final Map<Class<?>, Object> services = new HashMap<>();
	
	private PackLoader packloader = ExpressionPackLoader.getInstance();
	
	private static final Klink DEFAULT = new Klink();
	
	private final Namespace namespace = new Namespace("default");
	
	private final Environment systemEnv = new Environment(null);
	
	private final ExpressionLibrary exprLibrary = new ExpressionLibrary();
	
	private Environment currentEnv = systemEnv;
	
	private final Map<String, Environment> env = new HashMap<>();
	
	private Messenger messenger = new NullMessenger();
	
	private static final Messenger NULL_MESSENGER = new NullMessenger();
	
	private static class NullMessenger implements Messenger
	{
		@Override
		public void warn(String msg) {
		}

		@Override
		public void info(String msg) {
		}
	}
}