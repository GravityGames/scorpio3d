package com.gravitygamesinteractive.scorpio3dgameengine.rendering;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

import com.gravitygamesinteractive.scorpio3dgameengine.Main;
import com.gravitygamesinteractive.scorpio3dgameengine.ScorpioPalette;
 
public class TexturedMesh extends Mesh{
 
	int vaoId;
	int vboId;
	int vboiId;
	int vbocId;
	int vertexCount;
	int indicesCount;
	//int[] texIDs = new int[3];
	
	static TexturedVertex[] defaultVertices = {
			new TexturedVertex(-0.5f, 0.5f, 0f, 1f, 1f, 1f, 1f, 0f, 0f),
			new TexturedVertex(-0.5f, -0.5f, 0f, 1f, 1f, 1f, 1f, 0f, 1f),
			new TexturedVertex(0.5f, -0.5f, 0f, 1f, 1f, 1f, 1f, 1f, 1f),
			new TexturedVertex(0.5f, 0.5f, 0f, 1f, 1f, 1f, 1f, 1f, 0f)
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
		
		calcNormals(vertices, indices);
		
		ByteBuffer bb = ByteBuffer.allocateDirect(vertices.length * (Vertex.POSITION_SIZE + ColoredVertex.COLOR_SIZE + TexturedVertex.UV_SIZE + TexturedVertex.NORMAL_SIZE) * 4);
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
        	meshBuffer.put(vertices[i].getNormal());
        	//meshBuffer.put(vertices[i].getV());
        }
        
        meshBuffer.position(0);
        
     // OpenGL expects to draw vertices in counter clockwise order by default
        
        indicesCount = indices.length;
        bb = ByteBuffer.allocateDirect(indicesCount * 4);
        bb.order(ByteOrder.nativeOrder());
        IntBuffer indicesBuffer = bb.asIntBuffer();
        indicesBuffer.put(indices);
        indicesBuffer.position(0);
 
        vertexCount = vertices.length;
 
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
 
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, meshBuffer, GL15.GL_STATIC_DRAW);
        
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Vertex.POSITION_SIZE+ColoredVertex.COLOR_SIZE+TexturedVertex.UV_SIZE+TexturedVertex.NORMAL_SIZE, 0);
        GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, Vertex.POSITION_SIZE+ColoredVertex.COLOR_SIZE+TexturedVertex.UV_SIZE+TexturedVertex.NORMAL_SIZE, Vertex.POSITION_SIZE);
        GL20.glVertexAttribPointer(2, 2, GL11.GL_FLOAT, false, Vertex.POSITION_SIZE+ColoredVertex.COLOR_SIZE+TexturedVertex.UV_SIZE+TexturedVertex.NORMAL_SIZE, Vertex.POSITION_SIZE+ColoredVertex.COLOR_SIZE);
        GL20.glVertexAttribPointer(3, 3, GL11.GL_FLOAT, false, Vertex.POSITION_SIZE+ColoredVertex.COLOR_SIZE+TexturedVertex.UV_SIZE+TexturedVertex.NORMAL_SIZE, Vertex.POSITION_SIZE+ColoredVertex.COLOR_SIZE+TexturedVertex.UV_SIZE);
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
		GL20.glEnableVertexAttribArray(3);
		 
		// Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		 
		// Draw the vertices
		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);
		 
		// Put everything back to default (deselect)
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, texIDs[0]);
		//GL11.glBindTexture(GL11.GL_TEXTURE_2D, 4);
		GL30.glBindVertexArray(0);
	}
	
	public void render(int textureID, int normalTextureID){
		// Bind to the VAO that has all the information about the vertices
		
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, normalTextureID);
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		 
		// Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		 
		// Draw the vertices
		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);
		 
		// Put everything back to default (deselect)
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
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
	
	private void calcNormals(TexturedVertex[] vertices, int[] indices){
		for(int i = 0; i < indices.length; i += 3){
			int i0 = indices[i];
			int i1 = indices[i + 1];
			int i2 = indices[i + 2];
			
			Vector3f v1 = new Vector3f(0, 0, 1);
			Vector3f v2 = new Vector3f(0, 0, 1);
			
			Vector3f.sub(vertices[i1].getPosition(), vertices[i0].getPosition(), v1);
			Vector3f.sub(vertices[i2].getPosition(), vertices[i0].getPosition(), v2);
			
			Vector3f normal = new Vector3f(0, 0, 1);
			
			Vector3f.cross(v1, v2, normal);
			
			if(normal != null && (normal.x != 0 || normal.y!=0 || normal.z !=0))
				normal.normalise();
			
			Vector3f normal1 = new Vector3f();
			Vector3f normal2 = new Vector3f();
			Vector3f normal3 = new Vector3f();
			
			Vector3f.add(vertices[i0].getNormalVector(), normal, normal1);
			Vector3f.add(vertices[i1].getNormalVector(), normal, normal2);
			Vector3f.add(vertices[i2].getNormalVector(), normal, normal3);
			
			/*if(normal1 != null && (normal1.x != 0 || normal1.y!=0 || normal1.z !=0))
				normal1.normalise();
			
			if(normal2 != null && (normal2.x != 0 || normal2.y!=0 || normal2.z !=0))
				normal2.normalise();
			
			if(normal3 != null && (normal3.x != 0 || normal3.y!=0 || normal3.z !=0))
				normal3.normalise();*/
			
			vertices[i0].setNormal(normal1);
			vertices[i1].setNormal(normal2);
			vertices[i2].setNormal(normal3);
			
		}
		
		for(int i = 0; i < vertices.length; i++){
			Vector3f normal4 = new Vector3f();
			if(vertices[i].getNormal() != null && (vertices[i].getNormalVector().x != 0 || vertices[i].getNormalVector().y!=0 || vertices[i].getNormalVector().z !=0))
				normal4 = (Vector3f) vertices[i].getNormalVector().normalise();
				//vertices[i].setNormal((Vector3f)(vertices[i].getNormalVector().normalise()));
			vertices[i].setNormal(normal4);
			System.out.println(normal4);
		}
	}
 
}
