package org.kucro3.klink.syntax;

import org.kucro3.klink.Klink;
import org.kucro3.klink.exception.ScriptException;

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
	
	@Override
	public abstract boolean passed();
	
	protected final Operation operation;
	
	protected boolean result;
}