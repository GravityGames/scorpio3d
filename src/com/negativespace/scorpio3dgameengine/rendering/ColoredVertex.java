package com.negativespace.scorpio3dgameengine.rendering;

public class ColoredVertex extends Vertex{
	
	public float[] color = {1.0f, 1.0f, 1.0f, 1.0f};
	
	public static final byte COLOR_SIZE = 4 * 4;

	public ColoredVertex(float x, float y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	
	public ColoredVertex(float x, float y, float z) {
		super(x, y, z);
		// TODO Auto-generated constructor stub
	}
	
	public ColoredVertex(float x, float y, int r, int g, int b){
		super(x, y);
		this.color[0] = (float)r/255.0f;
		this.color[1] = (float)g/255.0f;
		this.color[2] = (float)b/255.0f;
	}
	
	public ColoredVertex(float x, float y, int r, int g, int b, int a){
		this(x, y, r, g, b);
		this.color[3] = (float)a/255.0f;
	}
	
	public ColoredVertex(float x, float y, float z, int r, int g, int b){
		super(x, y, z);
		this.color[0] = (float)r/255.0f;
		this.color[1] = (float)g/255.0f;
		this.color[2] = (float)b/255.0f;
	}
	
	public ColoredVertex(float x, float y, float z, int r, int g, int b, int a){
		this(x, y, z, r, g, b);
		this.color[3] = (float)a/255.0f;
	}
	
	public ColoredVertex(float x, float y, float z, float r, float g, float b){
		super(x, y, z);
		this.color[0] = r;
		this.color[1] = g;
		this.color[2] = b;
	}
	
	public ColoredVertex(float x, float y, float z, float r, float g, float b, float a){
		this(x, y, z, r, g, b);
		this.color[3] = a;
	}
	
	public float getR(){
		return this.color[0];
	}
	
	public float getG(){
		return this.color[1];
	}
	
	public float getB(){
		return this.color[2];
	}
	
	public float getA(){
		return this.color[3];
	}
	
	public void setColor(int r, int g, int b, int a){
		this.color[0] = (float)r/255.0f;
		this.color[1] = (float)g/255.0f;
		this.color[2] = (float)b/255.0f;
		this.color[3] = (float)a/255.0f;
	}
	
	public void setColor(float r, float g, float b, float a){
		this.color[0] = r;
		this.color[1] = g;
		this.color[2] = b;
		this.color[3] = a;
	}
	
	public void setR(int r){
		this.color[0] = (float)r/255.0f;
	}
	
	public void setR(float r){
		this.color[0] = r;
	}
	
	public void setG(int g){
		this.color[1] = (float)g/255.0f;
	}
	
	public void setG(float g){
		this.color[1] = g;
	}
	
	public void setB(int b){
		this.color[2] = (float)b/255.0f;
	}
	
	public void setB(float b){
		this.color[2] = b;
	}
	
	public void setA(int a){
		this.color[3] = (float)a/255.0f;
	}
	
	public void setA(float a){
		this.color[3] = a;
	}

}
