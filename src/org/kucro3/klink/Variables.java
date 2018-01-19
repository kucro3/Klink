package org.kucro3.klink;

import org.kucro3.klink.exception.ScriptException;

import java.util.Collection;
import java.util.Optional;

public interface Variables {
	public void clearVars();

	public void clearAllVars();
	
	public void deleteVar(String name);
	
	public void forceVar(Variable var);
	
	public Optional<Variables> getParent();
	
	public Optional<Variable> getVar(String name);
	
	public Collection<Variable> getVars();
	
	public boolean hasVar(String name);
	
	public Variable newVar(String name);
	
	public Variable newVarIfAbsent(String name);
	
	public void putVar(Variable var);
	
	public boolean removeVar(String name);
	
	public Variable requireVar(String name);

	public static ScriptException NoSuchVariable(String name)
	{
		return new ScriptException("No such variable: " + name);
	}

	public static ScriptException VariableRedefinition(String name)
	{
		return new ScriptException("Variable redefinition: " + name);
	}
	
	public static interface Variable
	{
		public Object get();
		
		public void set(Object ref);
		
		public String getName();
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