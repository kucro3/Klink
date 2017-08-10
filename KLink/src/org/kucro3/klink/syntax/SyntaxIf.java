package org.kucro3.klink.syntax;

public class SyntaxIf extends Judge {
	public SyntaxIf(Operation operation)
	{
		super(operation);
	}

	@Override
	public boolean passed()
	{
		return super.result;
	}
}
