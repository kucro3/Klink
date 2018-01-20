package org.kucro3.klink.functional;

public interface Parameter<T extends Type> {
    public T getType();

    public boolean isReference();

    public boolean isOptional();

    public String getName();
}
