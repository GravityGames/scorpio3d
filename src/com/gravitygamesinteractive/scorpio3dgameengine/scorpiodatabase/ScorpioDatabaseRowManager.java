package com.gravitygamesinteractive.scorpio3dgameengine.scorpiodatabase;

import java.util.ArrayList;

public class ScorpioDatabaseRowManager {

	public ScorpioDatabaseTable sdt;

	public ArrayList<String> columnid = new ArrayList<String>();
	public ArrayList<String> data = new ArrayList<String>();
	public ArrayList<Boolean> alreadyAdded = new ArrayList<Boolean>();
	public ArrayList<ScorpioDatabaseTable> tables = new ArrayList<ScorpioDatabaseTable>();

	public boolean primaryAlreadyAdded = false;

	ScorpioDatabaseRowManager(){

	}

	public void setTable(ScorpioDatabaseTable table){
		sdt = table;
	}

	public void setTableArray(ArrayList<ScorpioDatabaseTable> tables){
		this.tables=tables;
	}

	public void newRow(){
		primaryAlreadyAdded = false;
		alreadyAdded.clear();
		columnid.clear();
		data.clear();
	}

	public void addEntry(String columnName, String columnData){
		columnid.add(columnName);
		data.add(columnData);
		alreadyAdded.add(false);
	}

	public String lookupText(String table, String tag){

		for(int i=0; i<tables.size(); i++){
			if(tables.get(i).getName().equals(table)){
				for(int j=0; j<tables.get(i).columns.size(); j++){
					for(int k=0; k<tables.get(i).columns.get(j).tags.size(); k++){
						if(tables.get(i).columns.get(j).tags.get(k).equals(tag)){
							return tables.get(i).columns.get(j).rows.get(k);
						}
					}
				}
			}
		}

		return tag;
	}

	public void insertData(){

		int dataLength = 0;

		for(int i=0; i<sdt.columns.size(); i++){
			if(sdt.columns.get(i).isPrimaryKey()){
				dataLength = sdt.columns.get(i).rows.size()+1;
			}
		}
		for(int i=0; i<sdt.columns.size(); i++){
			for(int j=0; j<columnid.size(); j++){
				/*if(sdt.columns.get(i).isPrimaryKey() && !primaryAlreadyAdded){
					if(sdt.columns.get(i).getName().equals(columnid.get(j)) && !alreadyAdded.get(j)){
						sdt.columns.get(i).rows.add(data.get(j));
						alreadyAdded.set(j, true);
						primaryAlreadyAdded = true;
					}else{
						if(sdt.columns.get(i).isAutoincrement()){
							if(sdt.columns.get(i).getType() == ScorpioDatabaseColumn.Type.Integer){
								int value = Integer.parseInt(sdt.columns.get(i).rows.get(sdt.columns.get(i).rows.size()-1))+1;
								sdt.columns.get(i).rows.add(Integer.toString(value));
							}
							primaryAlreadyAdded=true;
						}else{
							sdt.columns.get(i).rows.add(sdt.columns.get(i).getDefaultValue());
						}
					}
				}else{*/
				if(sdt.columns.get(i).getName().equals(columnid.get(j)) && !alreadyAdded.get(j)){
					if(!sdt.columns.get(i).getReference().equals("")){
						System.out.println(sdt.columns.get(i).getReference());
						sdt.columns.get(i).rows.add(lookupText(sdt.columns.get(i).getReference(), data.get(j)));
					}else{
						sdt.columns.get(i).rows.add(data.get(j));
						alreadyAdded.set(j, true);
					}
				}
				/*}*/
			}

			if(sdt.columns.get(i).rows.size() < dataLength){
				if(sdt.columns.get(i).isPrimaryKey()){
					if(sdt.columns.get(i).isAutoincrement()){
						if(sdt.columns.get(i).getType() == ScorpioDatabaseColumn.Type.Integer){
							if(sdt.columns.get(i).rows.size()>0){
								int value = Integer.parseInt(sdt.columns.get(i).rows.get(sdt.columns.get(i).rows.size()-1))+1;
								sdt.columns.get(i).rows.add(Integer.toString(value));
							}else{
								sdt.columns.get(i).rows.add("0");
							}
						}
						primaryAlreadyAdded=true;
					}else{
						sdt.columns.get(i).rows.add(sdt.columns.get(i).getDefaultValue());
					}
				}else{
					sdt.columns.get(i).rows.add(sdt.columns.get(i).getDefaultValue());
				}
			}
		}
	}

}
