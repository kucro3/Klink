package org.kucro3.klink.syntax;

import org.kucro3.klink.Klink;

public class JudgeTrue implements Judgable {
	@Override
	public void execute(Klink sys)
	{
	}

	@Override
	public boolean passed() 
	{
		return true;
	}
}