package org.kucro3.klink.functional;

import org.kucro3.klink.exception.ScriptException;

public class CallInfo {
    public CallInfo()
    {
    }

    public void allocateReturns(int size)
    {
        if(returns != null)
            throw new ScriptException("Return slot already allocated");

        if(size < 0 || size > 16)
            throw new ScriptException("illegal return slot allocation: size of " + size);

        this.returns = new Object[size];
    }

    public java.util.Optional<Object[]> getReturns()
    {
        return java.util.Optional.ofNullable(returns);
    }

    private Object[] returns;
}
