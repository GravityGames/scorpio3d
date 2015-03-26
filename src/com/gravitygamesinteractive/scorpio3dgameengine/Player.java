package com.gravitygamesinteractive.scorpio3dgameengine;

import java.nio.FloatBuffer;

import org.lwjgl.util.vector.Vector3f;

public class Player extends GameObject{
	
	public static TexturedVertex[] vertices = {
		new TexturedVertex(-0.5f, 1.7018f, 0f, 0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f),
		new TexturedVertex(-0.5f, 0f, 0f, 0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f),
		new TexturedVertex(0.5f, 0f, 0f, 0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f),
		new TexturedVertex(0.5f, 1.7018f, 0f, 0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f)};
	
	public Player(float x, float y, int[] textureIds, FloatBuffer matrix44Buffer, int modelMatrixLocation){
		getTransform().setPosition(new Vector3f(x, y, 0));
		TexturedMesh tm = new TexturedMesh(vertices);
		//tm.texIDs = textureIds;
		addComponent(new MeshComponent(tm, textureIds[1]));
		//addComponent(new MeshComponent(cm, textureIds[1], matrix44Buffer, modelMatrixLocation));
	}

}
