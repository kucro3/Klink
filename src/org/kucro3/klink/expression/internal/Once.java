package org.kucro3.klink.expression.internal;

import org.kucro3.klink.*;
import org.kucro3.klink.exception.JumpOut;
import org.kucro3.klink.expression.Expression;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.syntax.Sequence;

public class Once implements ExpressionCompiler.Level3 {
    @Override
    public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock, Snapshot context)
    {
        Executable body;

        if(!seq.hasNext())
            return new JumpOutStub();

        return new CodeStub(Util.pullExecutable(seq, refs, codeBlock, context));
    }

    public static Expression instance()
    {
        return new Expression("#once", new Once());
    }

    public static abstract class Stub
    {
        public boolean getFlag()
        {
            return called;
        }

        public void setFlag(boolean b)
        {
            this.called = b;
        }

        private boolean called;
    }

    public static class JumpOutStub extends Stub implements ExpressionInstance
    {
        public JumpOutStub()
        {
        }

        @Override
        public void call(Klink sys, Environment env)
        {
            if(!getFlag())
                setFlag(true);
            else
                throw new JumpOut();
        }
    }

    public static class CodeStub extends Stub implements ExpressionInstance
    {
        public CodeStub(Executable body)
        {
            this.body = body;
        }

        public Executable getBody()
        {
            return body;
        }

        @Override
        public void call(Klink sys, Environment env)
        {
            if(getFlag())
                return;

            if(body != null)
                body.execute(sys);
            this.setFlag(true);
        }

        private final Executable body;
    }
}
