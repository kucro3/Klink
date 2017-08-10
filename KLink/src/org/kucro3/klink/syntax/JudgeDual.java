package org.kucro3.klink.syntax;

public abstract class JudgeDual implements Judgable {
	public JudgeDual(Judgable a, Judgable b)
	{
		this.a = a;
		this.b = b;
	}
	
	@Override
	public void execute()
	{
		a.execute();
		b.execute();
	}
	
	@Override
	public abstract boolean passed();
	
	protected final Judgable a;
	
	protected final Judgable b;
}