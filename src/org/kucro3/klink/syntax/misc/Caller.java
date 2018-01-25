package org.kucro3.klink.syntax.misc;

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

    private final String connector;

    private final Vector argumentVector;

    private final Vector returnVector;
}
