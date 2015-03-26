package com.gravitygamesinteractive.scorpio3dgameengine;

import java.nio.FloatBuffer;
import java.util.ArrayList;

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
	
	public void render(Transform transform, FloatBuffer matrix44Buffer, int modelMatrixLocation){
		for(int i=0; i<numChildren; i++){
			children.get(i).render(transform, matrix44Buffer, modelMatrixLocation);
		}
	}
	
	public void addChild(GameComponent child){
		children.add(child);
	}

}
