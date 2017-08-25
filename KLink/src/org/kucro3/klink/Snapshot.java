package org.kucro3.klink;

import org.kucro3.klink.flow.FlowSnapshot;

public class Snapshot {
	public Snapshot(Translator translator, FlowSnapshot flowsnapshot)
	{
		this.translator = translator;
		this.flowsnapshot = flowsnapshot;
	}
	
	public FlowSnapshot getFlowSnapshot()
	{
		return flowsnapshot;
	}
	
	public Translator getTranslator()
	{
		return translator;
	}
	
	private final FlowSnapshot flowsnapshot;
	
	private final Translator translator;
}