package org.kucro3.util;

import org.kucro3.klink.Klink;
import org.kucro3.klink.functional.KlinkFunctionRegistry;

import java.io.File;

public class Test {
    public static void main(String[] args) throws Exception
    {
        Klink klink = Klink.getDefault();
        File file = new File("E:\\klink2test.klnk");

        klink.provideService(KlinkFunctionRegistry.class, new KlinkFunctionRegistry());

        klink.execute(file);
    }
}
