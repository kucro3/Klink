package org.kucro3.klink.syntax;

import org.kucro3.klink.Klink;
import org.kucro3.klink.expression.Expression.ReturnType;

public abstract class Judge implements Judgable {
	public Judge(Operation operation)
	{
		if(!operation.getExpression().getReturnType().equals(ReturnType.BOOLEAN))
			throw new RuntimeException("Should not reach here");
		
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