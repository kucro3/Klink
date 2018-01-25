package org.kucro3.klink.flow;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Predicatable;

public abstract class PredicateDual implements Predicatable {
	public PredicateDual(Predicatable left, Predicatable right)
	{
		this.left = left;
		this.right = right;
	}
	
	@Override
	public void execute(Klink sys, Environment env)
	{
		left.execute(sys, env);
		right.execute(sys, env);
	}
	
	@Override
	public abstract boolean passed();
	
	protected final Predicatable left, right;
}