package com.gravitygamesinteractive.scorpio3dgameengine;

public class TexturedVertex extends ColoredVertex{
	
	//public float[] color = {1.0f, 1.0f, 1.0f, 1.0f};
	
	public float[] UVCoords = {0.0f, 0.0f};
	
	//public static final byte COLOR_SIZE = 4 * 4;
	
	public static final byte UV_SIZE = 4 * 2;
	
	public TexturedVertex(float x, float y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	
	public TexturedVertex(float x, float y, float z) {
		super(x, y, z);
		// TODO Auto-generated constructor stub
	}
	
	public TexturedVertex(float x, float y, float r, float g, float b, float a, float u, float v){
		super(x, y, r, g, b, a);
		UVCoords[0] = u;
		UVCoords[1] = v;
	}
	public TexturedVertex(float x, float y, float z, float r, float g, float b, float a, float u, float v){
		super(x, y, z, r, g, b, a);
		UVCoords[0] = u;
		UVCoords[1] = v;
	}
	
	public TexturedVertex(float x, float y, int r, int g, int b, int a, float u, float v){
		super(x, y, r, g, b, a);
		UVCoords[0] = u;
		UVCoords[1] = v;
	}
	public TexturedVertex(float x, float y, float z, int r, int g, int b, int a, float u, float v){
		super(x, y, z, r, g, b, a);
		UVCoords[0] = u;
		UVCoords[1] = v;
	}
	
	public float getU(){
		return UVCoords[0];
	}
	
	public float getV(){
		return UVCoords[1];
	}
	
	public float[] getUV(){
		return UVCoords;
	}
	
	public void setUV(float u, float v){
		UVCoords[0] = u;
		UVCoords[1] = v;
	}
	
	public void setU(float u){
		UVCoords[0] = u;
	}
	
	public void setV(float v){
		UVCoords[1] = v;
	}
}
