package org.kucro3.klink.functional;

import org.kucro3.klink.Environment;
import org.kucro3.klink.flow.Flow;

public class KlinkFunction implements Callable {
    KlinkFunction(Flow flow)
    {
        this.flow = flow;
    }

    @Override
    public void call(Environment env, Ref[] refs, CallInfo context)
    {

    }

    private final Flow flow;
}
