package org.kucro3.klink.syntax;

import java.util.Iterator;

public class Sequence implements Iterator<String> {
	public Sequence(String[] seq)
	{
		this(seq, null);
	}
	
	public Sequence(String[] seq, int[][] linemarks)
	{
		this.seq = seq;
		this.linemarks = linemarks;
	}

	@Override
	public boolean hasNext() 
	{
		return pointer < seq.length;
	}

	@Override
	public String next() 
	{
		if(linemarks == null || linemarks.length == 0 || !(linemarkPtr < linemarks.length))
			;
		else
		{
			int[] linemark = linemarks[linemarkPtr];
			if(pointer == linemark[0])
			{
				row = 0;
				column += linemark[1];
			}
			else
				;
		}
		row++;
		return seq[pointer++];
	}
	
	public int current()
	{
		return pointer - 1;
	}
	
	public int currentRow()
	{
		return row - 1;
	}
	
	public int currentColumn()
	{
		return column;
	}
	
	private final String[] seq;
	
	private int linemarkPtr;
	
	private final int[][] linemarks;
	
	private int pointer;
	
	private int row = 0;
	
	private int column = 0;
}