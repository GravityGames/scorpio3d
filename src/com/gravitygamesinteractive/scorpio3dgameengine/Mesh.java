package com.gravitygamesinteractive.scorpio3dgameengine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
 
public class Mesh {
 
	int vaoId;
	int vboId;
	int vboiId;
	int vertexCount;
	int indicesCount;
	
	public static Vertex[] defaultVertices = {new Vertex(-50f, 50f, 0f),
	new Vertex(-50f, -50f, 0f),
	new Vertex(50f, -50f, 0f),
	new Vertex(50f, 50f, 0f)};
	
	public Vertex[] vertices;
	
	public Mesh(){
		this(defaultVertices);
	}
 
	public Mesh(Vertex[] vertices){
		this.vertices = vertices;
        /*float[] vertices = {
                -50f, 50f, 0f,
                -50f, -50f, 0f,
                50f, -50f, 0f,
 
                50f, -50f, 0f,
                50f, 50f, 0f,
                -50f, 50f, 0f
        };*/
		
		/*float[] vertices = {
                -50f, 50f, 0f,
                -50f, -50f, 0f,
                50f, -50f, 0f,
                50f, 50f, 0f
        };*/
		
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * (Vertex.POSITION_SIZE)*4);
		bb.order(ByteOrder.nativeOrder());
		
		FloatBuffer verticesBuffer = bb.asFloatBuffer();
		
        //FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.POSITION_SIZE);
        for(int i=0; i<vertices.length; i++){
        	verticesBuffer.put(vertices[i].getX());
        	verticesBuffer.put(vertices[i].getY());
        	verticesBuffer.put(vertices[i].getZ());
        }
        //verticesBuffer.flip();
        
     // OpenGL expects to draw vertices in counter clockwise order by default
        int[] indices = {
                // Left bottom triangle
                0, 1, 2,
                // Right top triangle
                2, 3, 0
        };
        indicesCount = indices.length;
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indicesCount * 4);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
 
        vertexCount = vertices.length;
 
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
 
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL30.glBindVertexArray(0);
        
     // Create a new VBO for the indices and select it (bind)
        vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
 
	public void render(){
		// Bind to the VAO that has all the information about the vertices
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		 
		// Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		 
		// Draw the vertices
		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);
		 
		// Put everything back to default (deselect)
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}
 
	public void destroy(){
        GL20.glDisableVertexAttribArray(0);
 
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboId);
        
     // Delete the index VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboiId);
 
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoId);
	}
 
}
