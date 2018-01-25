package org.kucro3.klink.registers;

import org.kucro3.klink.Registers;

public class RVHelper extends RegisterHelper {
    RVHelper()
    {
        super("RV");
    }

    @Override
    public void leftShiftRegisters(Registers regset)
    {
        Object[] RV = regset.RV;
        for(int i = 0; i < RV.length - 1; i++)
            RV[i] = RV[i + 1];
        RV[RV.length - 1] = null;
    }

    @Override
    public void rightShiftRegisters(Registers regset)
    {
        Object[] RV = regset.RV;
        for(int i = RV.length - 1; i > 0; i--)
            RV[i] = RV[i - 1];
        RV[0] = null;
    }

    @Override
    public void clearRegisters(Registers regset)
    {
        fillRegisters(regset, null);
    }

    @Override
    public boolean fillRegisters(Registers regset, Object object)
    {
        Object[] RV = regset.RV;
        for(int i = 0; i < RV.length; i++)
            RV[i] = null;
        return true;
    }
}
