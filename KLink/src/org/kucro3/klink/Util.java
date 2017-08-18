package org.kucro3.klink;

import org.kucro3.klink.exception.ScriptException;

public class Util {
	private Util()
	{
	}
	
	public static void checkVariable(Object[] obj, int required)
	{
		if(obj.length != required)
			throw VarNotCompatible(required, obj.length);
	}
	
	public static void checkVariable(Object[] obj, VariableChecker checker)
	{
		if(!checker.check(obj))
			throw VarNotCompatible();
	}
	
	public static boolean parseBoolean(String s)
	{
		return Boolean.parseBoolean(s);
	}
	
	public static long parseLong(String s)
	{
		try {
			return Long.parseLong(s);
		} catch (NumberFormatException e) {
			throw new ScriptException("Illegal number format: " + e.getMessage());
		}
	}
	
	public static int parseInt(String s)
	{
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			throw new ScriptException("Illegal number format: " + e.getMessage());
		}
	}
	
	public static short parseShort(String s)
	{
		try {
			return Short.parseShort(s);
		} catch (NumberFormatException e) {
			throw new ScriptException("Illegal number format: " + e.getMessage());
		}
	}
	
	public static byte parseByte(String s)
	{
		try {
			return Byte.parseByte(s);
		} catch (NumberFormatException e) {
			throw new ScriptException("Illegal number format: " + e.getMessage());
		}
	}
	
	public static float parseFloat(String s)
	{
		try {
			return Float.parseFloat(s);
		} catch (NumberFormatException e) {
			throw new ScriptException("Illegal number format: " + e.getMessage());
		}
	}
	
	public static double parseDouble(String s)
	{
		try {
			return Double.parseDouble(s);
		} catch (NumberFormatException e) {
			throw new ScriptException("Illegal number format: " + e.getMessage());
		}
	}
	
	public static ScriptException VarNotCompatible()
	{
		throw new ScriptException("Variable not compatible");
	}
	
	public static ScriptException VarNotCompatible(int requiredCount, int currentCount)
	{
		throw new ScriptException("Variable not compatible: " + currentCount + " provided, " + requiredCount + " required");
	}
	
	public static final Ref[] NULL_REFS = new Ref[0];
	
	public static interface VariableChecker
	{
		boolean check(Object[] refs);
	}
}