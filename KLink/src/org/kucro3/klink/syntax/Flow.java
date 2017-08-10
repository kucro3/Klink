package org.kucro3.klink.syntax;

import org.kucro3.collection.CompoundList;

public class Flow implements Executable {
	public Flow()
	{
	}
	
	@Override
	public void execute() 
	{
		for(Operation op : flow)
			op.execute();
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