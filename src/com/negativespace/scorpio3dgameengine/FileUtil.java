package com.negativespace.scorpio3dgameengine;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
	
	public static byte[] readBinaryFile(String aInputFileName){
        //log("Reading in binary file named : " + aInputFileName);
        File file = new File(aInputFileName);
        //log("File size: " + file.length());
        byte[] result = new byte[(int)file.length()];
        try {
          InputStream input = null;
          try {
            int totalBytesRead = 0;
            input = new BufferedInputStream(new FileInputStream(file));
            while(totalBytesRead < result.length){
              int bytesRemaining = result.length - totalBytesRead;
              //input.read() returns -1, 0, or more :
              int bytesRead = input.read(result, totalBytesRead, bytesRemaining); 
              if (bytesRead > 0){
                totalBytesRead = totalBytesRead + bytesRead;
              }
            }
            /*
             the above style is a bit tricky: it places bytes into the 'result' array; 
             'result' is an output parameter;
             the while loop usually has a single iteration only.
            */
            //log("Num bytes read: " + totalBytesRead);
          }
          finally {
            //log("Closing input stream.");
        	  if(input!=null){
        		  input.close();
        	  }
          }
        }
        catch (FileNotFoundException ex) {
          System.out.println("File not found.");
          result = null;
        }
        catch (IOException ex) {
          System.out.println(ex);
        }
        return result;
      }

}
