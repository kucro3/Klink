package org.kucro3.klink.expression.internal.functional;

import org.kucro3.klink.Ref;
import org.kucro3.klink.Util;
import org.kucro3.klink.exception.JumpOut;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.syntax.misc.Vector;
import org.kucro3.klink.syntax.Sequence;

/**
 * Return ($anyvar, @anyreg, @/$...) ;
 */
public class Return implements ExpressionCompiler.Level1 {
    @Override
    public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq)
    {
        Vector vector = VECTOR.clone().parse(seq);

        if(vector.isExcepted())
            if(vector.outOfLimit())
                throw new ScriptException("Too many return values (no more than 16)");
            else
                throw new ScriptException("Syntax error");

        String[] contents = vector.getLastParsed();

        if(contents.length == 0)
            return (sys, env) -> {throw new JumpOut();};

        final Ref[] returns = new Ref[contents.length];
        for(int i = 0; i < returns.length; i++)
            returns[i] = Util.toRef(contents[i]);

        return (sys, env) -> {
            for(int i = 0; i < returns.length; i++)
                env.getRegisters().RV[i] = returns[i];
        };
    }

    private static final Vector VECTOR = new Vector("(", ")", ",", 16);
}
