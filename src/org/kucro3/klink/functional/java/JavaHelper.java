package org.kucro3.klink.functional.java;

import org.kucro3.klink.functional.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JavaHelper {
    public static Collection<JavaFunction> exportFunctions(Class<?> clazz)
    {
        List<JavaFunction> list = new ArrayList<>();

        for(Method method : clazz.getMethods())
            exportFunction(method).ifPresent(list::add);

        return list;
    }

    @SuppressWarnings("unchecked")
    public static java.util.Optional<JavaFunction> exportFunction(Method method)
    {
        Export exportInfo = method.getAnnotation(Export.class);

        if(exportInfo == null)
            return java.util.Optional.empty();

        int access = method.getModifiers();

        if(!Modifier.isPublic(access) || !Modifier.isStatic(access))
            return java.util.Optional.empty();

        boolean frontCallInfo;
        String identifier = exportInfo.value();
        java.lang.reflect.Parameter[] params = method.getParameters();

        if(params.length == 0)
            return java.util.Optional.empty();

        if(params[0].getType().equals(CallInfo.class))
            frontCallInfo = true;
        else if(params[params.length - 1].getType().equals(CallInfo.class))
            frontCallInfo = false;
        else
            return java.util.Optional.empty();

        Parameter<JavaType>[] generic = new Parameter[params.length - 1];

        int start = frontCallInfo ? 1 : 0;
        int end = frontCallInfo ? params.length - 1 : params.length - 2;
        for(int i = start, j = 0; i < end; i++, j++)
            generic[j] = new JavaParameter(params[i]);

        int j = 0;
        for(; j < generic.length && !generic[j].isOptional(); j++);
        for(; j < generic.length; j++)
            if(!generic[j].isOptional())
                return java.util.Optional.empty();

        return java.util.Optional.of(new JavaFunction(identifier, method, generic, frontCallInfo));
    }

    public static class JavaParameter implements Parameter<JavaType>
    {
        public JavaParameter(java.lang.reflect.Parameter param) {
            Optional optional = param.getAnnotation(Optional.class);
            Reference reference = param.getAnnotation(Reference.class);

            this.optional = optional != null;
            this.reference = reference != null;

            if(this.reference)
                this.type = new JavaType(reference.value());
            else
                this.type = new JavaType(param.getType());

            this.name = param.getName();
            this.param = param;
        }

        @Override
        public JavaType getType()
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

        public java.lang.reflect.Parameter getParameter()
        {
            return param;
        }

        protected final boolean optional;

        protected final boolean reference;

        protected final String name;

        protected final JavaType type;

        protected final java.lang.reflect.Parameter param;
    }
}
