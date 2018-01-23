package org.kucro3.klink.expression.internal.functional;

import org.kucro3.klink.Ref;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.functional.*;
import org.kucro3.klink.syntax.Sequence;

import java.util.*;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class Function implements ExpressionCompiler.Level2 {
    @Override
    public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock)
    {
        final Flow flow = codeBlock == null ? new Flow() : codeBlock;

        Parameter<?>[] params;

        String content = seq.next().trim();
        if(!content.startsWith("("))
            throw new ScriptException("Syntax error");

        StringBuilder buffer = new StringBuilder(content.substring(1));

        while(true)
            if((content = seq.next().trim()).endsWith(")"))
            {
                buffer.append(" ").append(content.substring(0, content.length() - 1));
                break;
            }
            else
                buffer.append(" ").append(content);

        content = buffer.toString().trim();

        if(content.isEmpty())
            params = new Parameter[0];
        else {
            String[] contents = content.split(",");
            params = new Parameter[content.length()];

            for (int i = 0; i < contents.length; i++) {
                ParameterContext param = new ParameterContext();

                String[] tokens = contents[i].split(" ");
                String name = tokens[tokens.length - 1];

                for (int j = 0; j < tokens.length - 1; j++) {
                    ContextManipluator manipluator = modifierManipluators.get(tokens[j]);
                    if (manipluator != null)
                        manipluator.manipluate(param);
                    else
                        throw new ScriptException("Invalid modifier: " + tokens[j]);
                }

                if (name.startsWith("&")) {
                    param.reference = true;
                    name = name.substring(1);
                }

                param.name = name;

                params[i] = param;
            }
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
}
