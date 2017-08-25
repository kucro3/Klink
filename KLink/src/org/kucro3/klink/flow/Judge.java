package org.kucro3.klink.flow;

import org.kucro3.klink.Executable;
import org.kucro3.klink.Judgable;
import org.kucro3.klink.Klink;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.syntax.Lined;

public abstract class Judge implements Judgable {
	public Judge(Operation operation)
	{
		this.operation = operation;
	}
	
	@Override
	public void execute(Klink sys)
	{
		operation.execute(sys);
		try {
			result = sys.currentEnv().popBooleanSlot();
		} catch (ScriptException e) {
			if(this instanceof Lined)
				e.addLineInfo(((Lined) this).line());
			throw e;
		}
	}
	
	public static Judgable mute(final Judgable judgable)
	{
		return new Judgable() {
			@Override
			public void execute(Klink sys) 
			{
			}

			@Override
			public boolean passed() 
			{
				return judgable.passed();
			}
		};
	}
	
	public static Judgable checkJudgable(Executable executable)
	{
		if(!(executable instanceof Judgable))
			throw JudgableContextRequired();
		return (Judgable) executable;
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