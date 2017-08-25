package org.kucro3.klink.flow;

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
