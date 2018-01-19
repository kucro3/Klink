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
    public boolean hasVar(String name)
    {
        return vars.containsKey(name);
    }
}
