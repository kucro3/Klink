package org.kucro3.klink.flow;

import org.kucro3.klink.Executable;
import org.kucro3.klink.Judgable;

public class BranchElse extends Branch {
	public BranchElse(Executable last, Executable exec)
	{
		super(Judge.mute(Judge.checkJudgable(last)), exec);
	}
	
	public BranchElse(Executable last, Judgable judgable, Executable exec)
	{
		super(new JudgeAnd(Judge.mute(Judge.checkJudgable(last)), judgable), exec);
	}
	
	@Override
	public boolean passed()
	{
		return !judgable.passed();
	}
}