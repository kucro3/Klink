package org.kucro3.klink.syntax;

import org.kucro3.klink.Klink;

public abstract class Loop implements Executable {
	public Loop(Executable body)
	{
		this.body = body;
	}
	
	@Override
	public void execute(Klink sys)
	{
		if(body != null)
			body.execute(sys);
	}
	
	protected final Executable body;
}