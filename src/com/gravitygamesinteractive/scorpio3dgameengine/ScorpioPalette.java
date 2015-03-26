package com.gravitygamesinteractive.scorpio3dgameengine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ScorpioPalette {
	
	public ArrayList<byte[]> colors = new ArrayList<byte[]>();
	
	public ScorpioPalette(){
		
	}
	
	public void addColor(byte r, byte g, byte b){
		addColor(r,g,b,(byte)255);
	}
	
	public void addColor(byte r, byte g, byte b, byte a){
		byte[] color = new byte[4];
		color[0] = r;
		color[1] = g;
		color[2] = b;
		color[3] = a;
		colors.add(color);
	}
	
	public void loadPalette(String filename){
		try {
			InputStream in = new FileInputStream(filename);
			char numColors = (char) (((byte)in.read() << 8) + ((byte)in.read() & 0xFF));
			for(int i=0; i<numColors; i++){
				byte[] color = new byte[4];
				color[0] = (byte)in.read();
				color[1] = (byte)in.read();
				color[2] = (byte)in.read();
				color[3] = (byte)in.read();
				colors.add(color);
			}
			//System.out.println((int)numColors);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static ScorpioPalette createPalette(String filename){
		ScorpioPalette scorpioPalette = new ScorpioPalette();
		try {
			InputStream in = new FileInputStream(filename);
			char numColors = (char) (((byte)in.read() << 8) + ((byte)in.read() & 0xFF));
			for(int i=0; i<numColors; i++){
				byte[] color = new byte[4];
				color[0] = (byte)in.read();
				color[1] = (byte)in.read();
				color[2] = (byte)in.read();
				color[3] = (byte)in.read();
				scorpioPalette.colors.add(color);
			}
			//System.out.println((int)numColors);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scorpioPalette;
	}
	
}
