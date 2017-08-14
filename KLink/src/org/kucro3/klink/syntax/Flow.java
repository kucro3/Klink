package org.kucro3.klink.syntax;

import org.kucro3.collection.CompoundList;
import org.kucro3.klink.Klink;
import org.kucro3.klink.exception.JumpOut;

public class Flow implements Executable {
	public Flow()
	{
	}
	
	@Override
	public void execute(Klink sys) 
	{
		try {
			for(Executable operation : flow)
				if(operation != null)
					operation.execute(sys);
		} catch (JumpOut signal) {
		}
	}
	
	public void append(Executable operation)
	{
		flow.add(operation);
	}
	
	public void clear()
	{
		flow.clear();
	}
	
	public int size()
	{
		return flow.size();
	}
	
	private CompoundList<Executable> flow = new CompoundList<>();
}