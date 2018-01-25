package org.kucro3.klink.flow;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Executable;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Predicatable;
import org.kucro3.klink.exception.BreakLoop;

public class LoopWhile extends Loop {
	public LoopWhile(Predicatable judge, Executable body)
	{
		super(body);
		this.judge = judge;
	}
	
	@Override
	public void execute(Klink sys, Environment env)
	{
		while(true) try {
			judge.execute(sys, env);
			if(!judge.passed())
				break;
			super.execute(sys, env);
		} catch (BreakLoop e) {
			break;
		}
	}
	
	protected final Predicatable judge;
}