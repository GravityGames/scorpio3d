package com.gravitygamesinteractive.scorpio3dgameengine.rendering;

import org.lwjgl.util.vector.Vector3f;

public class DirectionalLight extends Light{
	
	public Vector3f direction;
	
	public DirectionalLight(){
		super();
	}
	
	public DirectionalLight(float x, float y, float z, float intensity, float dirX, float dirY, float dirZ){
		this(new Vector3f(x, y, z), intensity, new Vector3f(dirX, dirY, dirZ));
	}
	
	public DirectionalLight(Vector3f color, float intensity, Vector3f direction){
		super(color, intensity);
		this.direction = direction;
	}

}
