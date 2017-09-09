package org.kucro3.klink;

import org.kucro3.klink.Variables.Variable;

public interface VariableFactory {
	public Variable produce(String name);
}
