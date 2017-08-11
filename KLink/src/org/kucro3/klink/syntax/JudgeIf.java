package org.kucro3.klink.syntax;

public class JudgeIf extends Judge {
	public JudgeIf(Operation operation)
	{
		super(operation);
	}

	@Override
	public boolean passed()
	{
		return super.result;
	}
}
