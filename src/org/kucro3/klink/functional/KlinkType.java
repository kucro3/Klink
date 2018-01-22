package org.kucro3.klink.functional;

@Typename("Java")
public class KlinkType implements Type<Class<?>> {
    public KlinkType(Class<?> type)
    {
        this.type = type;
    }

    @Override
    public Class<?> getType()
    {
        return type;
    }

    @Override
    public boolean isType(Object object)
    {
        return type.isInstance(object);
    }

    @Override
    public String getName()
    {
        return type.getCanonicalName();
    }

    private final Class<?> type;
}
