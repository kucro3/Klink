package org.kucro3.klink;

import org.kucro3.klink.exception.ScriptException;

public class Util {
	private Util()
	{
	}
	
	public static void checkVariable(Object[] obj, int required)
	{
		if(obj.length != required)
			throw ScriptException.VarNotCompatible(required, obj.length);
	}
}