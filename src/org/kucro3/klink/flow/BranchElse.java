package org.kucro3.klink.flow;

import org.kucro3.klink.Executable;
import org.kucro3.klink.Predicatable;

public class BranchElse extends Branch {
	public BranchElse(Executable last, Executable exec)
	{
		super(Predicate.mute(Predicate.checkJudgable(last)), exec);
	}
	
	public BranchElse(Executable last, Predicatable judgable, Executable exec)
	{
		super(new PredicateAnd(Predicate.mute(Predicate.checkJudgable(last)), judgable), exec);
	}
	
	@Override
	public boolean passed()
	{
		return !judgable.passed();
	}
}