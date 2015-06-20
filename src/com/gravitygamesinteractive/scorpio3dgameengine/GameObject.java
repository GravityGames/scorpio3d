package com.gravitygamesinteractive.scorpio3dgameengine;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.gravitygamesinteractive.scorpio3dgameengine.rendering.ShaderProgram;

public class GameObject {

	Matrix4f translation;
	protected Transform transform;

	public ArrayList<GameObject> children = new ArrayList<GameObject>();
	public ArrayList<GameComponent> components = new ArrayList<GameComponent>();

	public int numChildren;
	protected int numComponents;

	public GameObject(){
		setTransform(new Transform());
	}

	public void input(){

		numChildren = children.size();
		numComponents = components.size();

		for(int i=0; i<numChildren; i++){
			children.get(i).input();
		}

		for(int i=0; i<numComponents; i++){
			components.get(i).input();
		}

	}

	public void update(){

		for(int i=0; i<numChildren; i++){
			children.get(i).update();
		}

		for(int i=0; i<numComponents; i++){
			components.get(i).update(getTransform());
		}

	}

	public void render(ShaderProgram shader){
		
		for(int i=0; i<numComponents; i++){
			components.get(i).render(getTransform(), shader);
		}

		for(int i=0; i<numChildren; i++){
			children.get(i).render(shader);
		}

	}

	public void addChild(GameObject child){
		children.add(child);
	}
	
	public void addComponent(GameComponent component){
		components.add(component);
	}

	public Transform getTransform() {
		return transform;
	}

	public void setTransform(Transform transform) {
		this.transform = transform;
	}

}
