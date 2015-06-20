package com.gravitygamesinteractive.scorpio3dgameengine.rendering;

import org.lwjgl.util.vector.Vector3f;

public class TexturedVertex extends ColoredVertex{
	
	//public float[] color = {1.0f, 1.0f, 1.0f, 1.0f};
	
	public float[] UVCoords = {0.0f, 0.0f};
	
	public float[] normal = {0.0f, 0.0f, 0.0f};
	
	public float[] tangent = {0.0f, 0.0f, 0.0f};
	
	//public static final byte COLOR_SIZE = 4 * 4;
	
	public static final byte UV_SIZE = 4 * 2;
	public static final byte NORMAL_SIZE = 4 * 3;
	public static final byte TANGENT_SIZE = 4 * 3;
	
	public TexturedVertex(float x, float y) {
		super(x, y);
		// TODO Auto-generated constructor stub
	}
	
	public TexturedVertex(float x, float y, float z) {
		super(x, y, z);
		// TODO Auto-generated constructor stub
	}
	
	public TexturedVertex(float x, float y, float r, float g, float b, float a, float u, float v){
		super(x, y, r, g, b, a);
		UVCoords[0] = u;
		UVCoords[1] = v;
	}
	public TexturedVertex(float x, float y, float z, float r, float g, float b, float a, float u, float v){
		super(x, y, z, r, g, b, a);
		UVCoords[0] = u;
		UVCoords[1] = v;
	}
	
	public TexturedVertex(float x, float y, int r, int g, int b, int a, float u, float v){
		super(x, y, r, g, b, a);
		UVCoords[0] = u;
		UVCoords[1] = v;
	}
	public TexturedVertex(float x, float y, float z, int r, int g, int b, int a, float u, float v){
		super(x, y, z, r, g, b, a);
		UVCoords[0] = u;
		UVCoords[1] = v;
	}
	
	public float getU(){
		return UVCoords[0];
	}
	
	public float getV(){
		return UVCoords[1];
	}
	
	public float[] getUV(){
		return UVCoords;
	}
	
	public void setUV(float u, float v){
		UVCoords[0] = u;
		UVCoords[1] = v;
	}
	
	public void setU(float u){
		UVCoords[0] = u;
	}
	
	public void setV(float v){
		UVCoords[1] = v;
	}
	
	public float[] getNormal(){
		return normal;
	}
	
	public float[] getTangent(){
		return tangent;
	}
	
	public Vector3f getNormalVector(){
		return new Vector3f(normal[0], normal[1], normal[2]);
	}
	
	public Vector3f getTangentVector(){
		return new Vector3f(tangent[0], tangent[1], tangent[2]);
	}
	
	public void setNormal(Vector3f normalV){
		normal[0]=normalV.x;
		normal[1]=normalV.y;
		normal[2]=normalV.z;
	}
}
