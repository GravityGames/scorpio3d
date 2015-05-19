package com.gravitygamesinteractive.scorpio3dgameengine;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.gravitygamesinteractive.scorpio3dgameengine.Main;
import com.gravitygamesinteractive.scorpio3dgameengine.Mesh;
import com.gravitygamesinteractive.scorpio3dgameengine.TexturedMesh;
import com.gravitygamesinteractive.scorpio3dgameengine.Transform;

public class HUDMeshComponent extends HUDComponent{

Mesh mesh;
	
	int textureId;
	
	public HUDMeshComponent(Mesh mesh, int textureId){
		this.mesh = mesh;
		this.textureId = textureId;
	}
	
	/*@Override
	public void update(Transform transform){
		
		modelMatrix = new Matrix4f();
		
		Matrix4f.scale(transform.getScale(), modelMatrix, modelMatrix);
    	Matrix4f.translate(transform.getPosition(), modelMatrix, modelMatrix);
    	Matrix4f.rotate(Main.degreesToRadians(transform.getRotation().z), new Vector3f(0, 0, 1),
    	        modelMatrix, modelMatrix);
    	Matrix4f.rotate(Main.degreesToRadians(transform.getRotation().y), new Vector3f(0, 1, 0),
    	        modelMatrix, modelMatrix);
    	Matrix4f.rotate(Main.degreesToRadians(transform.getRotation().x), new Vector3f(1, 0, 0),
    	        modelMatrix, modelMatrix);
	}*/
	
	@Override
	public void render(){
		/*modelMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(modelMatrixLocation, false, matrix44Buffer);*/
        if(mesh instanceof TexturedMesh){
        	((TexturedMesh) mesh).render(textureId);
        }else{
        	mesh.render();
        }
	}
	
}
