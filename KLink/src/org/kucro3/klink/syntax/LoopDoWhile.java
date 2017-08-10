package org.kucro3.klink.syntax;

import org.kucro3.klink.Klink;

public class LoopDoWhile extends Loop {
	public LoopDoWhile(Judge judge)
	{
		this.judge = judge;
	}
	
	@Override
	public void execute(Klink sys)
	{
		sys.pushLoop();
		judge.execute(sys);
		while(sys.inLoop())
		{
			if(judge != null)
			{
				judge.execute(sys);
				if(!judge.passed())
					break;
			}
			super.execute(sys);
		}
		sys.popLoop();
	}
	
	protected final Judge judge; 
}