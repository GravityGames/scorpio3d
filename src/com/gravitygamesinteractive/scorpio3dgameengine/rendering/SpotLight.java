package com.gravitygamesinteractive.scorpio3dgameengine.rendering;

import org.lwjgl.util.vector.Vector3f;

public class SpotLight extends Light{
	
	float constant;
	float linear;
	float exponent;
	Vector3f position;
	float range;
	public Vector3f direction;
	float cutoff;
	
	public SpotLight(){
		super();
	}
	
	public SpotLight(float x, float y, float z, float intensity, float constant, float linear, float exponent, float posX, float posY, float posZ, float range, float dirX, float dirY, float dirZ, float cutoff){
		this(new Vector3f(x, y, z), intensity, constant, linear, exponent, new Vector3f(posX, posY, posZ), range, new Vector3f(dirX, dirY, dirZ), cutoff);
	}
	
	public SpotLight(Vector3f color, float intensity, float constant, float linear, float exponent, Vector3f position, float range, Vector3f direction, float cutoff){
		super(color, intensity);
		this.constant = constant;
		this.linear = linear;
		this.exponent = exponent;
		this.position = position;
		this.range=range;
		this.direction = direction;
		this.cutoff = cutoff;
	}

}
