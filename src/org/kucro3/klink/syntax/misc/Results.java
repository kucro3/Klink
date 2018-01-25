package org.kucro3.klink.syntax.misc;

public enum Results implements Result {
    SUCCEEDED("Succeeded", true),
    FAILED("Failed", false),
    SYNTAX_ERROR("Syntax error", false),
    PATTERN_MISMATCH("Pattern mismatch", false);

    private Results(String message, boolean passed)
    {
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
        return name();
    }

    @Override
    public boolean passed()
    {
        return passed;
    }

    private final boolean passed;

    private final String message;
}
