package org.kucro3.klink;

import org.kucro3.klink.exception.ScriptException;

public class Registers {
	public final Object OR[] = new Object[16];
	
	public final byte I08[] = new byte[6];
	
	public final short I16[] = new short[6];
	
	public final int I32[] = new int[6];
	
	public final long I64[] = new long[6];
	
	public final float F32[] = new float[6];
	
	public final double F64[] = new double[6];
	
	public static int checkRange(int index)
	{
		if(index <= 5 && index >= 0)
			return index;
		throw RegisterSetOutOfRange(index);
	}
	
	public static int checkRangeForOR(int index)
	{
		if(index <= 15 && index >= 0)
			return index;
		throw RegisterSetOutOfRange(index);
	}
	
	public static ScriptException RegisterSetOutOfRange(int index)
	{
		throw new ScriptException("Register set out of range: " + index);
	}
	
	public interface Adapter
	{
		Object at(Registers regs, int index);
		
		void at(Registers regs, int index, Object obj);
	}
	
	public static class AdapterForOR implements Adapter
	{
		@Override
		public Object at(Registers regs, int index) 
		{
			return regs.OR[checkRangeForOR(index)];
		}

		@Override
		public void at(Registers regs, int index, Object obj) 
		{
			regs.OR[checkRangeForOR(index)] = obj;
		}
	}
	
	public static class Register implements Ref
	{
		public Register(Adapter adapter, int index)
		{
			this.adapter = adapter;
			this.index = index;
		}
		
		@Override
		public Object get(Environment env) 
		{
			return adapter.at(env.getRegisters(), index);
		}

		@Override
		public void set(Environment env, Object obj)
		{
			force(env, obj);
		}
		
		@Override
		public void force(Environment env, Object obj)
		{
			adapter.at(env.getRegisters(), index, obj);
		}

		@Override
		public boolean isVar() 
		{
			return false;
		}

		@Override
		public boolean isReg() 
		{
			return true;
		}
		
		private final int index;
		
		private final Adapter adapter;
	}
}