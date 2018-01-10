package org.kucro3.klink.flow;

import org.kucro3.klink.Predicatable;

public class PredicateOr extends PredicateDual {
	public PredicateOr(Predicatable a, Predicatable b)
	{
		super(a, b);
	}

	@Override
	public boolean passed()
	{
		return left.passed() | right.passed();
	}
}