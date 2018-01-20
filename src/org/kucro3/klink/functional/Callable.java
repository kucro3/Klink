package org.kucro3.klink.functional;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;

public interface Callable {
    public void call(Klink klink, Environment env, Ref[] refs, CallInfo context);
}
