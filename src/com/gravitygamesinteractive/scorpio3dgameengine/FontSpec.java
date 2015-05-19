package com.gravitygamesinteractive.scorpio3dgameengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FontSpec {
	
	int imageWidth = 256, imageHeight=256;
	
	public ArrayList<Integer> IDs = new ArrayList<Integer>();
	public ArrayList<Integer> xValues = new ArrayList<Integer>();
	public ArrayList<Integer> yValues = new ArrayList<Integer>();
	public ArrayList<Integer> widthValues = new ArrayList<Integer>();
	public ArrayList<Integer> heightValues = new ArrayList<Integer>();
	public ArrayList<Integer> xOffsets = new ArrayList<Integer>();
	public ArrayList<Integer> yOffsets = new ArrayList<Integer>();
	public ArrayList<Integer> xAdvances = new ArrayList<Integer>();
	
	public void readFontSpec(String filename){

		try {
			//InputStream in = new FileInputStream(filename);
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			String line;
			String[] splitLine;
			String[] splitLine2;
			while((line = reader.readLine()) != null){
				if(line.startsWith("char") && !line.startsWith("chars")){
					splitLine = line.split("\\s+");
					splitLine2 = splitLine[1].split("=");
					IDs.add(Integer.parseInt(splitLine2[1]));
					splitLine2 = splitLine[2].split("=");
					xValues.add(Integer.parseInt(splitLine2[1]));
					splitLine2 = splitLine[3].split("=");
					yValues.add(Integer.parseInt(splitLine2[1]));
					splitLine2 = splitLine[4].split("=");
					widthValues.add(Integer.parseInt(splitLine2[1]));
					splitLine2 = splitLine[5].split("=");
					heightValues.add(Integer.parseInt(splitLine2[1]));
					splitLine2 = splitLine[6].split("=");
					xOffsets.add(Integer.parseInt(splitLine2[1]));
					splitLine2 = splitLine[7].split("=");
					yOffsets.add(Integer.parseInt(splitLine2[1]));
					splitLine2 = splitLine[8].split("=");
					xAdvances.add(Integer.parseInt(splitLine2[1]));
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
