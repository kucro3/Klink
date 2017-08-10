package org.kucro3.klink.syntax;

public class Branch implements Executable {
	public Branch(Judgable judgable, Executable branch)
	{
		this.judgable = judgable;
		this.branch = branch;
	}

	@Override
	public void execute()
	{
		judgable.execute();
		if(judgable.passed())
			branch.execute();
	}
	
	protected final Judgable judgable;
	
	protected final Executable branch;
}