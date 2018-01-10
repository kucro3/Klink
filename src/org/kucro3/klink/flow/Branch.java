package org.kucro3.klink.flow;

import java.util.Objects;

import org.kucro3.klink.Executable;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Predicatable;

public class Branch implements Executable, Predicatable {
	public Branch(Predicatable judgable, Executable branch)
	{
		this.judgable = judgable;
		this.branch = Objects.requireNonNull(branch);
	}

	@Override
	public void execute(Klink sys)
	{
		judgable.execute(sys);
			
		if(passed())
			branch.execute(sys);
	}
	
	@Override
	public boolean passed() 
	{
		return judgable.passed();
	}
	
	protected final Predicatable judgable;
	
	protected final Executable branch;
}