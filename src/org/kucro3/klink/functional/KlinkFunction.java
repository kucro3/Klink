package org.kucro3.klink.functional;

import org.kucro3.klink.Environment;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.flow.Flow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class KlinkFunction implements Function {
    public KlinkFunction(Flow flow, String identifier, Parameter[] params)
    {
        this.flow = flow;
        this.identifier = identifier;

        this.params = Arrays.asList(params);

        this.essentialParams = new ArrayList<>();
        this.optionalParams = new ArrayList<>();

        int i = 0;

        for(; i < params.length; i++)
            if(params[i].isOptional())
                break;
            else
                essentialParams.add(params[i]);

        for(; i < params.length; i++)
            if(!params[i].isOptional())
                throw new ScriptException("Illegal argument declaration");
            else
                optionalParams.add(params[i]);

        this.essentialParams.trimToSize();
        this.optionalParams.trimToSize();
    }

    @Override
    public void call(Environment env, Ref[] refs, CallInfo context)
    {
        // TODO
    }

    @Override
    public String getIdentifier()
    {
        return identifier;
    }

    @Override
    public int getParameterCount()
    {
        return params.size();
    }

    @Override
    public <T extends Type> List<Parameter<T>> getParameters()
    {
        return Collections.unmodifiableList(params);
    }

    @Override
    public <T extends Type> List<Parameter<T>> getEssentialParameters()
    {
        return Collections.unmodifiableList(essentialParams);
    }

    @Override
    public <T extends Type> List<Parameter<T>> getOptionalParameters()
    {
        return Collections.unmodifiableList(optionalParams);
    }

    private final String identifier;

    private final List<Parameter> params;

    private final ArrayList<Parameter> essentialParams;

    private final ArrayList<Parameter> optionalParams;

    private final Flow flow;
}
