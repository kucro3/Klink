package org.kucro3.klink.functional;

public interface Type<T> {
    public T getType();

    public boolean isType(Object object);

    public String getName();
}
