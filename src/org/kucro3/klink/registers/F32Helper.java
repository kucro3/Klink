package org.kucro3.klink.registers;

import org.kucro3.klink.Registers;

public class F32Helper extends RegisterHelper {
    F32Helper()
    {
        super("F32");
    }

    @Override
    public void leftShiftRegisters(Registers regset)
    {
        float[] F32 = regset.F32;
        for(int i = 0; i < F32.length - 1; i++)
            F32[i] = F32[i + 1];
        F32[F32.length - 1] = 0F;
    }

    @Override
    public void rightShiftRegisters(Registers regset)
    {
        float[] F32 = regset.F32;
        for(int i = F32.length - 1; i > 0; i--)
            F32[i] = F32[i - 1];
        F32[0] = 0F;
    }

    @Override
    public void clearRegisters(Registers regset)
    {
        float[] F32 = regset.F32;
        for(int i = 0; i < F32.length; i++)
            F32[i] = 0F;
    }

    @Override
    public boolean fillRegisters(Registers regset, Object object)
    {
        if(!(object instanceof Number))
            return false;

        float[] F32 = regset.F32;
        for(int i = 0; i < F32.length; i++)
            F32[i] = ((Number) object).floatValue();

        return true;
    }
}
