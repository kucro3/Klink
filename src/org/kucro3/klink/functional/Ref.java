package org.kucro3.klink.functional;

public interface Ref<T> {
    public void set(T value);

    public T get();
}
