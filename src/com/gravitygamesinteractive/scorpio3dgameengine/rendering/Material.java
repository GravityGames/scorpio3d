package com.gravitygamesinteractive.scorpio3dgameengine.rendering;

import org.lwjgl.util.vector.Vector3f;

public class Material {
	
	String name;
	Vector3f ambientColor;
	Vector3f diffuseColor;
	Vector3f specularColor;
	float specularExponent;
	float transparency = 1;
	int diffuseTexture;
	
	public Material(String name){
		this.name=name;
	}
	
	public Material(String name, Vector3f color){
		this.name=name;
		this.diffuseColor=color;
	}
	
	public Material(String name, Vector3f diffuseColor, int diffuseTexture){
		this.name=name;
		this.diffuseColor=diffuseColor;
		this.diffuseTexture=diffuseTexture;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector3f getAmbientColor() {
		return ambientColor;
	}

	public void setAmbientColor(Vector3f ambientColor) {
		this.ambientColor = ambientColor;
	}
	
	public Vector3f getDiffuseColor() {
		return diffuseColor;
	}

	public void setDiffuseColor(Vector3f color) {
		this.diffuseColor = color;
	}

	public Vector3f getSpecularColor() {
		return specularColor;
	}

	public void setSpecularColor(Vector3f specularColor) {
		this.specularColor = specularColor;
	}

	public float getSpecularExponent() {
		return specularExponent;
	}

	public void setSpecularExponent(float specularExponent) {
		this.specularExponent = specularExponent;
	}

	public float getTransparency() {
		return transparency;
	}

	public void setTransparency(float transparency) {
		this.transparency = transparency;
	}
	
	public int getDiffuseTexture() {
		return diffuseTexture;
	}

	public void setDiffuseTexture(int texture) {
		this.diffuseTexture = texture;
	}

}
