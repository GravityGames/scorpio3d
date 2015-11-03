package com.negativespace.scorpio3dgameengine.scorpiodatabase;

import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ScorpioDatabase {

	boolean gameDataFile=false;

	public ArrayList<ScorpioDatabaseTable> tables = new ArrayList<ScorpioDatabaseTable>();

	public ScorpioDatabase(){

	}

	public void readXMLFile(String filename){

		try {

			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {

				String currentTableName;
				boolean populatingTable = false;
				boolean inRow = false;
				int tableToPopulate = -1;
				String currentColumn = "";
				ScorpioDatabaseRowManager row = new ScorpioDatabaseRowManager();

				public void startElement(String uri, String localName,String qName, 
						Attributes attributes) throws SAXException {

					if(!populatingTable){

						System.out.println("Start Element :" + qName);

						if (qName.equalsIgnoreCase("GAMEDATA")) {
							System.out.println("This is a gamedata file.");
							gameDataFile=true;
						}else if (qName.equalsIgnoreCase("TABLE")) {
							if(attributes.getLocalName(0).equalsIgnoreCase("NAME")){
								tables.add(new ScorpioDatabaseTable(attributes.getValue(0)));
								currentTableName = attributes.getValue(0);
								//System.out.println(attributes.getValue(0));
							}	
						}else if (qName.equalsIgnoreCase("COLUMN")) {
							String name = "";
							ScorpioDatabaseColumn.Type type = ScorpioDatabaseColumn.Type.Text;
							String defaultValue = "", reference = "";
							boolean primaryKey = false, unique = false, notnull = false, autoIncrement = false;

							int numAttributes = attributes.getLength();

							for(int i=0; i<numAttributes; i++){
								if(attributes.getLocalName(i).equalsIgnoreCase("NAME")){
									name = attributes.getValue(i);
								}else if(attributes.getLocalName(i).equalsIgnoreCase("TYPE")){
									if(attributes.getValue(i).equalsIgnoreCase("BOOLEAN")){
										type = ScorpioDatabaseColumn.Type.Boolean;
									}else if(attributes.getValue(i).equalsIgnoreCase("INTEGER")){
										type = ScorpioDatabaseColumn.Type.Integer;
									}else if(attributes.getValue(i).equalsIgnoreCase("INT")){
										type = ScorpioDatabaseColumn.Type.Integer;
									}else if(attributes.getValue(i).equalsIgnoreCase("TEXT")){
										type = ScorpioDatabaseColumn.Type.Text;
									}else if(attributes.getValue(i).equalsIgnoreCase("STRING")){
										type = ScorpioDatabaseColumn.Type.Text;
									}else if(attributes.getValue(i).equalsIgnoreCase("BYTE")){
										type = ScorpioDatabaseColumn.Type.Byte;
									}else if(attributes.getValue(i).equalsIgnoreCase("SHORT")){
										type = ScorpioDatabaseColumn.Type.Short;
									}else if(attributes.getValue(i).equalsIgnoreCase("CHARACTER")){
										type = ScorpioDatabaseColumn.Type.Character;
									}else if(attributes.getValue(i).equalsIgnoreCase("CHAR")){
										type = ScorpioDatabaseColumn.Type.Character;
									}
								}else if(attributes.getLocalName(i).equalsIgnoreCase("DEFAULT")){
									defaultValue = attributes.getValue(i);
								}else if(attributes.getLocalName(i).equalsIgnoreCase("REFERENCE")){
									reference = attributes.getValue(i);
								}else if(attributes.getLocalName(i).equalsIgnoreCase("PRIMARYKEY")){
									primaryKey = Boolean.parseBoolean(attributes.getValue(i));
								}else if(attributes.getLocalName(i).equalsIgnoreCase("UNIQUE")){
									unique = Boolean.parseBoolean(attributes.getValue(i));
								}else if(attributes.getLocalName(i).equalsIgnoreCase("NOTNULL")){
									notnull = Boolean.parseBoolean(attributes.getValue(i));
								}else if(attributes.getLocalName(i).equalsIgnoreCase("AUTOINCREMENT")){
									autoIncrement = Boolean.parseBoolean(attributes.getValue(i));
								}
							}

							int tableID = -1;

							for(int i=0; i<tables.size(); i++){
								if(tables.get(i).getName().equals(currentTableName)){
									tableID = i;
									break;
								}
							}

							ScorpioDatabaseColumn sdc = new ScorpioDatabaseColumn(name, type);

							sdc.setDefaultValue(defaultValue);
							sdc.setReference(reference);
							sdc.setPrimaryKey(primaryKey);
							sdc.setUnique(unique);
							sdc.setNotnull(notnull);
							sdc.setAutoincrement(autoIncrement);

							tables.get(tableID).columns.add(sdc);

							if(primaryKey){
								tables.get(tableID).setPrimaryKey(tables.get(tableID).columns.size()-1);
							}

						}else{
							for(int i=0; i<tables.size(); i++){
								if(qName.equals(tables.get(i).getName())){
									populatingTable = true;
									tableToPopulate = i;
									break;
								}
							}
						}
					}else{
						
						if(!inRow){
							if(qName.equalsIgnoreCase("ROW")){
								inRow=true;
								row.newRow();
								row.setTable(tables.get(tableToPopulate));
								row.setTableArray(tables);
								for(int i=0; i<tables.get(tableToPopulate).columns.size(); i++){
									if(attributes.getLength() > 0){
										if(attributes.getQName(0).equalsIgnoreCase("TAG")){
											tables.get(tableToPopulate).columns.get(i).tags.add((attributes.getValue(0)));
										}
									}else{
										tables.get(tableToPopulate).columns.get(i).tags.add("");
									}
								}
							}
						}else{
							for(int i=0; i<tables.get(tableToPopulate).columns.size(); i++){
								if(qName.equals(tables.get(tableToPopulate).columns.get(i).getName())){
									//tables.get(tableToPopulate).columns.get(i).add();
									//System.out.println(qName);
									currentColumn = qName;
									break;
								}
							}
						}

					}

				}

				public void endElement(String uri, String localName,
						String qName) throws SAXException {

					if(!populatingTable){

						System.out.println("End Element :" + qName);

					}else{
						if(inRow){
							if(qName.equalsIgnoreCase("ROW")){
								inRow=false;
								row.insertData();
							}else{
								currentColumn = "";
							}
						}else{
							for(int i=0; i<tables.size(); i++){
								if(qName.equals(tables.get(i).getName())){
									System.out.println("End Element :" + qName);
									populatingTable = false;
									tableToPopulate = -1;
									break;
								}
							}
						}
					}

				}

				public void characters(char ch[], int start, int length) throws SAXException {
					if(inRow && currentColumn.length() > 0){
						for(int i=0; i<tables.get(tableToPopulate).columns.size(); i++){
							if(currentColumn.equals(tables.get(tableToPopulate).columns.get(i).getName())){
								//System.out.println(currentColumn + " : " + new String(ch, start, length));
								row.addEntry(currentColumn, new String(ch, start, length));
								break;
							}
						}
					}
				}

			};

			saxParser.parse(filename, handler);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
