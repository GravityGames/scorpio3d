package com.negativespace.scorpio3dgameengine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TextLoader {
	
	public static String loadText(String filename){
		 StringBuilder text = new StringBuilder();
		    int shaderID = 0;
		     
		    try {
		        BufferedReader reader = new BufferedReader(new FileReader(filename));
		        String line;
		        while ((line = reader.readLine()) != null) {
		            text.append(line).append("\n");
		        }
		        reader.close();
		    } catch (IOException e) {
		        System.err.println("Could not read file.");
		        e.printStackTrace();
		        System.exit(-1);
		    }
		    
		    return text.toString();
	}
	
}
