package org.kucro3.klink.syntax;

import org.kucro3.klink.Klink;

public abstract class Judge implements Judgable {
	public Judge(Operation operation)
	{
		this.operation = operation;
	}
	
	@Override
	public void execute(Klink sys)
	{
		operation.execute(sys);
		result = sys.currentEnv().popBooleanSlot();
	}
	
	@Override
	public abstract boolean passed();
	
	protected final Operation operation;
	
	protected boolean result;
}