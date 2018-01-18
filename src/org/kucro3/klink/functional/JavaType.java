package org.kucro3.klink.functional;

@Typename("Java")
public final class JavaType implements Type<Class<?>> {
    public static JavaType of(Class<?> type)
    {
        return new JavaType(type);
    }

    JavaType(Class<?> type)
    {
        this.type = type;
    }

    @Override
    public Class<?> getType()
    {
        return type;
    }

    private final Class<?> type;
}
