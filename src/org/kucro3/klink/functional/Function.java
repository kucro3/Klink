package org.kucro3.klink.functional;

import java.util.List;

public interface Function extends Callable {
    public String getIdentifier();

    public int getParameterCount();

    public <T extends Type> List<Parameter<T>> getParameters();

    public <T extends Type> List<Parameter<T>> getEssentialParameters();

    public <T extends Type> List<Parameter<T>> getOptionalParameters();
}
