package org.kucro3.klink.registers;

import org.kucro3.klink.Registers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class RegisterHelper {
    RegisterHelper(String name)
    {
        this.name = name;
    }

    public abstract void leftShiftRegisters(Registers regset);

    public abstract void rightShiftRegisters(Registers regset);

    public abstract void clearRegisters(Registers regset);

    public abstract boolean fillRegisters(Registers regset, Object object);

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
        {
            put("RV", new RVHelper());
        }
    };

    private final String name;
}
