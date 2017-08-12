package org.kucro3.klink;

import java.util.HashMap;
import java.util.Map;

import org.kucro3.klink.exception.ScriptException;

public class Variables {
	public Variables()
	{
	}
	
	public Variables(Variables parent)
	{
		this.parent = parent;
	}
	
	public void clearVars()
	{
		vars.clear();
	}
	
	public void removeVar(String name)
	{
		vars.remove(name);
	}
	
	public Variable newVar(String name)
	{
		if(hasVar(name))
			throw VariableRedefinition(name);
		Variable var = new Variable(name);
		vars.put(name, var);
		return var;
	}
	
	public Variable requireVar(String name)
	{
		Variable var;
		if((var = getVar(name)) == null)
			throw NoSuchVariable(name);
		return var;
	}
	
	public Variable getVar(String name)
	{
		return vars.get(name);
	}
	
	public void deleteVar(String name)
	{
		if(vars.remove(name) == null)
			throw NoSuchVariable(name);
	}
	
	public boolean hasVar(String name)
	{
		if(parent != null)
			if(parent.hasVar(name))
				return true;
		return vars.containsKey(name);
	}
	
	public Variables getParent()
	{
		return parent;
	}
	
	public static ScriptException NoSuchVariable(String name)
	{
		return new ScriptException("No such variable: " + name);
	}
	
	public static ScriptException VariableRedefinition(String name)
	{
		return new ScriptException("Variable redefinition: " + name);
	}
	
	Variables parent;
	
	private final Map<String, Variable> vars = new HashMap<>();
	
	public class Variable
	{
		public Variable(String name)
		{
			this.name = name;
		}
		
		public String getName()
		{
			return name;
		}
		
		private final String name;
		
		public Object ref;
	}
	
	public static class Var
	{
		public Var(String name)
		{
			this.name = name;
		}
		
		public String getName()
		{
			return name;
		}
		
		public Variable get(Variables vars)
		{
			return vars.getVar(name);
		}
		
		public Variable require(Variables vars)
		{
			return vars.requireVar(name);
		}
		
		public void remove(Variables vars)
		{
			vars.removeVar(name);
		}
		
		public void delete(Variables vars)
		{
			vars.deleteVar(name);
		}
		
		public Variable define(Variables vars)
		{
			return vars.newVar(name);
		}
		
		private final String name;
	}
}