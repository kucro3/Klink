package org.kucro3.klink.flow;

import org.kucro3.klink.Executable;
import org.kucro3.klink.Judgable;
import org.kucro3.klink.Klink;
import org.kucro3.klink.exception.BreakLoop;

public class LoopWhile extends Loop {
	public LoopWhile(Judgable judge, Executable body)
	{
		super(body);
		this.judge = judge;
	}
	
	@Override
	public void execute(Klink sys)
	{
		while(true) try {
			judge.execute(sys);
			if(!judge.passed())
				break;
			super.execute(sys);
		} catch (BreakLoop e) {
			break;
		}
	}
	
	protected final Judgable judge; 
}