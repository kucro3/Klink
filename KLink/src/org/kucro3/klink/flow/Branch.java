package org.kucro3.klink.flow;

import org.kucro3.klink.Executable;
import org.kucro3.klink.Judgable;
import org.kucro3.klink.Klink;

public class Branch implements Executable, Judgable {
	public Branch(Judgable judgable, Executable branch)
	{
		this.judgable = judgable;
		this.branch = branch;
	}

	@Override
	public void execute(Klink sys)
	{
		judgable.execute(sys);
			
		if(passed())
			if(branch != null)
				branch.execute(sys);
	}
	
	@Override
	public boolean passed() 
	{
		return judgable.passed();
	}
	
	protected final Judgable judgable;
	
	protected final Executable branch;
}