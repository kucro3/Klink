package org.kucro3.klink.syntax;

public class SyntaxIfnot extends Judge {
	public SyntaxIfnot(Operation operation)
	{
		super(operation);
	}

	@Override
	public boolean passed() 
	{
		return !super.result;
	}
}