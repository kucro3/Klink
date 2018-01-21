package org.kucro3.klink.expression.internal.functional;

import org.kucro3.klink.Ref;
import org.kucro3.klink.expression.ExpressionCompiler;
import org.kucro3.klink.expression.ExpressionInstance;
import org.kucro3.klink.expression.ExpressionLibrary;
import org.kucro3.klink.flow.Flow;
import org.kucro3.klink.syntax.Sequence;

public class Function implements ExpressionCompiler.Level2 {
    @Override
    public ExpressionInstance compile(ExpressionLibrary lib, Ref[] refs, Sequence seq, Flow codeBlock)
    {
        final Flow flow = codeBlock == null ? new Flow() : codeBlock;
    }
}
