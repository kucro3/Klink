package org.kucro3.klink;

import org.kucro3.klink.Variables.Variable;
import org.kucro3.klink.exception.ScriptException;

public class Environment {
	public Environment(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return name;
	}
	
	public Source getSource()
	{
		return source;
	}
	
	public void setSource(Source source)
	{
		this.source = source;
	}
	
	public void setVarSlot(Variable[] var)
	{
		this.varSlot = var;
	}
	
	public Variable[] getVarSlot()
	{
		return this.varSlot;
	}
	
	public Variable[] popVarSlot()
	{
		Variable[] var = this.varSlot;
		this.varSlot = null;
		return var;
	}
	
	public Object getReturnSlot()
	{
		if(returnSlot == NULL)
			throw NoObjectReturned();
		return returnSlot;
	}
	
	public Object popReturnSlot()
	{
		if(returnSlot == NULL)
			throw NoObjectReturned();
		Object ret = returnSlot;
		returnSlot = NULL;
		return ret;
	}
	
	public void setReturnSlot(Object obj)
	{
		this.returnSlot = obj;
	}
	
	public boolean getBooleanSlot()
	{
		if(this.booleanSlot == null)
			throw UnknownBooleanState();
		return this.booleanSlot;
	}
	
	public boolean popBooleanSlot()
	{
		if(this.booleanSlot == null)
			throw UnknownBooleanState();
		boolean bool = this.booleanSlot;
		this.booleanSlot = null;
		return bool;
	}
	
	public void setBooleanSlot(boolean bool)
	{
		this.booleanSlot = bool;
	}
	
	public Variables getVars()
	{
		return varRoot;
	}
	
	public void setVars(Variables vars)
	{
		this.varRoot = vars;
	}
	
	public Variables pushVars()
	{
		Variables child = new MappedVariables(varRoot);
		this.varRoot = child;
		return child;
	}
	
	public Variables popVars()
	{
		if(varRoot.getParent() == null)
			throw VarsStackUnderflow();
		this.varRoot = varRoot.getParent().orElse(null);
		return this.varRoot;
	}
	
	public Registers getRegisters()
	{
		return regs;
	}
	
	public void setRegisters(Registers regs)
	{
		this.regs = regs;
	}
	
	public static ScriptException VarsStackUnderflow()
	{
		return new ScriptException("Variable container stack underflow");
	}
	
	public static ScriptException UnknownBooleanState()
	{
		return new ScriptException("Unknown boolean state");
	}
	
	public static ScriptException NoObjectReturned()
	{
		return new ScriptException("No object returned");
	}
	
	private Registers regs = new Registers();
	
	private Variable[] varSlot;
	
	private Object returnSlot;
	
	private Boolean booleanSlot;
	
	private Variables varRoot = new MappedVariables();
	
	private Source source = new SystemSource();
	
	private final String name;
	
	private static final Object NULL = new Object();
}