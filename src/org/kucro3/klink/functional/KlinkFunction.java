package org.kucro3.klink.functional;

import org.kucro3.klink.Environment;
import org.kucro3.klink.Klink;
import org.kucro3.klink.OverridableVariables;
import org.kucro3.klink.Variables;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.flow.Flow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class KlinkFunction implements Function {
    public KlinkFunction(Flow flow, String identifier, Parameter[] params)
    {
        this.flow = flow;
        this.identifier = identifier;

        this.params = Arrays.asList(params);

        this.essentialParams = new ArrayList<>();
        this.optionalParams = new ArrayList<>();

        int i = 0;

        for(; i < params.length; i++)
            if(params[i].isOptional())
                break;
            else
                essentialParams.add(params[i]);

        for(; i < params.length; i++)
            if(!params[i].isOptional())
                throw new ScriptException("Illegal argument declaration");
            else
                optionalParams.add(params[i]);

        this.essentialParams.trimToSize();
        this.optionalParams.trimToSize();
    }

    @Override
    public void call(Klink klink, Environment env, org.kucro3.klink.Ref[] refs, CallInfo context)
    {
        // protect scene
        Variables vars = env.getVars();
        Variables replacement = new OverridableVariables(vars);

        try {
            // call
            if (refs.length < essentialParams.size())
                throw NeedMoreArguments();

            for (int i = 0; i < essentialParams.size(); i++)
            {
                Parameter<Type> param = essentialParams.get(i);
                Object object = refs[i].get(env);

                if (param != null && !param.getType().isType(object))
                    throw IncompatibleArgumentType(param.getType().getName(), i);

                Variables.Variable var =
                        param.isReference() ?
                                refs[i].isVar() ? new VarVariable(param.getName(), vars, refs[i]) : new RefVariable(param.getName(), env, refs[i])
                        : new HeapVariable(param.getName(), object);

                replacement.putVar(var);
            }

        } finally {
            // recover scene
            if (env.getVars() == replacement)
                env.setVars(vars);
        }
    }

    @Override
    public String getIdentifier()
    {
        return identifier;
    }

    @Override
    public int getParameterCount()
    {
        return params.size();
    }

    @Override
    public List<Parameter<Type>> getParameters()
    {
        return Collections.unmodifiableList(params);
    }

    @Override
    public List<Parameter<Type>> getEssentialParameters()
    {
        return Collections.unmodifiableList(essentialParams);
    }

    @Override
    public List<Parameter<Type>> getOptionalParameters()
    {
        return Collections.unmodifiableList(optionalParams);
    }

    public static ScriptException NeedMoreArguments()
    {
        return new ScriptException("Need more arguments");
    }

    public static ScriptException IncompatibleArgumentType(String name, int index)
    {
        return new ScriptException("Incompatible argument type at index " + index + " (Type of \" " + name + " \" needed)");
    }

    private final String identifier;

    private final List<Parameter<Type>> params;

    private final ArrayList<Parameter<Type>> essentialParams;

    private final ArrayList<Parameter<Type>> optionalParams;

    private final Flow flow;

    public static class HeapVariable implements Variables.Variable
    {
        public HeapVariable(String name, Object obj)
        {
            this.name = name;
            this.object = obj;
        }

        @Override
        public Object get()
        {
            return object;
        }

        @Override
        public void set(Object object)
        {
            this.object = object;
        }

        @Override
        public String getName()
        {
            return name;
        }

        protected Object object;

        private final String name;
    }

    public static class VarVariable implements Variables.Variable
    {
        public VarVariable(String name, Variables vars, org.kucro3.klink.Ref ref)
        {
            this.name = name;
            this.vars = vars;
            this.ref = ref;
        }

        @Override
        public Object get()
        {
            return vars.requireVar(ref.getName()).get();
        }

        @Override
        public void set(Object object)
        {
            vars.requireVar(ref.getName()).set(object);
        }

        @Override
        public String getName()
        {
            return name;
        }

        protected org.kucro3.klink.Ref ref;

        protected Variables vars;

        private final String name;
    }

    public static class RefVariable implements Variables.Variable
    {
        public RefVariable(String name, Environment env, org.kucro3.klink.Ref ref)
        {
            this.name = name;
            this.env = env;
            this.ref = ref;
        }

        @Override
        public Object get()
        {
            return ref.get(env);
        }

        @Override
        public void set(Object object)
        {
            ref.set(env, object);
        }

        @Override
        public String getName()
        {
            return name;
        }

        protected final Environment env;

        protected org.kucro3.klink.Ref ref;

        private final String name;
    }
}
