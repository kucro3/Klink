package org.kucro3.klink.flow;

import java.util.Optional;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Executable;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Predicatable;
import org.kucro3.klink.exception.BreakLoop;

public class LoopFor extends Loop {
	public LoopFor(Optional<Executable> init, Predicatable judge, Optional<Executable> control, Executable body)
	{
		super(body);
		this.init = init;
		this.judge = judge;
		this.control = control;
	}
	
	@Override
	public void execute(Klink sys, Environment env)
	{
		init.ifPresent((e) -> e.execute(sys, env));
		while(true) try {
			judge.execute(sys, env);
			if(!judge.passed())
				break;
			super.execute(sys, env);
			control.ifPresent((e) -> e.execute(sys, env));
		} catch (BreakLoop e) {
			break;
		}
	}
	
	protected final Optional<Executable> init;
	
	protected final Predicatable judge;
	
	protected final Optional<Executable> control;
}