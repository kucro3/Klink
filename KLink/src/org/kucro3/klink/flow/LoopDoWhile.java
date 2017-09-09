package org.kucro3.klink.flow;

import org.kucro3.klink.Executable;
import org.kucro3.klink.Judgable;
import org.kucro3.klink.Klink;
import org.kucro3.klink.exception.BreakLoop;

public class LoopDoWhile extends Loop {
	public LoopDoWhile(Judgable judge, Executable body)
	{
		super(body);
		this.judge = judge;
	}
	
	@Override
	public void execute(Klink sys)
	{
		try {
			super.execute(sys);
			while(true)
			{
				judge.execute(sys);
				if(!judge.passed())
					break;
				super.execute(sys);
			}
		} catch (BreakLoop e) {
		}
	}
	
	protected final Judgable judge; 
}