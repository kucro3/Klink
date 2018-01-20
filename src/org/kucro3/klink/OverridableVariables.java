package org.kucro3.klink;

public class OverridableVariables extends MappedVariables {
    public OverridableVariables()
    {
    }

    public OverridableVariables(VariableFactory factory)
    {
        super(factory);
    }

    public OverridableVariables(VariableFactory factory, Variables parent)
    {
        super(factory, parent);
    }

    public OverridableVariables(Variables parent)
    {
        super(parent);
    }

    @Override
    public void putVar(Variable var)
    {
        if(vars.containsKey(var.getName()))
            throw Variables.VariableRedefinition(var.getName());
        vars.put(var.getName(), var);
    }

    @Override
    public Variable newVar(String name)
    {
        if(vars.containsKey(name))
            throw Variables.VariableRedefinition(name);
        Variable var = super.factory.produce(name);
        vars.put(name,var);
        return var;
    }

    @Override
    public Variable newVarIfAbsent(String name)
    {
        Variable var = vars.get(name);
        if(var == null)
            vars.put(name, var = factory.produce(name));
        return var;
    }
}
