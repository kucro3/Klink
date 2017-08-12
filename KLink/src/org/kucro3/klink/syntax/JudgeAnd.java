package org.kucro3.klink.syntax;

public class JudgeAnd extends JudgeDual {
	public JudgeAnd(Judgable a, Judgable b) 
	{
		super(a, b);
	}

	@Override
	public boolean passed() 
	{
		return left.passed() & right.passed();
	}
}