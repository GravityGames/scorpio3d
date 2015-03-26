package com.gravitygamesinteractive.scorpio3dgameengine;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ColoredMesh extends Mesh{

	int vaoId;
	int vboId;
	int vboiId;
	int vbocId;
	int vertexCount;
	int indicesCount;

	/*static ColoredVertex[] defaultVertices = {
			new ColoredVertex(-50f, 50f, 0f, 1f, 0f, 0f, 1f),
			new ColoredVertex(-50f, -50f, 0f, 0f, 1f, 0f, 1f),
			new ColoredVertex(50f, -50f, 0f, 0f, 0f, 1f, 1f),
			new ColoredVertex(50f, 50f, 0f, 1f, 1f, 1f, 1f)
		};*/

	static ColoredVertex[] defaultVertices = {
		new ColoredVertex(-0.5f, 0.5f, 0f, 1f, 0f, 0f, 1f),
		new ColoredVertex(-0.5f, -0.5f, 0f, 0f, 1f, 0f, 1f),
		new ColoredVertex(0.5f, -0.5f, 0f, 0f, 0f, 1f, 1f),
		new ColoredVertex(0.5f, 0.5f, 0f, 1f, 1f, 1f, 1f)
	};

	public ColoredVertex[] vertices;
	
	public int[] indices;

	static int[] defaultIndices = {
		// Left bottom triangle
		0, 1, 2,
		// Right top triangle
		2, 3, 0
	};

	public ColoredMesh(){
		this(defaultVertices);
	}

	public ColoredMesh(ColoredVertex[] vertices){
		this(vertices, defaultIndices);
	}
	
	public ColoredMesh(ColoredVertex[] vertices, int[] indices){
		this(vertices, indices, false);
	}

	public ColoredMesh(ColoredVertex[] vertices, int[] indices, boolean dynamicDraw){
		this.vertices = vertices;
		this.indices = indices;
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

		FloatBuffer meshBuffer = BufferUtils.createFloatBuffer(vertices.length * (Vertex.POSITION_SIZE + ColoredVertex.COLOR_SIZE));
		for(int i=0; i<vertices.length; i++){
			meshBuffer.put(vertices[i].getX());
			meshBuffer.put(vertices[i].getY());
			meshBuffer.put(vertices[i].getZ());
			meshBuffer.put(vertices[i].getR());
			meshBuffer.put(vertices[i].getG());
			meshBuffer.put(vertices[i].getB());
			meshBuffer.put(vertices[i].getA());
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
		if(dynamicDraw){
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, meshBuffer, GL15.GL_STREAM_DRAW);
		}else{
			GL15.glBufferData(GL15.GL_ARRAY_BUFFER, meshBuffer, GL15.GL_STATIC_DRAW);
		}

		GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Vertex.POSITION_SIZE+ColoredVertex.COLOR_SIZE, 0);
		GL20.glVertexAttribPointer(1, 4, GL11.GL_FLOAT, false, Vertex.POSITION_SIZE+ColoredVertex.COLOR_SIZE, Vertex.POSITION_SIZE);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

		GL30.glBindVertexArray(0);

		// Create a new VBO for the indices and select it (bind)
		vboiId = GL15.glGenBuffers();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		if(dynamicDraw){
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STREAM_DRAW);
		}else{
			GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL15.GL_STATIC_DRAW);
		}
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);

	}

	public void render(){
		// Bind to the VAO that has all the information about the vertices
		GL30.glBindVertexArray(vaoId);
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);

		// Bind to the index VBO that has all the information about the order of the vertices
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);

		// Draw the vertices
		GL11.glDrawElements(GL11.GL_TRIANGLES, indicesCount, GL11.GL_UNSIGNED_INT, 0);

		// Put everything back to default (deselect)
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
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
	}

	public void updateMesh(){
		// Update vertices in the VBO, first bind the VBO
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);

		FloatBuffer meshBuffer2 = BufferUtils.createFloatBuffer(vertices.length * (Vertex.POSITION_SIZE + ColoredVertex.COLOR_SIZE));
		for(int i=0; i<vertices.length; i++){
			meshBuffer2.put(vertices[i].getX());
			meshBuffer2.put(vertices[i].getY());
			meshBuffer2.put(vertices[i].getZ());
			meshBuffer2.put(vertices[i].getR());
			meshBuffer2.put(vertices[i].getG());
			meshBuffer2.put(vertices[i].getB());
			meshBuffer2.put(vertices[i].getA());
		}
		meshBuffer2.flip();

		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, meshBuffer2);
		//GL15.glBufferSubData(target, offset, data);

		// And of course unbind
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
		IntBuffer indicesBuffer2 = BufferUtils.createIntBuffer(indicesCount * 4);
		indicesBuffer2.put(indices);
		indicesBuffer2.flip();

		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboiId);
		//GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, indicesBuffer2, GL15.GL_STATIC_DRAW);
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, indicesBuffer2);
		// Deselect (bind to 0) the VBO
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
		
	}

}
