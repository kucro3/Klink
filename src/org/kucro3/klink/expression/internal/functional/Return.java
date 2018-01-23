package org.kucro3.klink.expression.internal.functional;

import org.kucro3.klink.Ref;
import org.kucro3.klink.Util;
import org.kucro3.klink.exception.JumpOut;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.syntax.Sequence;

public class Return implements ExpressionCompiler.Level1 {
    @Override
    public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq)
    {
        String content;
        String[] contents;

        try {
            String expr = seq.leftToString();

            if(expr.trim().isEmpty())
                return (sys, env) -> {throw new JumpOut();};

            int start = expr.indexOf('(');
            int end = expr.lastIndexOf(')') + 1;

            content = expr.substring(start + 1, end);
            contents = content.split(",");
        } catch (Exception e) {
            throw new ScriptException("Syntax error");
        }

        if(contents.length > 16)
            throw new ScriptException("Too many return values (no more than 16)");

        if(contents.length == 1)
        {
            content = contents[0].trim();

            if(content.isEmpty())
                return (sys, env) -> {throw new JumpOut();};

            final Ref callingRef = Util.toRef(content) ;

            return (sys, env) ->
                    env.getRegisters().RV[0] = callingRef.get(env);
        }

        final Ref[] callingRefs = new Ref[contents.length];
        for(int i = 0; i < contents.length; i++)
            callingRefs[i] = Util.toRef(contents[i] = contents[i].trim());

        return (sys, env) -> {
            for(int i = 0; i < callingRefs.length; i++)
                env.getRegisters().RV[i] = callingRefs[i].get(env);
            throw new JumpOut();
        };
    }
}
