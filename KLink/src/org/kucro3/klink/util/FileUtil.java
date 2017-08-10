package org.kucro3.klink.util;

import java.io.*;
import java.util.ArrayList;

import org.kucro3.klink.syntax.*;

public class FileUtil {
	public static Sequence readFrom(String filename) throws IOException
	{
		return readFrom(new File(filename));
	}
	
	public static Sequence readFrom(File file) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int word;
		ArrayList<String> strs = new ArrayList<>();
		ArrayList<int[]> linemarks = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		
		int[] currentLinemark = null;
		
		while((word = reader.read()) != -1)
			switch(word)
			{
			case '\n':
			case '\r':
				if(currentLinemark != null)
					currentLinemark[1]++;
				else {
					currentLinemark = new int[2];
					currentLinemark[0] = strs.size();
					currentLinemark[1] = 1;
				}
				
			case ' ':
				if(currentLinemark != null)
					linemarks.add(currentLinemark);
				currentLinemark = null;
				
				strs.add(sb.toString());
				
				sb = new StringBuilder();
				
				break;
				
			default:
				if(currentLinemark != null)
					linemarks.add(currentLinemark);
				currentLinemark = null;
				
				sb.append((char) word);
			}
		
		reader.close();
		
		return new Sequence(strs.toArray(new String[0]), linemarks.toArray(new int[0][0]));
	}
}