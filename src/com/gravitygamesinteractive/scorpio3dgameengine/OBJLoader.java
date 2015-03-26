package com.gravitygamesinteractive.scorpio3dgameengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

public class OBJLoader {
	
	public static TexturedMesh loadModel(){
		
		
		return null;
	}
	
	public static ColoredMesh loadModel(String filename, boolean dynamicDraw){
		return loadModel(filename, "", dynamicDraw, false);
	}
	
	public static ColoredMesh loadModel(String filename, String materialname, boolean dynamicDraw, boolean usesMTL){
		ArrayList<Vector3f> vertexPositions = new ArrayList<Vector3f>();
		ArrayList<Vector3f> vertexColors = new ArrayList<Vector3f>();
		ArrayList<Material> materials = new ArrayList<Material>();
		int activeMaterial = 0;
		if(usesMTL){
			materials = loadMaterials(materialname);
		}
		ArrayList<ColoredVertex> vertices = new ArrayList<ColoredVertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		ColoredMesh mesh = null;
		try {
			//InputStream in = new FileInputStream(filename);
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			String line;
			String[] splitLine;
			while((line = reader.readLine()) != null){
				if(line.startsWith("v")){
					splitLine = line.split(" ");
					vertexPositions.add(new Vector3f(Float.valueOf(splitLine[1]), Float.valueOf(splitLine[2]), Float.valueOf(splitLine[3])));
					//if(!usesMTL){
						vertexColors.add(new Vector3f(1, 1, 1));
					//}
					//vertices.add(new ColoredVertex(Float.valueOf(splitLine[1]), Float.valueOf(splitLine[2]), Float.valueOf(splitLine[3])));
				}else if(line.startsWith("f")){
					splitLine = line.split(" ");
					indices.add(Integer.parseInt(splitLine[1])-1);
					indices.add(Integer.parseInt(splitLine[2])-1);
					indices.add(Integer.parseInt(splitLine[3])-1);
					if(usesMTL){
						vertexColors.set(Integer.parseInt(splitLine[1])-1, materials.get(activeMaterial).color);
						vertexColors.set(Integer.parseInt(splitLine[2])-1, materials.get(activeMaterial).color);
						vertexColors.set(Integer.parseInt(splitLine[3])-1, materials.get(activeMaterial).color);
					}
				}else if(line.startsWith("usemtl") && usesMTL){
					splitLine = line.split(" ");
					for(int i=0; i<materials.size(); i++){
						if(materials.get(i).name.equals(splitLine[1])){
							System.out.println("test");
							activeMaterial = i;
							break;
						}
					}
				}
			}
			for(int i=0; i<vertexPositions.size(); i++){
				vertices.add(new ColoredVertex(vertexPositions.get(i).x, vertexPositions.get(i).y, vertexPositions.get(i).z, vertexColors.get(i).x, vertexColors.get(i).y, vertexColors.get(i).z, 1.0f));
			}
			ColoredVertex[] v = new ColoredVertex[vertices.size()];
			vertices.toArray(v);
			Integer[] j = new Integer[indices.size()];
			int[] i = new int[indices.size()];
			indices.toArray(j);
			for(int k=0; k<i.length; k++){
				i[k] = j[k].intValue();
			}
			
			mesh = new ColoredMesh(v, i, dynamicDraw);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mesh;
		
	}
	
	private static ArrayList<Material> loadMaterials(String filename){
		
		ArrayList<Material>materials = new ArrayList<Material>();
		
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(new File(filename)));
			String line;
			String[] splitLine;
			String materialName = "";
			while((line = reader.readLine()) != null){
				if(line.startsWith("newmtl")){
					splitLine = line.split(" ");
					materialName = splitLine[1];
				}else if(line.startsWith("Kd")){
					splitLine = line.split(" ");
					materials.add(new Material(materialName, new Vector3f(Float.valueOf(splitLine[1]), Float.valueOf(splitLine[2]), Float.valueOf(splitLine[3]))));
				}
			}
			reader.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(materials.size());
		return materials;
		
	}

}
