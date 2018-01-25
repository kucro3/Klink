package org.kucro3.klink.expression.internal.functional;

import org.kucro3.klink.Ref;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.functional.*;
import org.kucro3.klink.syntax.misc.Vector;
import org.kucro3.klink.syntax.Sequence;

import java.util.*;
import java.util.Optional;

/**
 * $arg : reference
 * optional arg : optional
 *
 * Function (arg0, args...) FunctionName :
 *     ... codes here ;
 * ;
 */
@SuppressWarnings("unchecked")
public class Function implements ExpressionCompiler.Level2 {
    @Override
    public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock)
    {
        final Flow flow = codeBlock == null ? new Flow() : codeBlock;

        // parse params
        Parameter<?>[] params;
        Vector vector = VECTOR.clone().parse(seq);

        if(vector.isExcepted())
            if(vector.outOfLimit())
                throw new ScriptException("Too many arguments (no more than 16)");
            else
                throw new ScriptException("Syntax error");

        String[] parsed = vector.getLastParsed();
        params = new Parameter[parsed.length];
        for(int i = 0; i < params.length; i++)
        {
            ContextManipluator manipluator;
            ParameterContext param = new ParameterContext();
            String[] contents = parsed[i].split(" ");

            for(int j = 0; j < contents.length - 1; j++)
                if((manipluator = modifierManipluators.get(contents[j])) == null)
                    throw new ScriptException("Illegal modifier: " + contents[j]);
                else
                    manipluator.manipluate(param);

            params[i] = param;
        }

        final String functionName = seq.next();
        final KlinkFunction function = new KlinkFunction(flow, functionName, params);

        return (sys, env) -> {
            Optional<KlinkFunctionRegistry> optional = sys.getService(KlinkFunctionRegistry.class);

            if(!optional.isPresent())
                throw new ScriptException("Functional service not available");

            KlinkFunctionRegistry registry = optional.get();
            registry.defineFunction(functionName, function);
        };
    }

    private static interface ContextManipluator
    {
        void manipluate(ParameterContext context);
    }

    private static class ParameterContext implements Parameter
    {
        void setType(Type<?> type)
        {
            if(this.type != DEFAULT)
                throw new ScriptException("Multiple type modifier");
            this.type = type;
        }

        @Override
        public Type getType()
        {
            return type;
        }

        @Override
        public boolean isReference()
        {
            return reference;
        }

        @Override
        public boolean isOptional()
        {
            return optional;
        }

        @Override
        public String getName()
        {
            return name;
        }

        boolean optional;

        boolean reference;

        Type<?> type;

        String name;

        private static final KlinkType DEFAULT = new KlinkType(Object.class);
    }

    private static final Map<String, ContextManipluator> modifierManipluators = new HashMap<String, ContextManipluator>()
    {
        {
            put("optional", (ctx) -> ctx.optional = true);
        }
    };

    private static final Vector VECTOR = new Vector("(", ")", ",", 16);
}
