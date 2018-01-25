package org.kucro3.klink.syntax.misc;

public class SimpleResult implements Result {
    SimpleResult(String name, String message, boolean passed)
    {
        this.name = name;
        this.message = message;
        this.passed = passed;
    }

    @Override
    public String getMessage()
    {
        return message;
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public boolean passed()
    {
        return passed;
    }

    private final String name;

    private final String message;

    private final boolean passed;
}
