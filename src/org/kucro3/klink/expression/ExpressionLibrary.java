package org.kucro3.klink.expression;

import java.util.HashMap;
import java.util.Map;

import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.internal.Break;
import org.kucro3.klink.expression.internal.ConstBoolean;
import org.kucro3.klink.expression.internal.ConstByte;
import org.kucro3.klink.expression.internal.ConstDouble;
import org.kucro3.klink.expression.internal.ConstFloat;
import org.kucro3.klink.expression.internal.ConstInt;
import org.kucro3.klink.expression.internal.ConstLong;
import org.kucro3.klink.expression.internal.ConstShort;
import org.kucro3.klink.expression.internal.ConstString;
import org.kucro3.klink.expression.internal.Escape;
import org.kucro3.klink.expression.internal.ExecuteDocument;
import org.kucro3.klink.expression.internal.Exit;
import org.kucro3.klink.expression.internal.False;
import org.kucro3.klink.expression.internal.LoadDocument;
import org.kucro3.klink.expression.internal.LoadDocumentAndRun;
import org.kucro3.klink.expression.internal.LoadExpressionPack;
import org.kucro3.klink.expression.internal.PrintVar;
import org.kucro3.klink.expression.internal.PrintlnVar;
import org.kucro3.klink.expression.internal.Run;
import org.kucro3.klink.expression.internal.True;
import org.kucro3.klink.expression.internal.VarAssignment;
import org.kucro3.klink.expression.internal.VarAssignmentFromExpression;
import org.kucro3.klink.expression.internal.VarCall;
import org.kucro3.klink.expression.internal.VarControl;

public class ExpressionLibrary {
	public ExpressionLibrary()
	{
		this(true);
	}
	
	public ExpressionLibrary(boolean sys)
	{
		if(sys)
			init();
	}
	
	private final void init()
	{
		forceExpression(True.instance());
		forceExpression(False.instance());
		forceExpression(VarControl.instance());
		forceExpression(VarCall.instance());
		forceExpression(VarAssignment.instance());
		forceExpression(VarAssignmentFromExpression.Object.instance());
		forceExpression(VarAssignmentFromExpression.Boolean.instance());
		forceExpression(PrintVar.instance());
		forceExpression(PrintlnVar.instance());
		forceExpression(Exit.instance());
		forceExpression(Escape.instance());
		forceExpression(Break.instance());
		forceExpression(ConstBoolean.instance());
		forceExpression(ConstByte.instance());
		forceExpression(ConstDouble.instance());
		forceExpression(ConstFloat.instance());
		forceExpression(ConstInt.instance());
		forceExpression(ConstLong.instance());
		forceExpression(ConstShort.instance());
		forceExpression(ConstString.instance());
		forceExpression(Run.instance());
		forceExpression(LoadDocument.instance());
		forceExpression(LoadDocumentAndRun.instance());
		forceExpression(ExecuteDocument.instance());
		forceExpression(LoadExpressionPack.instance());
	}
	
	public void removeExpression(String name)
	{
		identifiers.remove(name);
	}
	
	public Expression getExpression(String name)
	{
		return identifiers.get(name);
	}
	
	public Expression requireExpression(String name)
	{
		Expression identifier;
		if((identifier = getExpression(name)) == null)
			throw NoSuchIdentifier(name);
		return identifier;
	}
	
	public void forceExpression(Expression expression)
	{
		String name = expression.getName();
		identifiers.put(name, expression);
	}
	
	public void putExpression(Expression expression)
	{
		String name = expression.getName();
		if(identifiers.putIfAbsent(name, expression) != null)
			throw IdentifierRedefinition(name);
	}
	
	public static ScriptException NoSuchIdentifier(String name)
	{
		return new ScriptException("No such identifier: " + name);
	}
	
	public static ScriptException IdentifierRedefinition(String name)
	{
		return new ScriptException("Identifier Redefinition: " + name);
	}
	
	private final Map<String, Expression> identifiers = new HashMap<>();
}