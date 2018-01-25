package org.kucro3.klink.flow;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;
import org.kucro3.klink.Predicatable;

public class PredicateTrue implements Predicatable {
	@Override
	public void execute(Klink sys, Environment env)
	{
	}

	@Override
	public boolean passed() 
	{
		return true;
	}
}