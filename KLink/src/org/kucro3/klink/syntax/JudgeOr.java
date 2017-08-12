package org.kucro3.klink.syntax;

public class JudgeOr extends JudgeDual {
	public JudgeOr(Judgable a, Judgable b)
	{
		super(a, b);
	}

	@Override
	public boolean passed()
	{
		return left.passed() | right.passed();
	}
}