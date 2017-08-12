package org.kucro3.klink.util;

import java.io.*;
import java.util.ArrayList;

import org.kucro3.klink.Klink;
import org.kucro3.klink.syntax.*;

public class FileUtil {
	public static void main(String[] args) throws Exception
	{
		Sequence seq = readFrom("E:\\test.klnk");
		Executable e = Translator.translate(Klink.getDefault(), seq, null);
		e.execute(Klink.getDefault());
	}
	
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
				
				sb.trimToSize();
				
				if(sb.length() != 0)
				{
					strs.add(sb.toString());
					
					sb = new StringBuilder();
				}
				
				break;
				
			default:
				if(currentLinemark != null)
					linemarks.add(currentLinemark);
				currentLinemark = null;
				
				sb.append((char) word);
			}
		
		sb.trimToSize();
		if(sb.length() != 0)
			strs.add(sb.toString());
		
		reader.close();
		
		return new Sequence(strs.toArray(new String[0]), linemarks.toArray(new int[0][0]));
	}
}