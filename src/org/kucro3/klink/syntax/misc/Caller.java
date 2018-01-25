package org.kucro3.klink.syntax.misc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Caller {
    public Caller(Vector argumentVector, Vector returnVector, String connector)
    {
        this.argumentVector = Objects.requireNonNull(argumentVector, "argumentVector");
        this.returnVector = Objects.requireNonNull(returnVector, "returnVector");
        this.connector = Objects.requireNonNull(connector, "connector");
    }

    public String getConnector()
    {
        return connector;
    }

    public Caller setConnector(String connector)
    {
        return new Caller(argumentVector, returnVector, connector);
    }

    public Vector getArgumentVector()
    {
        return argumentVector;
    }

    public Caller setArgumentVector(Vector argumentVector)
    {
        return new Caller(argumentVector, returnVector, connector);
    }

    public Vector getReturnVector()
    {
        return returnVector;
    }

    public Caller setReturnVector(Vector returnVector)
    {
        return new Caller(argumentVector, returnVector, connector);
    }

    public Caller set(Vector argumentVector, Vector returnVector)
    {
        return set(argumentVector, returnVector, connector);
    }

    public Caller set(Vector argumentVector, Vector returnVector, String connector)
    {
        return new Caller(argumentVector, returnVector, connector);
    }

    public String getLastParsedFunctionName()
    {
        return parsedFunctionName;
    }

    public String[] getLastParsedArguments()
    {
        return parsedArguments;
    }

    public String[] getLastParsedReturns()
    {
        return parsedReturns;
    }

    public List<String> getLastReaded()
    {
        ArrayList<String> list = new ArrayList<>();

        list.addAll(argumentVector.getLastReaded());

        if(argumentVector.getResult().passed())
            list.addAll(returnVector.getLastReaded());

        list.trimToSize();
        return Collections.unmodifiableList(list);
    }

    public void reset()
    {
        parsedArguments = null;
        parsedReturns = null;
        parsedFunctionName = null;
        lazyLastReaded = null;
        result = null;

        argumentVector.reset();
        returnVector.reset();
    }

    public Result getResult()
    {
        return result;
    }

    private String[] parsedReturns;

    private String[] parsedArguments;

    private String parsedFunctionName;

    private List<String> lazyLastReaded;

    private Result result;

    private final String connector;

    private final Vector argumentVector;

    private final Vector returnVector;
}
