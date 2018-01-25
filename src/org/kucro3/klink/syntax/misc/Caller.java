package org.kucro3.klink.syntax.misc;

import org.kucro3.klink.Ref;
import org.kucro3.klink.Util;
import org.kucro3.klink.syntax.Sequence;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Caller implements Cloneable, Parser {
    public Caller(Vector argumentVector, Vector returnVector, String connector)
    {
        this.argumentVector = Objects.requireNonNull(argumentVector, "argumentVector").clone();
        this.returnVector = Objects.requireNonNull(returnVector, "returnVector").clone();
        this.connector = Objects.requireNonNull(connector, "connector");
    }

    @Override
    public Caller parse(Sequence seq)
    {
        reset();

        argumentVector.parse(seq);

        String temp;
        String[] contents;
        Ref[] parsed;

        PARSING_PARAMS:
        {
            if(argumentVector.getResult().equals(Results.PATTERN_MISMATCH))
            {
                List<String> readed = argumentVector.getLastReaded();
                if(readed.size() != 1)
                {
                    this.result = Results.SYNTAX_ERROR;
                    return this;
                }
                else if((temp = readed.get(0)).equals("->"))
                    break PARSING_PARAMS;
                else
                {
                    this.parsedFunctionName = temp;
                    this.result = Results.SUCCEEDED;
                    return this;
                }
            }

            if(!argumentVector.getResult().passed())
            {
                this.result = Results.FAILED;
                return this;
            }

            contents = argumentVector.getLastParsed();
            parsed = new Ref[contents.length];
            for(int i = 0; i < contents.length; i++)
                parsed[i] = Util.toRef(contents[i]);
            this.parsedArguments = parsed;

            if((temp = seq.next()).equals("->"))
                break PARSING_PARAMS;
            else
            {
                this.parsedFunctionName = temp;
                this.result = Results.SUCCEEDED;
                return this;
            }
        }

        returnVector.parse(seq);

        if(!returnVector.getResult().passed())
        {
            this.result = Results.FAILED;
            return this;
        }

        contents = returnVector.getLastParsed();
        parsed = new Ref[contents.length];
        for(int i = 0; i < contents.length; i++)
            parsed[i] = Util.toRef(contents[i]);
        this.parsedReturns = parsed;

        this.parsedFunctionName = seq.next();

        return this;
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

    public Ref[] getLastParsedArguments()
    {
        return parsedArguments;
    }

    public Ref[] getLastParsedReturns()
    {
        return parsedReturns;
    }

    public List<String> getLastReaded()
    {
        if(lazyLastReaded != null)
            return Collections.unmodifiableList(lazyLastReaded);

        ArrayList<String> list = new ArrayList<>();

        list.addAll(argumentVector.getLastReaded());

        if(argumentVector.getResult().passed() && returnVector.getResult() != null)
            list.addAll(returnVector.getLastReaded());

        list.trimToSize();
        return Collections.unmodifiableList(lazyLastReaded = list);
    }

    @Override
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

    @Override
    public Result getResult()
    {
        return result;
    }

    @Override
    public Caller clone()
    {
        return new Caller(argumentVector, returnVector, connector);
    }

    private Ref[] parsedReturns;

    private Ref[] parsedArguments;

    private String parsedFunctionName;

    private List<String> lazyLastReaded;

    private Result result;

    private final String connector;

    private final Vector argumentVector;

    private final Vector returnVector;
}
