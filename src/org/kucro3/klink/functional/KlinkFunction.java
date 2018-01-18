package org.kucro3.klink.functional;

import org.kucro3.klink.Environment;
import org.kucro3.klink.flow.Flow;

import java.util.List;

public class KlinkFunction implements Function {
    public KlinkFunction(Flow flow)
    {
        this.flow = flow;
    }

    @Override
    public void call(Environment env, Ref[] refs, CallInfo context)
    {

    }

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
    public <T extends Type> List<Parameter<T>> getParameters()
    {
        return null;
    }

    @Override
    public <T extends Type> List<Parameter<T>> getEssentialParameters()
    {
        return null;
    }

    @Override
    public <T extends Type> List<Parameter<T>> getOptionalParameters()
    {
        return null;
    }

    private final Flow flow;
}
