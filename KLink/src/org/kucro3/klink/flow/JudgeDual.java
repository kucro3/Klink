package org.kucro3.klink.flow;

import org.kucro3.klink.Judgable;
import org.kucro3.klink.Klink;

public abstract class JudgeDual implements Judgable {
	public JudgeDual(Judgable left, Judgable right)
	{
		this.left = left;
		this.right = right;
	}
	
	@Override
	public void execute(Klink sys)
	{
		left.execute(sys);
		right.execute(sys);
	}
	
	@Override
	public abstract boolean passed();
	
	protected final Judgable left, right;
}