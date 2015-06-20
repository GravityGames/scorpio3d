package com.gravitygamesinteractive.scorpio3dgameengine;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import com.gravitygamesinteractive.scorpio3dgameengine.rendering.ShaderProgram;

public class GameComponent {
	
	public ArrayList<GameComponent> children = new ArrayList<GameComponent>();
	int numChildren;
	
	public void input(){
		
		numChildren = children.size();

		for(int i=0; i<numChildren; i++){
			children.get(i).input();
		}
		
	}
	
	public void update(Transform transform){
		for(int i=0; i<numChildren; i++){
			children.get(i).update(transform);
		}
	}
	
	public void render(Transform transform, ShaderProgram shader){
		for(int i=0; i<numChildren; i++){
			children.get(i).render(transform, shader);
		}
	}
	
	public void addChild(GameComponent child){
		children.add(child);
	}

}
