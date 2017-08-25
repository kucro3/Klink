package org.kucro3.klink.flow;

import org.kucro3.klink.Executable;
import org.kucro3.klink.Judgable;
import org.kucro3.klink.Klink;
import org.kucro3.klink.exception.BreakLoop;

public class LoopFor extends Loop {
	public LoopFor(Executable init, Judgable judge, Executable control, Executable body)
	{
		super(body);
		this.init = init;
		this.judge = judge;
		this.control = control;
	}
	
	@Override
	public void execute(Klink sys)
	{
		if(init != null)
			init.execute(sys);
		while(true) try {
			if(judge != null)
			{
				judge.execute(sys);
				if(!judge.passed())
					break;
			}
			super.execute(sys);
			if(control != null)
				control.execute(sys);
		} catch (BreakLoop e) {
			break;
		}
	}
	
	protected final Executable init;
	
	protected final Judgable judge;
	
	protected final Executable control;
}