package org.kucro3.klink.functional.java;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.functional.CallInfo;
import org.kucro3.klink.functional.Function;
import org.kucro3.klink.functional.Parameter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class JavaFunction implements Function {
    public JavaFunction(String identifier, Method method, Parameter<JavaType>[] params, boolean frontCallInfo)
    {
        this.identifier = identifier;
        this.method = method;
        this.params = Arrays.asList(params);
        this.essentialParams = new ArrayList<>();
        this.optionalParams = new ArrayList<>();
        this.frontCallInfo = frontCallInfo;

        int i = 0;
        for(; i < params.length && !params[i].isOptional(); i++)
            essentialParams.add(params[i]);
        for(; i < params.length; i++)
            optionalParams.add(params[i]);

        this.essentialParams.trimToSize();
        this.optionalParams.trimToSize();
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
    public List<Parameter<JavaType>> getParameters()
    {
        return Collections.unmodifiableList(params);
    }

    @Override
    public List<Parameter<JavaType>> getEssentialParameters()
    {
        return Collections.unmodifiableList(essentialParams);
    }

    @Override
    public List<Parameter<JavaType>> getOptionalParameters()
    {
        return Collections.unmodifiableList(optionalParams);
    }

    @Override
    public void call(Klink klink, Environment env, org.kucro3.klink.Ref[] refs, CallInfo callInfo)
    {
        if (refs.length < essentialParams.size())
            throw Function.NeedMoreArguments();

        if (refs.length > params.size())
            throw Function.TooManyArguments();

        int i = frontCallInfo ? 1 : 0;
        Object[] arguments = new Object[params.size() + 1];

        for (; i < refs.length; i++)
            if (!params.get(i).getType().isType(refs[i].get(env)))
                throw Function.IncompatibleArgumentType(params.get(i).getType().getName(), i);
            else
                arguments[i] = refs[i].get(env);

        for(; i < arguments.length; i++)
            arguments[i] = null;

        arguments[frontCallInfo ? 0 : arguments.length - 1] = callInfo;

        try {
            method.invoke(null, arguments);
        } catch (Exception e) {
            throw new ScriptException("Invocation failure", e);
        }
    }

    private final String identifier;

    private final Method method;

    private final List<Parameter<JavaType>> params;

    private final ArrayList<Parameter<JavaType>> essentialParams;

    private final ArrayList<Parameter<JavaType>> optionalParams;

    private final boolean frontCallInfo;
}
