package org.kucro3.klink.flow;

import java.util.Optional;

import org.kucro3.klink.Executable;
import org.kucro3.klink.Judgable;
import org.kucro3.klink.Klink;
import org.kucro3.klink.exception.BreakLoop;

public class LoopFor extends Loop {
	public LoopFor(Optional<Executable> init, Judgable judge, Optional<Executable> control, Executable body)
	{
		super(body);
		this.init = init;
		this.judge = judge;
		this.control = control;
	}
	
	@Override
	public void execute(Klink sys)
	{
		init.ifPresent((e) -> e.execute(sys));
		while(true) try {
			judge.execute(sys);
			if(!judge.passed())
				break;
			super.execute(sys);
			control.ifPresent((e) -> e.execute(sys));
		} catch (BreakLoop e) {
			break;
		}
	}
	
	protected final Optional<Executable> init;
	
	protected final Judgable judge;
	
	protected final Optional<Executable> control;
}