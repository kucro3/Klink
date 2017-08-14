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
	
	public Variable newVarIfAbsent(String name)
	{
		Variable var = vars.get(name);
		if(var == null)
			vars.put(name, var = new DefaultVariable(name));
		return var;
	}
	
	public Variable newVar(String name)
	{
		if(hasVar(name))
			throw VariableRedefinition(name);
		Variable var = new DefaultVariable(name);
		vars.put(name, var);
		return var;
	}
	
	public void putVar(Variable var)
	{
		if(hasVar(var.getName()))
			throw VariableRedefinition(var.getName());
		vars.put(var.getName(), var);
	}
	
	public void forceVar(Variable var)
	{
		vars.put(var.getName(), var);
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
	
	public static interface Variable
	{
		public Object get();
		
		public void set(Object ref);
		
		public String getName();
	}
	
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
	
	public static class Var implements Ref
	{
		public Var(String name)
		{
			this.name = name;
		}
		
		public String getName()
		{
			return name;
		}
		
		public Variable require(Environment env)
		{
			return env.getVars().requireVar(name);
		}
		
		public void remove(Environment env)
		{
			env.getVars().removeVar(name);
		}
		
		public void delete(Environment env)
		{
			env.getVars().deleteVar(name);
		}
		
		public Variable define(Environment env)
		{
			return env.getVars().newVar(name);
		}
		
		@Override
		public Object get(Environment env)
		{
			return env.getVars().requireVar(name).get();
		}

		@Override
		public void set(Environment env, Object obj) 
		{
			env.getVars().requireVar(name).set(obj);
		}

		@Override
		public boolean isVar()
		{
			return true;
		}
		
		@Override
		public boolean isReg() 
		{
			return false;
		}
		
		@Override
		public void force(Environment env, Object obj) 
		{
			env.getVars().newVarIfAbsent(name).set(obj);
		}
		
		private final String name;
	}
}