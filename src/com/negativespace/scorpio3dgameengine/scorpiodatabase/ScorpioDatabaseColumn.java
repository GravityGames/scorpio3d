package com.negativespace.scorpio3dgameengine.scorpiodatabase;

import java.util.ArrayList;

public class ScorpioDatabaseColumn {
	
	public enum Type{ Boolean, Integer, Text, Float, Byte, Short, Character};
	
	private String name;
	private Type type;
	private boolean primaryKey = false;
	private boolean autoincrement = false;
	private boolean notnull = false;
	private boolean unique = false;
	private String reference;
	private String defaultValue;

	public ArrayList<String> rows = new ArrayList<String>();
	public ArrayList<String> tags = new ArrayList<String>();
	
	public ScorpioDatabaseColumn(String name, Type type){
		this.name=name;
		this.type=type;
	}

	public String getName() {
		return name;
	}

	/*public void setName(String name) {
		this.name = name;
	}*/

	public Type getType() {
		return type;
	}

	/*public void setType(Type type) {
		this.type = type;
	}*/

	public boolean isPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}

	public boolean isAutoincrement() {
		return autoincrement;
	}

	public void setAutoincrement(boolean autoincrement) {
		this.autoincrement = autoincrement;
	}

	public boolean isNotnull() {
		return notnull;
	}

	public void setNotnull(boolean notnull) {
		this.notnull = notnull;
	}

	public boolean isUnique() {
		return unique;
	}

	public void setUnique(boolean unique) {
		this.unique = unique;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}
	
	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
