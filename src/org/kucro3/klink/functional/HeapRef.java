package org.kucro3.klink.functional;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Ref;

public class HeapRef implements Ref {
    public HeapRef(String name)
    {
        this.name = name;
    }

    public HeapRef(String name, Object obj)
    {
        this(name);
        this.object = obj;
    }

    @Override
    public Object get(Environment env)
    {
        return object;
    }

    @Override
    public void set(Environment env, Object obj)
    {
        force(env, obj);
    }

    @Override
    public void force(Environment env, Object obj)
    {
        this.object = obj;
    }

    @Override
    public boolean isVar()
    {
        return false;
    }

    @Override
    public boolean isReg()
    {
        return false;
    }

    @Override
    public String getName()
    {
        return name;
    }

    private final String name;

    private Object object;
}
