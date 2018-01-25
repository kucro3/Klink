package org.kucro3.klink.syntax.misc;

import org.kucro3.klink.syntax.Sequence;

public interface Parser {
    public Parser parse(Sequence seq);

    public Parser clone();

    public void reset();

    public Result getResult();
}
