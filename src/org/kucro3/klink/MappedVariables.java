package org.kucro3.klink;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.kucro3.klink.exception.ScriptException;

public class MappedVariables implements Variables {
	public MappedVariables()
	{
		this(DefaultVariable::new);
	}
	
	public MappedVariables(VariableFactory factory)
	{
		this(factory, null);
	}
	
	public MappedVariables(Variables parent)
	{
		this(DefaultVariable::new, parent);
	}
	
	public MappedVariables(VariableFactory factory, Variables parent)
	{
		this.factory = factory;
		this.parent = parent;
	}
	
	@Override
	public void clearVars()
	{
		vars.clear();
	}
	
	@Override
	public boolean removeVar(String name)
	{
		if(vars.remove(name) == null)
			if(parent == null || !parent.removeVar(name))
				return false;
		return true;
	}
	
	@Override
	public Variable newVarIfAbsent(String name)
	{
		Variable var = vars.get(name);
		if(var == null)
			vars.put(name, var = factory.produce(name));
		return var;
	}
	
	@Override
	public Variable newVar(String name)
	{
		if(hasVar(name))
			throw Variables.VariableRedefinition(name);
		Variable var = factory.produce(name);
		vars.put(name, var);
		return var;
	}
	
	@Override
	public void putVar(Variable var)
	{
		if(hasVar(var.getName()))
			throw Variables.VariableRedefinition(var.getName());
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
		Optional<Variable> var;
		if(!(var = getVar(name)).isPresent())
			throw Variables.NoSuchVariable(name);
		return var.get();
	}
	
	@Override
	public Optional<Variable> getVar(String name)
	{
		Variable var = null;
		if(parent != null)
			var = parent.getVar(name).orElse(null);
		if(var == null)
			var = vars.get(name);
		return Optional.ofNullable(var);
	}
	
	@Override
	public void deleteVar(String name)
	{
		if(vars.remove(name) == null)
			if(parent == null)
				throw Variables.NoSuchVariable(name);
			else
				parent.deleteVar(name);
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
	public void clearAllVars()
	{
		if(parent != null)
			parent.clearAllVars();
		vars.clear();
	}
	
	@Override
	public Optional<Variables> getParent()
	{
		return Optional.ofNullable(parent);
	}
	
	@Override
	public Collection<Variable> getVars()
	{
		return vars.values();
	}

	Variables parent;
	
	protected final VariableFactory factory;
	
	protected final Map<String, Variable> vars = new HashMap<>();
	
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