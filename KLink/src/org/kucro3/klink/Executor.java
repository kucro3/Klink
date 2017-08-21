package org.kucro3.klink;

import org.kucro3.klink.exception.Interruption;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.syntax.Executable;

public class Executor {
	public Executor()
	{
	}

	public void execute(Executable entity, Klink sys) 
	{
		try {
			entity.execute(sys);
		} catch (ScriptException e) {
			if(scriptExceptionHandler != null)
				scriptExceptionHandler.handle(e);
		} catch (Interruption i) {
			if(interruptionHandler != null)
				interruptionHandler.handle(i);
		}
	}
	
	public ScriptExceptionHandler getScriptExceptionHandler()
	{
		return scriptExceptionHandler;
	}
	
	public InterruptionHandler getInterruptionHandler()
	{
		return interruptionHandler;
	}
	
	public void setScriptExceptionHandler(ScriptExceptionHandler handler)
	{
		this.scriptExceptionHandler = handler;
	}
	
	public void setInterruptionHandler(InterruptionHandler handler)
	{
		this.interruptionHandler = handler;
	}
	
	protected ScriptExceptionHandler scriptExceptionHandler;
	
	protected InterruptionHandler interruptionHandler;
	
	public static interface ScriptExceptionHandler
	{
		void handle(ScriptException e);
	}
	
	public static interface InterruptionHandler
	{
		void handle(Interruption e);
	}
}