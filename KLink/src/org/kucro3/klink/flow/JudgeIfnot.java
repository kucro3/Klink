package org.kucro3.klink.flow;

public class JudgeIfnot extends Judge {
	public JudgeIfnot(Operation operation)
	{
		super(operation);
	}

	@Override
	public boolean passed() 
	{
		return !super.result;
	}
}