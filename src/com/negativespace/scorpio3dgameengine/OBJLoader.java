package com.negativespace.scorpio3dgameengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.negativespace.scorpio3dgameengine.rendering.ColoredMesh;
import com.negativespace.scorpio3dgameengine.rendering.ColoredVertex;
import com.negativespace.scorpio3dgameengine.rendering.Material;
import com.negativespace.scorpio3dgameengine.rendering.TexturedMesh;
import com.negativespace.scorpio3dgameengine.rendering.TexturedVertex;

public class OBJLoader {
	
	public static TexturedMesh loadModel(String filename){
		ArrayList<Vector3f> vertexPositions = new ArrayList<Vector3f>();
		ArrayList<Vector3f> vertexColors = new ArrayList<Vector3f>();
		ArrayList<Material> materials = new ArrayList<Material>();
		int activeMaterial = 0;
		ArrayList<Vector2f> texturePositions = new ArrayList<Vector2f>();
		ArrayList<Vector3f> normalPositions = new ArrayList<Vector3f>();
		ArrayList<TexturedVertex> vertices = new ArrayList<TexturedVertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();
		ArrayList<Integer> textureIndices = new ArrayList<Integer>();
		
		TexturedMesh mesh = null;
		
		try {
			//InputStream in = new FileInputStream(filename);
			BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
			String line;
			String[] splitLine;
			while((line = reader.readLine()) != null){
				if(line.startsWith("mtllib")){
					splitLine = line.split(" ");
					materials = loadMaterials("assets/materials/" + splitLine[1]);
				}else if(line.startsWith("v") && !line.startsWith("vt")){
					splitLine = line.split(" ");
					vertexPositions.add(new Vector3f(Float.valueOf(splitLine[1])/2, Float.valueOf(splitLine[2])/2, Float.valueOf(splitLine[3])/2));
					//if(!usesMTL){
					vertexColors.add(new Vector3f(1, 1, 1));
					//}
					//vertices.add(new ColoredVertex(Float.valueOf(splitLine[1]), Float.valueOf(splitLine[2]), Float.valueOf(splitLine[3])));
				}else if(line.startsWith("vt")){
					splitLine = line.split(" ");
					texturePositions.add(new Vector2f(Float.valueOf(splitLine[1]), Math.abs(1.0f-Float.valueOf(splitLine[2]))));
				}else if(line.startsWith("usemtl")){
					splitLine = line.split(" ");
					for(int i=0; i<materials.size(); i++){
						if(materials.get(i).getName().equals(splitLine[1])){
							System.out.println("test");
							activeMaterial = i;
							break;
						}
					}
				}else if(line.startsWith("f")){
					splitLine = line.split(" ");
					String[] splitLine2;
					for(int i=1; i<splitLine.length; i++){
						System.out.println(splitLine[i]);
						splitLine2 = splitLine[i].split("/");
						
						boolean vertexExists=false;
						
						for(int j=0; j<vertices.size(); j++){
							if(splitLine2.length==1){
								if(vertices.get(j).position[0]==vertexPositions.get(Integer.parseInt(splitLine2[0])-1).x &&
										vertices.get(j).position[1]==vertexPositions.get(Integer.parseInt(splitLine2[0])-1).y &&
										vertices.get(j).position[2]==vertexPositions.get(Integer.parseInt(splitLine2[0])-1).z){
									indices.add(j);
									vertexExists=true;
									break;
								}
							}else{
							if(vertices.get(j).position[0]==vertexPositions.get(Integer.parseInt(splitLine2[0])-1).x &&
									vertices.get(j).position[1]==vertexPositions.get(Integer.parseInt(splitLine2[0])-1).y &&
									vertices.get(j).position[2]==vertexPositions.get(Integer.parseInt(splitLine2[0])-1).z &&
									vertices.get(j).getU()==texturePositions.get(Integer.parseInt(splitLine2[1])-1).x &&
									vertices.get(j).getV()==texturePositions.get(Integer.parseInt(splitLine2[1])-1).y){
								indices.add(j);
								vertexExists=true;
								break;
							}
							}
						}
						if(!vertexExists){
							if(splitLine2.length==1){
								vertices.add(new TexturedVertex(vertexPositions.get(Integer.parseInt(splitLine2[0])-1).x, vertexPositions.get(Integer.parseInt(splitLine2[0])-1).y, vertexPositions.get(Integer.parseInt(splitLine2[0])-1).z, 1.0f, 1.0f, 1.0f, 1.0f, 0, 0));
							}else{
								vertices.add(new TexturedVertex(vertexPositions.get(Integer.parseInt(splitLine2[0])-1).x, vertexPositions.get(Integer.parseInt(splitLine2[0])-1).y, vertexPositions.get(Integer.parseInt(splitLine2[0])-1).z, 1.0f, 1.0f, 1.0f, 1.0f, texturePositions.get(Integer.parseInt(splitLine2[1])-1).x, texturePositions.get(Integer.parseInt(splitLine2[1])-1).y));
							}
							indices.add(vertices.size()-1);
						}
						
						//System.out.println(splitLine2[0] + "   " + splitLine2[1]);
						/*indices.add(Integer.parseInt(splitLine2[0])-1);
						if(!splitLine2.equals("") && splitLine2.length>=2){
							textureIndices.add(Integer.parseInt(splitLine2[1])-1);
						}*/
					}
					
					//continue;
					//vertexColors.set(Integer.parseInt(splitLine[1])-1, materials.get(activeMaterial).color);
					//vertexColors.set(Integer.parseInt(splitLine[2])-1, materials.get(activeMaterial).color);
					//vertexColors.set(Integer.parseInt(splitLine[3])-1, materials.get(activeMaterial).color);
				}
				
			}
			
			/*for(int i=0; i<vertexPositions.size(); i++){
				vertices.add(new TexturedVertex(vertexPositions.get(i).x, vertexPositions.get(i).y, vertexPositions.get(i).z));
			}
			
			if(texturePositions.size()>0){
				for(int i=0; i<indices.size(); i++){
					vertices.get(indices.get(i)).setUV(texturePositions.get(textureIndices.get(i)).x, texturePositions.get(textureIndices.get(i)).y);
				}
			}*/
			
			TexturedVertex[] v = new TexturedVertex[vertices.size()];
			vertices.toArray(v);
			int[] i = new int[indices.size()];
			for(int k=0; k<i.length; k++){
				i[k] = indices.get(k).intValue();
			}
			
			mesh = new TexturedMesh(v, i);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mesh;
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
						vertexColors.set(Integer.parseInt(splitLine[1])-1, materials.get(activeMaterial).getDiffuseColor());
						vertexColors.set(Integer.parseInt(splitLine[2])-1, materials.get(activeMaterial).getDiffuseColor());
						vertexColors.set(Integer.parseInt(splitLine[3])-1, materials.get(activeMaterial).getDiffuseColor());
					}
				}else if(line.startsWith("usemtl") && usesMTL){
					splitLine = line.split(" ");
					for(int i=0; i<materials.size(); i++){
						if(materials.get(i).getName().equals(splitLine[1])){
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
			int currentMaterial = 0;
			while((line = reader.readLine()) != null){
				if(line.startsWith("newmtl")){
					splitLine = line.split(" ");
					materialName = splitLine[1];
					materials.add(new Material(materialName));
					currentMaterial = materials.size()-1;
				}else if(line.startsWith("Kd")){
					splitLine = line.split(" ");
					//materials.add(new Material(materialName, new Vector3f(Float.valueOf(splitLine[1]), Float.valueOf(splitLine[2]), Float.valueOf(splitLine[3]))));
					materials.get(currentMaterial).setDiffuseColor(new Vector3f(Float.valueOf(splitLine[1]), Float.valueOf(splitLine[2]), Float.valueOf(splitLine[3])));
				}else if(line.startsWith("map_Kd")){
					splitLine = line.split(" ");
					if(splitLine[1].endsWith(".png") || splitLine[1].endsWith(".PNG")){
						materials.get(currentMaterial).setDiffuseTexture(TextureLoader.loadPNG("assets/textures/" + splitLine[1], GL13.GL_TEXTURE0));
					}
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
