package org.kucro3.klink.functional;

import org.kucro3.klink.exception.ScriptException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class FunctionRegistry<T extends Function> {
    protected FunctionRegistry()
    {
    }

    public boolean hasFunction(String identifier)
    {
        return map.containsKey(identifier);
    }

    public Optional<T> getFunction(String identifier)
    {
        return Optional.ofNullable(map.get(identifier));
    }

    public void putFunction(String identifier, T function)
    {
        map.put(identifier, function);
    }

    public void defineFunction(String identifier, T function)
    {
        if(hasFunction(identifier))
            throw FunctionRedefinition(identifier);
        putFunction(identifier, function);
    }

    public T requireFunction(String identifier)
    {
        Optional<T> optional = getFunction(identifier);
        if(!optional.isPresent())
            throw NoFunctionDefFound(identifier);
        return optional.get();
    }

    public static ScriptException FunctionRedefinition(String name)
    {
        return new ScriptException("Function redefinition: " + name);
    }

    public static ScriptException NoFunctionDefFound(String name)
    {
        return new ScriptException("No such function: " + name);
    }

    protected final Map<String, T> map = new HashMap<>();
}
