package com.gravitygamesinteractive.scorpio3dgameengine.rendering;

import org.lwjgl.util.vector.Vector3f;

import com.gravitygamesinteractive.scorpio3dgameengine.GameObject;

public class Camera extends GameObject{
	
	public Vector3f cameraPosition;
	
	public Camera(){
		cameraPosition = new Vector3f(0, 0, 0);
	}
	
	public Camera(float x, float y, float z){
		cameraPosition = new Vector3f(x, y, z);
	}
	
	public void translate(float x, float y, float z){
		translate(new Vector3f(x, y, z));
	}
	
	public void translate(Vector3f translation){
		cameraPosition.setX(cameraPosition.getX() + translation.getX());
		cameraPosition.setY(cameraPosition.getY() + translation.getY());
		cameraPosition.setZ(cameraPosition.getZ() + translation.getZ());
	}
	
	public void setPosition(float x, float y, float z){
		setPosition(new Vector3f(x, y, z));
	}
	
	public void setPosition(Vector3f position){
		cameraPosition.setX(position.getX());
		cameraPosition.setY(position.getY());
		cameraPosition.setZ(position.getZ());
	}
	
	public Vector3f getPosition(){
		return cameraPosition;
	}

}
