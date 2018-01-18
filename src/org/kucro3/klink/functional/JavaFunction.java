package org.kucro3.klink.functional;

import org.kucro3.klink.Environment;

import java.util.List;

public class JavaFunction implements Function {
    @Override
    public String getIdentifier()
    {
        return null;
    }

    @Override
    public int getParameterCount()
    {
        return 0;
    }

    @Override
    public List<Parameter<JavaType>> getParameters()
    {
        return null;
    }

    @Override
    public List<Parameter<?>> getEssentialParameters()
    {
        return null;
    }

    @Override
    public List<Parameter<?>> getOptionalParameters()
    {
        return null;
    }

    @Override
    public void call(Environment env, Ref[] refs, CallInfo context) {

    }
}
