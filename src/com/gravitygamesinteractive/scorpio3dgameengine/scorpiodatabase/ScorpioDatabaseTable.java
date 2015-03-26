package com.gravitygamesinteractive.scorpio3dgameengine.scorpiodatabase;

import java.util.ArrayList;

public class ScorpioDatabaseTable {
	
	private String name;
	
	private int primaryKey;

	public ArrayList<ScorpioDatabaseColumn> columns = new ArrayList<ScorpioDatabaseColumn>();
	
	public ScorpioDatabaseTable(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public ArrayList<ScorpioDatabaseColumn> getColumns() {
		return columns;
	}
	
	public int getPrimaryKey(){
		return primaryKey;
	}
	
	public void setPrimaryKey(int primaryKey){
		this.primaryKey=primaryKey;
	}
	
}
