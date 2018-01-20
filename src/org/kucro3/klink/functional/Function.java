package org.kucro3.klink.functional;

import org.kucro3.klink.exception.ScriptException;

import java.util.List;

public interface Function extends Callable {
    public String getIdentifier();

    public int getParameterCount();

    public <T extends Type> List<Parameter<T>> getParameters();

    public <T extends Type> List<Parameter<T>> getEssentialParameters();

    public <T extends Type> List<Parameter<T>> getOptionalParameters();

    public static ScriptException NeedMoreArguments()
    {
        return new ScriptException("Need more arguments");
    }

    public static ScriptException TooManyArguments()
    {
        return new ScriptException("Too many arguments");
    }

    public static ScriptException IncompatibleArgumentType(String name, int index)
    {
        return new ScriptException("Incompatible argument type at index " + index + " (Type of \" " + name + " \" needed)");
    }
}
