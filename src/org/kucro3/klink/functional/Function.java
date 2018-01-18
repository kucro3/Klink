package org.kucro3.klink.functional;

import java.util.List;

public interface Function extends Callable {
    public String getIdentifier();

    public int getParameterCount();

    public List<Parameter> getParameters();

    public List<Parameter> getEssentialParameters();

    public List<Parameter> getOptionalParameters();
}
