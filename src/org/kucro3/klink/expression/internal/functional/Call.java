package org.kucro3.klink.expression.internal.functional;

import org.kucro3.klink.Ref;
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
        // TODO

        return (sys, env) -> {
            KlinkFunctionRegistry registry = sys.getService(KlinkFunctionRegistry.class).orElse(null);

            if(registry == null)
                throw new ScriptException("Functional service not available");


        };
    }

    private static final Vector VECTOR = new Vector("(", ")", ",", 16);
}
