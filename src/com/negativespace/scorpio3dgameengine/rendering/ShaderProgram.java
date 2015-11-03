package com.negativespace.scorpio3dgameengine.rendering;

import java.nio.FloatBuffer;
import java.util.HashMap;

import org.lwjgl.opengl.GL20;

public class ShaderProgram {
	
	public int program;
	
	public FloatBuffer matrix44Buffer;
	
	public HashMap<String, Integer> uniforms;
	
	public ShaderProgram(){
		program = GL20.glCreateProgram();
		
		uniforms = new HashMap<String, Integer>();
	}
	
	public void addUniform(String uniformName){
		int uniformLocation = GL20.glGetUniformLocation(program, uniformName);
		
		uniforms.put(uniformName, uniformLocation);
	}
	
	public int getUniformLocation(String uniformName){
		return uniforms.get(uniformName);
	}
	
	public void addShader(String shaderCode, int shaderType){
		int shader = GL20.glCreateShader(shaderType);
		
		GL20.glShaderSource(shader, shaderCode);
		GL20.glCompileShader(shader);
		GL20.glAttachShader(program, shader);
	}
	
	public void addVertexShader(String shaderCode){
		addShader(shaderCode, GL20.GL_VERTEX_SHADER);
	}
	
	public void addFragmentShader(String shaderCode){
		addShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
	}
	
	public void compile(){
		GL20.glLinkProgram(program);
	}
	
	public void bind(){
		GL20.glUseProgram(program);
	}

}
