package com.gravitygamesinteractive.scorpio3dgameengine;

import java.util.ArrayList;

public class HUDObject {
	
	public ArrayList<HUDObject> children = new ArrayList<HUDObject>();
	public ArrayList<HUDComponent> components = new ArrayList<HUDComponent>();
	
	int numberOfChildren = 0;
	int numberOfComponents = 0;
	
	public HUDObject(){
		
	}
	
	public void input(){
		
		for(int i=0; i<numberOfChildren; i++){
			children.get(i).input();
		}
		
		for(int i=0; i<numberOfComponents; i++){
			components.get(i).input();
		}
		
	}
	
	public void update(){
		
		for(int i=0; i<numberOfChildren; i++){
			children.get(i).update();
		}
		
		for(int i=0; i<numberOfComponents; i++){
			components.get(i).update();
		}
		
	}
	
	public void render(){
	
		for(int i=0; i<numberOfChildren; i++){
			children.get(i).render();
		}
		
		for(int i=0; i<numberOfComponents; i++){
			components.get(i).render();
		}
		
	}
	
	public void addChild(HUDObject child){
		children.add(child);
		numberOfChildren++;
	}
	
	public void addComponent(HUDComponent component){
		components.add(component);
		numberOfComponents++;
	}

}
