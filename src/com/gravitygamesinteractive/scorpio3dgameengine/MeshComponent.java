package com.gravitygamesinteractive.scorpio3dgameengine;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class MeshComponent extends GameComponent{
	
	private Mesh mesh;
	
	private Matrix4f modelMatrix;
	
	//private FloatBuffer matrix44Buffer;
	
	int textureId;
	
	//int modelMatrixLocation;
	
	public MeshComponent(Mesh mesh, int textureId){
		this.setMesh(mesh);
		this.textureId = textureId;
		//this.matrix44Buffer=matrix44Buffer;
		//this.modelMatrixLocation=modelMatrixLocation;
	}
	
	@Override
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
    	
    	/*// Scale, translate and rotate model
    	Matrix4f.scale(modelScale, modelMatrix, modelMatrix);
    	Matrix4f.translate(modelPos, modelMatrix, modelMatrix);
    	Matrix4f.rotate(this.degreesToRadians(modelAngle.z), new Vector3f(0, 0, 1),
    	        modelMatrix, modelMatrix);
    	Matrix4f.rotate(this.degreesToRadians(modelAngle.y), new Vector3f(0, 1, 0),
    	        modelMatrix, modelMatrix);
    	Matrix4f.rotate(this.degreesToRadians(modelAngle.x), new Vector3f(1, 0, 0),
    	        modelMatrix, modelMatrix);*/
	}
	
	@Override
	public void render(Transform transform, FloatBuffer matrix44Buffer, int modelMatrixLocation){
		modelMatrix.store(matrix44Buffer); matrix44Buffer.flip();
        GL20.glUniformMatrix4(modelMatrixLocation, false, matrix44Buffer);
        if(getMesh() instanceof TexturedMesh){
        	((TexturedMesh) getMesh()).render(textureId);
        }else{
        	getMesh().render();
        }
	}

	public Mesh getMesh() {
		return mesh;
	}

	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}

}
