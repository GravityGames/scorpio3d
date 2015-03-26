package com.gravitygamesinteractive.scorpio3dgameengine;

import org.lwjgl.util.vector.Vector3f;

public class Material {
	
	String name;
	Vector3f color;
	
	public Material(String name, Vector3f color){
		this.name=name;
		this.color=color;
	}

}
