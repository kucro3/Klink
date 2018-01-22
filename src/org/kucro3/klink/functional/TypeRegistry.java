package org.kucro3.klink.functional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TypeRegistry {
    public Optional<Type<?>> getType(String name)
    {
        return Optional.ofNullable(map.get(name));
    }

    public boolean hasType(String name)
    {
        return map.containsKey(name);
    }

    public boolean defType(String name, Type<?> type)
    {
        if(map.containsKey(name))
            return false;
        map.put(name, type);
        return true;
    }

    private final Map<String, Type<?>> map = new HashMap<>();
}
