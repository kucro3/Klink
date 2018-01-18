package org.kucro3.klink.functional;

import org.kucro3.klink.Environment;

public interface Callable {
    public void call(Environment env, Ref[] refs, CallInfo context);
}
