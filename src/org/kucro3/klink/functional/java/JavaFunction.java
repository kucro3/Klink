package org.kucro3.klink.functional.java;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;
import org.kucro3.klink.functional.Function;
import org.kucro3.klink.functional.Parameter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class JavaFunction implements Function {
    public JavaFunction(String identifier, Method method, Parameter<JavaType>[] params)
    {
        this.identifier = identifier;
        this.method = method;
        this.params = Arrays.asList(params);
        this.essentialParams = new ArrayList<>();
        this.optionalParams = new ArrayList<>();

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
    public void call(Klink klink, Environment env, org.kucro3.klink.Ref[] refs)
    {

    }

    private final String identifier;

    private final Method method;

    private final List<Parameter<JavaType>> params;

    private final ArrayList<Parameter<JavaType>> essentialParams;

    private final ArrayList<Parameter<JavaType>> optionalParams;
}
