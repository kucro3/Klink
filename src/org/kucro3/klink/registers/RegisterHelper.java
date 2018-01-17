package org.kucro3.klink.registers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class RegisterHelper {


    public static Optional<RegisterHelper> getHelper(String name)
    {
        return Optional.ofNullable(helpers.get(name));
    }

    private static final Map<String, RegisterHelper> helpers = new HashMap<String, RegisterHelper>()
    {

    };
}
