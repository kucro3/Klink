package org.kucro3.klink.registers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class RegisterHelper {
    RegisterHelper(String name)
    {
        this.name = name;
    }

    public abstract void leftShiftRegisters();

    public abstract void rightShiftRegisters();

    public abstract void clearRegisters();

    public abstract void fillRegisters(Object object);

    public String getName()
    {
        return name;
    }

    public static Optional<RegisterHelper> getHelper(String name)
    {
        return Optional.ofNullable(helpers.get(name));
    }

    private static final Map<String, RegisterHelper> helpers = new HashMap<String, RegisterHelper>()
    {

    };

    private final String name;
}
