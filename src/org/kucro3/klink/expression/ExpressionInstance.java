package org.kucro3.klink.expression;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;

public interface ExpressionInstance {
	public void call(Klink sys, Environment env);
}