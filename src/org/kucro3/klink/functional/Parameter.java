package org.kucro3.klink.functional;

public interface Parameter<T> {
    public Type<T> getType();

    public boolean isReference();

    public boolean isOptional();
}
