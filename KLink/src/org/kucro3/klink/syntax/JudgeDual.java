package org.kucro3.klink.syntax;

import org.kucro3.klink.Klink;

public abstract class JudgeDual implements Judgable {
	public JudgeDual(Judgable a, Judgable b)
	{
		this.a = a;
		this.b = b;
	}
	
	@Override
	public void execute(Klink sys)
	{
		a.execute(sys);
		b.execute(sys);
	}
	
	@Override
	public abstract boolean passed();
	
	protected final Judgable a;
	
	protected final Judgable b;
}