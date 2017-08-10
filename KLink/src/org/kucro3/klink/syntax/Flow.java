package org.kucro3.klink.syntax;

import org.kucro3.collection.CompoundList;
import org.kucro3.klink.Klink;

public class Flow implements Executable {
	public Flow()
	{
	}
	
	@Override
	public void execute(Klink sys) 
	{
		for(Operation operation : flow)
			operation.execute(sys);
	}
	
	public void appendOperation(Operation operation)
	{
		flow.add(operation);
	}
	
	public void clearOperation()
	{
		flow.clear();
	}
	
	public int size()
	{
		return flow.size();
	}
	
	private CompoundList<Operation> flow;
}