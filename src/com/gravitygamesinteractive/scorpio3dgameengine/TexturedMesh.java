package com.gravitygamesinteractive.scorpio3dgameengine;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
 
public class TexturedMesh extends Mesh{
 
	int vaoId;
	int vboId;
	int vboiId;
	int vbocId;
	int vertexCount;
	int indicesCount;
	//int[] texIDs = new int[3];
	
	static TexturedVertex[] defaultVertices = {
			new TexturedVertex(50f, 50f, 0f, 1f, 1f, 1f, 1f, 0f, 0f),
			new TexturedVertex(50f, -50f, 0f, 1f, 1f, 1f, 1f, 0f, 1f),
			new TexturedVertex(150f, -50f, 0f, 1f, 1f, 1f, 1f, 1f, 1f),
			new TexturedVertex(150f, 50f, 0f, 1f, 1f, 1f, 1f, 1f, 0f)
		};
	
	static int[] defaultIndices = {
            // Left bottom triangle
            0, 1, 2,
            // Right top triangle
            2, 3, 0
    };
	
	public TexturedMesh(){
		this(defaultVertices);
	}
	
	public TexturedMesh(TexturedVertex[] vertices){
		this(vertices, defaultIndices);
	}
 
	public TexturedMesh(TexturedVertex[] vertices, int[] indices){
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
		
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * (Vertex.POSITION_SIZE + ColoredVertex.COLOR_SIZE + TexturedVertex.UV_SIZE) * 4);
		bb.order(ByteOrder.nativeOrder());
		
		FloatBuffer meshBuffer = bb.asFloatBuffer();
		
        //FloatBuffer meshBuffer = BufferUtils.createFloatBuffer(vertices.length * (Vertex.POSITION_SIZE + ColoredVertex.COLOR_SIZE + TexturedVertex.UV_SIZE));
        for(int i=0; i<vertices.length; i++){
        	meshBuffer.put(vertices[i].getX());
        	meshBuffer.put(vertices[i].getY());
        	meshBuffer.put(vertices[i].getZ());
        	meshBuffer.put(vertices[i].getR());
        	meshBuffer.put(vertices[i].getG());
        	meshBuffer.put(vertices[i].getB());
        	meshBuffer.put(vertices[i].getA());
        	meshBuffer.put(vertices[i].getUV());
        	//meshBuffer.put(vertices[i].getV());
        }
        
        meshBuffer.flip();
        
     // OpenGL expects to draw vertices in counter clockwise order by default
        
        indicesCount = indices.length;
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indicesCount * 4);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
 
        vertexCount = vertices.length;
 
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
 
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, meshBuffer, GL15.GL_STATIC_DRAW);
        
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Vertex.POSITION_SIZE+ColoredVertex.COLOR_SIZE+TexturedVertex.UV_SIZE, 0);
        GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, Vertex.POSITION_SIZE+ColoredVertex.COLOR_SIZE+TexturedVertex.UV_SIZE, Vertex.POSITION_SIZE);
        GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, Vertex.POSITION_SIZE+ColoredVertex.COLOR_SIZE+TexturedVertex.UV_SIZE, Vertex.POSITION_SIZE+ColoredVertex.COLOR_SIZE);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        
        GL30.glBindVertexArray(0);
        
     // Create a new VBO for the indices and select it (bind)
        vboiId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        
        ScorpioPalette sp = new ScorpioPalette();
        
        sp.loadPalette("assets/textures/test.spal");
        
        GL20.glLinkProgram(Main.pId);
        GL20.glValidateProgram(Main.pId);
        
	}
 
	public void render(int textureID){
		// Bind to the VAO that has all the information about the vertices
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		 
		// Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		 
		// Draw the vertices
		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);
		 
		// Put everything back to default (deselect)
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, texIDs[0]);
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, 4);
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
        
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vbocId);
        
        //GL11.glDeleteTextures(texIDs[0]);
        //GL11.glDeleteTextures(texIDs[1]);
        //GL11.glDeleteTextures(texIDs[2]);
	}
 
}
