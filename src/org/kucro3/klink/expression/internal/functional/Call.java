package org.kucro3.klink.expression.internal.functional;

import org.kucro3.klink.Ref;
import org.kucro3.klink.Util;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.functional.KlinkFunctionRegistry;
import org.kucro3.klink.syntax.misc.Vector;
import org.kucro3.klink.syntax.Sequence;

/**
 * Formats:
 *
 * Call FunctionName ;
 * Call (args) FunctionName ;
 * Call (args) -> (returns) FunctionName ;
 * Call -> (returns) FunctionName ;
 */
public class Call implements ExpressionCompiler.Level1 {
    @Override
    public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq)
    {
        Vector vector = VECTOR.clone().parse(seq);

        final String functionName;
        final Ref[] args;
        final Ref[] returns;

        String temp;
        String[] parsed;

        PARSING:
        {
            PARSE_PARAMS:
            if (vector.isExcepted())
                if (vector.outOfLimit())
                    throw new ScriptException("Too many arguments (no more than 16)");
                else
                {
                    args = new Ref[0];

                    if((temp = vector.getLastReaded().get(0)).equals("->"))
                        break PARSE_PARAMS;

                    returns = new Ref[0];
                    functionName = temp;

                    break PARSING;
                }
            else
            {
                parsed = vector.getLastParsed();
                args = new Ref[parsed.length];

                for(int i = 0; i < parsed.length; i++)
                    args[i] = Util.toRef(parsed[i]);

                if((temp = seq.next()).equals("->"))
                {
                    vector.parse(seq);

                    if(vector.isExcepted())
                        if(vector.outOfLimit())
                            throw new ScriptException("Too many return values (no more than 16)");
                        else
                            throw new ScriptException("Syntax error");

                    parsed = vector.getLastParsed();
                    returns = new Ref[parsed.length];

                    for(int i = 0; i < parsed.length; i++)
                        returns[i] = Util.toRef(parsed[i]);

                    break PARSING;
                }
                else
                {
                    functionName = temp;
                    returns = new Ref[0];

                    break PARSING;
                }
            }
        }

        return (sys, env) -> {
            KlinkFunctionRegistry registry = sys.getService(KlinkFunctionRegistry.class).orElse(null);

            if(registry == null)
                throw new ScriptException("Functional service not available");


        };
    }

    private static final Vector VECTOR = new Vector("(", ")", ",", 16);
}
