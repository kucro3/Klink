package org.kucro3.klink.flow;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Executable;
import org.kucro3.klink.Klink;

public abstract class Loop implements Executable {
	public Loop(Executable body)
	{
		this.body = body;
	}
	
	@Override
	public void execute(Klink sys, Environment env)
	{
		body.execute(sys, env);
	}
	
	protected final Executable body;
}