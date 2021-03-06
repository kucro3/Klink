package org.kucro3.klink.expression;

import java.util.HashMap;
import java.util.Map;

import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.expression.internal.*;
import org.kucro3.klink.expression.internal.functional.Call;
import org.kucro3.klink.expression.internal.functional.CallNative;
import org.kucro3.klink.expression.internal.functional.Function;
import org.kucro3.klink.expression.internal.functional.Return;

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
		forceExpression(Once.instance());
		forceExpression(Call.instance());
		forceExpression(CallNative.instance());
		forceExpression(Function.instance());
		forceExpression(Return.instance());
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