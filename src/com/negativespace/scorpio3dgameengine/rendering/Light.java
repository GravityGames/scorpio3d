package com.negativespace.scorpio3dgameengine.rendering;

import org.lwjgl.util.vector.Vector3f;

public class Light {
	
	public Vector3f color;
	public float intensity = 1f;
	
	public Light(){
		this(new Vector3f(1, 1, 1), 1f);
	}
	
	public Light(float x, float y, float z){
		this(new Vector3f(x, y, z), 1f);
	}
	
	public Light(Vector3f color, float intensity){
		this.color = color;
		this.intensity = intensity;
	}

}
