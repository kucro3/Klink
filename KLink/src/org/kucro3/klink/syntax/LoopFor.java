package org.kucro3.klink.syntax;

import org.kucro3.klink.Klink;

public class LoopFor extends Loop {
	public LoopFor(Executable init, Judge judge, Executable control)
	{
		this.init = init;
		this.judge = judge;
		this.control = control;
	}
	
	@Override
	public void execute(Klink sys)
	{
		sys.pushLoop();
		if(init != null)
			init.execute(sys);
		while(sys.inLoop())
		{
			if(judge != null)
			{
				judge.execute(sys);
				if(!judge.passed())
					break;
			}
			super.execute(sys);
			if(control != null)
				control.execute(sys);
		}
		sys.popLoop();
	}
	
	protected final Executable init;
	
	protected final Judge judge;
	
	protected final Executable control;
}