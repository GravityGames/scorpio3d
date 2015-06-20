package com.gravitygamesinteractive.scorpio3dgameengine.rendering;

import org.lwjgl.util.vector.Vector3f;

public class Vertex {
	
	public float[] position = {0.0f, 0.0f, 0.0f};
	
	public static final byte POSITION_SIZE = 3 * 4;
	
	public Vertex(float x, float y){
		this(x, y, 0.0f);
	}
	
	public Vertex(float x, float y, float z){
		this.position[0]=x;
		this.position[1]=y;
		this.position[2]=z;
	}
	
	public float getX(){
		return position[0];
	}
	
	public float getY(){
		return position[1];
	}
	
	public float getZ(){
		return position[2];
	}
	
	public void setX(float x){
		position[0]=x;
	}
	
	public void setY(float y){
		position[1]=y;
	}
	
	public void setZ(float z){
		position[2]=z;
	}
	
	public Vector3f getPosition(){
		return new Vector3f(position[0], position[1], position[2]);
	}
	
	public void setPosition(float x, float y){
		position[0]=x;
		position[1]=y;
	}
	
	public void setPosition(float x, float y, float z){
		position[0]=x;
		position[1]=y;
		position[2]=z;
	}

}
