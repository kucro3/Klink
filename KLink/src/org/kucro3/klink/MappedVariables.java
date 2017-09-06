package org.kucro3.klink;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.kucro3.klink.exception.ScriptException;

public class MappedVariables implements Variables {
	public MappedVariables()
	{
	}
	
	public MappedVariables(Variables parent)
	{
		this.parent = parent;
	}
	
	@Override
	public void clearVars()
	{
		vars.clear();
	}
	
	@Override
	public void removeVar(String name)
	{
		vars.remove(name);
	}
	
	@Override
	public Variable newVarIfAbsent(String name)
	{
		Variable var = vars.get(name);
		if(var == null)
			vars.put(name, var = new DefaultVariable(name));
		return var;
	}
	
	@Override
	public Variable newVar(String name)
	{
		if(hasVar(name))
			throw VariableRedefinition(name);
		Variable var = new DefaultVariable(name);
		vars.put(name, var);
		return var;
	}
	
	@Override
	public void putVar(Variable var)
	{
		if(hasVar(var.getName()))
			throw VariableRedefinition(var.getName());
		vars.put(var.getName(), var);
	}
	
	@Override
	public void forceVar(Variable var)
	{
		vars.put(var.getName(), var);
	}
	
	@Override
	public Variable requireVar(String name)
	{
		Variable var;
		if((var = getVar(name)) == null)
			throw NoSuchVariable(name);
		return var;
	}
	
	@Override
	public Variable getVar(String name)
	{
		return vars.get(name);
	}
	
	@Override
	public void deleteVar(String name)
	{
		if(vars.remove(name) == null)
			throw NoSuchVariable(name);
	}
	
	@Override
	public boolean hasVar(String name)
	{
		if(parent != null)
			if(parent.hasVar(name))
				return true;
		return vars.containsKey(name);
	}
	
	@Override
	public Variables getParent()
	{
		return parent;
	}
	
	@Override
	public Collection<Variable> getVars()
	{
		return vars.values();
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
	
	public static class DefaultVariable implements Variable
	{
		public DefaultVariable(String name)
		{
			this.name = name;
		}
		
		@Override
		public String getName()
		{
			return name;
		}
		
		@Override
		public Object get()
		{
			return ref;
		}
		
		@Override
		public void set(Object ref)
		{
			this.ref = ref;
		}
		
		private final String name;
		
		private Object ref;
	}
}