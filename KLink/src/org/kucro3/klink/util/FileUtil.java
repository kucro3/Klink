package org.kucro3.klink.util;

import java.io.*;
import java.util.ArrayList;

import org.kucro3.klink.Klink;
import org.kucro3.klink.exception.ScriptException;
import org.kucro3.klink.syntax.*;

public class FileUtil {
	public static void main(String[] args) throws Exception
	{
		Sequence seq = readFrom("E:\\test.klnk");
		try {
			Executable e = Translator.translate(Klink.getDefault(), seq, null);
			e.execute(Klink.getDefault());
		} catch (ScriptException e) {
			System.err.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Sequence readFrom(String filename) throws IOException
	{
		return readFrom(new File(filename));
	}
	
	public static Sequence readFrom(File file) throws IOException
	{
		BufferedReader reader = new BufferedReader(new FileReader(file));
		ArrayList<String> strs = new ArrayList<>();
		ArrayList<int[]> linemarks = new ArrayList<>();
		String line;
		
		int[] currentLinemark = null;
		
		while((line = reader.readLine()) != null)
		{
			line = line.trim();
			if(line.length() != 0)
			{
				if(currentLinemark != null)
					linemarks.add(currentLinemark);
				currentLinemark = null;
				
				String[] seq = line.split(" ");
				for(String s : seq)
					strs.add(s);
			}
			
			if(currentLinemark == null)
				(currentLinemark = new int[2])[0] = strs.size();
			currentLinemark[1]++;
		}
		
		reader.close();
		
		return new Sequence(strs.toArray(new String[0]), (int[][]) linemarks.toArray(new int[0][]));
	}
}