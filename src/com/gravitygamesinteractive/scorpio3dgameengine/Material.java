package com.gravitygamesinteractive.scorpio3dgameengine;

import org.lwjgl.util.vector.Vector3f;

public class Material {
	
	String name;
	Vector3f color;
	int texture;
	
	public Material(String name){
		this.name=name;
	}
	
	public Material(String name, Vector3f color){
		this.name=name;
		this.color=color;
	}
	
	public Material(String name, Vector3f color, int texture){
		this.name=name;
		this.color=color;
		this.texture=texture;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public int getTexture() {
		return texture;
	}

	public void setTexture(int texture) {
		this.texture = texture;
	}

}
