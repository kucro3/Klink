package org.kucro3.klink.syntax;

import org.kucro3.klink.Klink;

public class Branch implements Executable {
	public Branch(Judgable judgable, Executable branch)
	{
		this.judgable = judgable;
		this.branch = branch;
	}

	@Override
	public void execute(Klink sys)
	{
		judgable.execute(sys);
			
		if(judgable.passed())
			if(branch != null)
				branch.execute(sys);
	}
	
	protected final Judgable judgable;
	
	protected final Executable branch;
}