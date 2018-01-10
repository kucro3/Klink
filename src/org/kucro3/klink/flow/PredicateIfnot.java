package org.kucro3.klink.flow;

public class PredicateIfnot extends Predicate {
	public PredicateIfnot(Operation operation)
	{
		super(operation);
	}

	@Override
	public boolean passed() 
	{
		return !super.result;
	}
}