package org.kucro3.klink.expression.internal.functional;

import org.kucro3.klink.expression.Expression;

public class CallNative {
    private CallNative()
    {
    }

    public static Expression instance()
    {
        return new Expression("CallNative", new Call(true));
    }
}
