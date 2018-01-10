package org.kucro3.klink.flow;

public class PredicateIf extends Predicate {
	public PredicateIf(Operation operation)
	{
		super(operation);
	}

	@Override
	public boolean passed()
	{
		return super.result;
	}
}
