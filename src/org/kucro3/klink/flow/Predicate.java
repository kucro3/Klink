package org.kucro3.klink.flow;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Executable;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Predicatable;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.syntax.Lined;

public abstract class Predicate implements Predicatable {
	public Predicate(Operation operation)
	{
		this.operation = operation;
	}
	
	@Override
	public void execute(Klink sys, Environment env)
	{
		operation.execute(sys, env);
		try {
			result = sys.currentEnv().popBooleanSlot();
		} catch (ScriptException e) {
			if(this instanceof Lined)
				e.addLineInfo(((Lined) this).line());
			throw e;
		}
	}
	
	public static Predicatable mute(final Predicate judgable)
	{
		return new Predicatable() {
			@Override
			public void execute(Klink sys, Environment env)
			{
			}

			@Override
			public boolean passed()
			{
				return judgable.passed();
			}
		};
	}
	
	public static Predicate checkJudgable(Executable executable)
	{
		if(!(executable instanceof Predicate))
			throw JudgableContextRequired();
		return (Predicate) executable;
	}
	
	public static ScriptException JudgableContextRequired()
	{
		throw new ScriptException("Judgable context required");
	}
	
	@Override
	public abstract boolean passed();
	
	protected final Operation operation;
	
	protected boolean result;
}