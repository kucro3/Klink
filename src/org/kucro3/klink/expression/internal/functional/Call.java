package org.kucro3.klink.expression.internal.functional;

import org.kucro3.klink.Ref;
import org.kucro3.klink.Registers;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.functional.*;
import org.kucro3.klink.functional.Function;
import org.kucro3.klink.syntax.misc.Caller;
import org.kucro3.klink.syntax.misc.Result;
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
    public Call(boolean isNative)
    {
        this.isNative = isNative;
    }

    @Override
    public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq)
    {
        Caller caller = CALLER.clone().parse(seq);

        Result result;
        if(!caller.getResult().passed())
            if(!(result = caller.getArgumentVector().getResult()).passed())
                if(result.equals(Vector.OUT_OF_LIMIT))
                    throw new ScriptException("Too many arguments (no more than 16)");
                else
                    throw new ScriptException(result.getMessage());
            else if(!(result = caller.getReturnVector().getResult()).passed())
                if(result.equals(Vector.OUT_OF_LIMIT))
                    throw new ScriptException("Too many return values (no more than 16)");
                else
                    throw new ScriptException(result.getMessage());
            else;
        else;

        final String functionName = caller.getLastParsedFunctionName();
        final Ref[] arguments = caller.getLastParsedArguments();
        final Ref[] returns = caller.getLastParsedReturns();

        return (sys, env) -> {
            FunctionRegistry registry =
                    isNative ? sys.getService(NativeFunctionRegistry.class).orElse(null)
                             : sys.getService(KlinkFunctionRegistry.class).orElse(null);

            if(registry == null)
                throw new ScriptException("Functional service not available");

            Function function = registry.requireFunction(functionName);

            Ref[] callingRefs;
            if(arguments != null)
                callingRefs = arguments;
            else
            {
                callingRefs = new Ref[function.getParameterCount()];
                for(int i = 0; i < callingRefs.length; i++)
                    callingRefs[i] = new Registers.Register(ADAPTER_RV, i);
            }

            CallInfo callInfo = new CallInfo();
            function.call(sys, env, callingRefs, callInfo);

            Object[] returned = callInfo.getReturns().orElse(new Object[0]);

            Ref[] returnRefs;
            if(returns == null)
                returnRefs = new Ref[0];
            else
                returnRefs = returns;

            int i = 0;
            for(int j = 0; j < returnRefs.length; j++, i++)
                returnRefs[j].set(env, returned[i]);

            Object[] RV = env.getRegisters().RV;
            for(int j = 0; i < returned.length && j < RV.length; j++, i++)
                RV[j] = returned[i];

            if(i != returned.length)
                throw new ScriptException("Return overflow");
        };
    }

    public static Expression instance()
    {
        return new Expression("Call", new Call(false));
    }

    private final boolean isNative;

    private static final Registers.Adapter ADAPTER_RV = new Registers.AdapterForRV();

    private static final Vector VECTOR = new Vector("(", ")", ",", 16);

    private static final Caller CALLER = new Caller(VECTOR, VECTOR, "->");
}
