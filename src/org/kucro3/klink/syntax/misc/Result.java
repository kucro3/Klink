package org.kucro3.klink.syntax.misc;

public interface Result {
    public String getMessage();

    public String getName();

    public boolean passed();

    public static SimpleResult of(String name, String message, boolean passed)
    {
        return new SimpleResult(name, message, passed);
    }
}
