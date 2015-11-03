package com.negativespace.scorpio3dgameengine.rendering;

import org.lwjgl.util.vector.Vector3f;

public class PointLight extends Light{
	
	float constant;
	float linear;
	float exponent;
	Vector3f position;
	float range;
	
	public PointLight(){
		super();
	}
	
	public PointLight(float x, float y, float z, float intensity, float constant, float linear, float exponent, float posX, float posY, float posZ, float range){
		this(new Vector3f(x, y, z), intensity, constant, linear, exponent, new Vector3f(posX, posY, posZ), range);
	}
	
	public PointLight(Vector3f color, float intensity, float constant, float linear, float exponent, Vector3f position, float range){
		super(color, intensity);
		this.constant = constant;
		this.linear = linear;
		this.exponent = exponent;
		this.position = position;
		this.range=range;
	}

}
