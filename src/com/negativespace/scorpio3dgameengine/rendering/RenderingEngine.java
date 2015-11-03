package com.negativespace.scorpio3dgameengine.rendering;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.negativespace.scorpio3dgameengine.GameObject;
import com.negativespace.scorpio3dgameengine.TextLoader;
import com.negativespace.scorpio3dgameengine.Util;

public class RenderingEngine {
	
	public static int normalPos;

	public ShaderProgram forwardAmbient;
	public ShaderProgram forwardDirectional;
	public ShaderProgram forwardPoint;
	public ShaderProgram forwardSpot;

	Matrix4f projectionMatrix, viewMatrix;
	FloatBuffer matrix44Buffer;

	//int projectionMatrixLocation;
	//int viewMatrixLocation;

	Vector3f ambientLight;

	float specularIntensity = 0f;
	float specularPower = 0f;
	Vector3f directionalColor = new Vector3f(1, 1, 1);
	float directionalIntensity = 0.8f;
	Vector3f directionalDirection = new Vector3f(1, 1, 1);

	static float aspectRatio;

	public int gameWidth = 1920;
	public int gameHeight = 1080;
	
	public int fps = 60;

	public static Camera mainCamera;

	public static ArrayList<Light> lights = new ArrayList<Light>();

	public RenderingEngine(){

		setClearColor(0.0f, 0.0f, 0.0f);

		setProjection((float)gameWidth / (float)gameHeight);

		viewMatrix = new Matrix4f();

		matrix44Buffer = BufferUtils.createFloatBuffer(16);

		mainCamera = new Camera(0, 0, -1);

		Matrix4f.translate(mainCamera.getPosition(), viewMatrix, viewMatrix);

		ambientLight = new Vector3f(0.2f, 0.2f, 0.2f);

		forwardAmbient = new ShaderProgram();
		forwardDirectional = new ShaderProgram();
		forwardPoint = new ShaderProgram();
		forwardSpot = new ShaderProgram();
		forwardAmbient.addVertexShader(TextLoader.loadText("assets/shaders/BasicVertex.vs"));
		forwardAmbient.addFragmentShader(TextLoader.loadText("assets/shaders/ForwardFragment.fs"));

		forwardDirectional.addVertexShader(TextLoader.loadText("assets/shaders/ForwardDirectional.vs"));
		forwardDirectional.addFragmentShader(TextLoader.loadText("assets/shaders/ForwardDirectional.fs"));
		
		forwardPoint.addVertexShader(TextLoader.loadText("assets/shaders/ForwardDirectional.vs"));
		forwardPoint.addFragmentShader(TextLoader.loadText("assets/shaders/forward_point.fs"));
		
		forwardSpot.addVertexShader(TextLoader.loadText("assets/shaders/ForwardDirectional.vs"));
		forwardSpot.addFragmentShader(TextLoader.loadText("assets/shaders/forward_spot.fs"));

		// vsId = Main.loadShader("assets/shaders/BasicVertex.vs", GL20.GL_VERTEX_SHADER);

		// Load the fragment shader
		//fsId = Main.loadShader("assets/shaders/PhongFragment.fs", GL20.GL_FRAGMENT_SHADER);

		//pId = GL20.glCreateProgram();
		//GL20.glAttachShader(pId, vsId);
		//GL20.glAttachShader(pId, fsId);

		// Position information will be attribute 0
		GL20.glBindAttribLocation(forwardAmbient.program, 0, "in_Position");
		// Color information will be attribute 1
		GL20.glBindAttribLocation(forwardAmbient.program, 1, "in_Color");
		GL20.glBindAttribLocation(forwardAmbient.program, 2, "in_TextureCoord");
		// GL20.glBindAttribLocation(forwardAmbient.program, 3, "in_Normal");

		GL20.glBindAttribLocation(forwardDirectional.program, 0, "in_Position");
		// Color information will be attribute 1
		GL20.glBindAttribLocation(forwardDirectional.program, 1, "in_Color");
		GL20.glBindAttribLocation(forwardDirectional.program, 2, "in_TextureCoord");
		GL20.glBindAttribLocation(forwardDirectional.program, 3, "in_Normal");
		
		GL20.glBindAttribLocation(forwardPoint.program, 0, "in_Position");
		// Color information will be attribute 1
		GL20.glBindAttribLocation(forwardPoint.program, 1, "in_Color");
		GL20.glBindAttribLocation(forwardPoint.program, 2, "in_TextureCoord");
		GL20.glBindAttribLocation(forwardPoint.program, 3, "in_Normal");
		
		GL20.glBindAttribLocation(forwardSpot.program, 0, "in_Position");
		// Color information will be attribute 1
		GL20.glBindAttribLocation(forwardSpot.program, 1, "in_Color");
		GL20.glBindAttribLocation(forwardSpot.program, 2, "in_TextureCoord");
		GL20.glBindAttribLocation(forwardSpot.program, 3, "in_Normal");

		//GL20.glLinkProgram(pId);

		forwardAmbient.compile();

		forwardDirectional.compile();
		
		forwardPoint.compile();
		
		forwardSpot.compile();

		//GL20.glValidateProgram(pId);

		forwardAmbient.addUniform("projectionMatrix");
		forwardAmbient.addUniform("viewMatrix");
		forwardAmbient.addUniform("modelMatrix");

		forwardDirectional.addUniform("projectionMatrix");
		forwardDirectional.addUniform("viewMatrix");
		forwardDirectional.addUniform("modelMatrix");
		forwardDirectional.addUniform("specularIntensity");
		forwardDirectional.addUniform("specularPower");
		forwardDirectional.addUniform("eyePos");
		forwardDirectional.addUniform("directionalLight.base.color");
		forwardDirectional.addUniform("directionalLight.base.intensity");
		forwardDirectional.addUniform("directionalLight.direction");
		forwardDirectional.addUniform("texture_normal");
		
		forwardPoint.addUniform("projectionMatrix");
		forwardPoint.addUniform("viewMatrix");
		forwardPoint.addUniform("modelMatrix");
		forwardPoint.addUniform("specularIntensity");
		forwardPoint.addUniform("specularPower");
		forwardPoint.addUniform("eyePos");
		forwardPoint.addUniform("pointLight.base.color");
		forwardPoint.addUniform("pointLight.base.intensity");
		forwardPoint.addUniform("pointLight.atten.constant");
		forwardPoint.addUniform("pointLight.atten.linear");
		forwardPoint.addUniform("pointLight.atten.exponent");
		forwardPoint.addUniform("pointLight.position");
		forwardPoint.addUniform("pointLight.range");
		forwardPoint.addUniform("texture_normal");
		
		forwardSpot.addUniform("projectionMatrix");
		forwardSpot.addUniform("viewMatrix");
		forwardSpot.addUniform("modelMatrix");
		forwardSpot.addUniform("specularIntensity");
		forwardSpot.addUniform("specularPower");
		forwardSpot.addUniform("eyePos");
		forwardSpot.addUniform("spotLight.pointLight.base.color");
		forwardSpot.addUniform("spotLight.pointLight.base.intensity");
		forwardSpot.addUniform("spotLight.pointLight.atten.constant");
		forwardSpot.addUniform("spotLight.pointLight.atten.linear");
		forwardSpot.addUniform("spotLight.pointLight.atten.exponent");
		forwardSpot.addUniform("spotLight.pointLight.position");
		forwardSpot.addUniform("spotLight.pointLight.range");
		forwardSpot.addUniform("spotLight.direction");
		forwardSpot.addUniform("spotLight.cutoff");
		forwardSpot.addUniform("texture_normal");

		//projectionMatrixLocation = GL20.glGetUniformLocation(shader.program, "projectionMatrix");
		//viewMatrixLocation = GL20.glGetUniformLocation(shader.program, "viewMatrix");
		//modelMatrixLocation = GL20.glGetUniformLocation(shader.program, "modelMatrix");
	}

	public void render(GameObject root){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		// Setup view matrix
		viewMatrix = new Matrix4f();

		Matrix4f.translate(mainCamera.getPosition(), viewMatrix, viewMatrix);

		forwardAmbient.bind();
		projectionMatrix.store(this.matrix44Buffer); this.matrix44Buffer.flip();
		GL20.glUniformMatrix4(forwardAmbient.getUniformLocation("projectionMatrix"), false, this.matrix44Buffer);
		viewMatrix.store(this.matrix44Buffer); this.matrix44Buffer.flip();
		GL20.glUniformMatrix4(forwardAmbient.getUniformLocation("viewMatrix"), false, this.matrix44Buffer);

		GL20.glUniform3f(GL20.glGetUniformLocation(forwardAmbient.program, "ambient_Light"), ambientLight.x, ambientLight.y, ambientLight.z);


		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glEnable(GL11.GL_DEPTH_TEST);

		forwardAmbient.matrix44Buffer = matrix44Buffer;

		//ambientLight = new Vector3f(ambientLight.x+0.001f, ambientLight.y+0.001f, ambientLight.z+0.001f);

		root.render(forwardAmbient);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		GL11.glDepthMask(false);
		GL11.glDepthFunc(GL11.GL_EQUAL);

		for(int i=0; i<lights.size(); i++){
			if(lights.get(i) instanceof DirectionalLight){
				DirectionalLight light = (DirectionalLight)lights.get(i);
				
				forwardDirectional.bind();
				
				ARBShaderObjects.glUniform1iARB(forwardDirectional.getUniformLocation("texture_normal"), 1);

				projectionMatrix.store(this.matrix44Buffer); this.matrix44Buffer.flip();
				GL20.glUniformMatrix4(forwardDirectional.getUniformLocation("projectionMatrix"), false, this.matrix44Buffer);
				viewMatrix.store(this.matrix44Buffer); this.matrix44Buffer.flip();
				GL20.glUniformMatrix4(forwardDirectional.getUniformLocation("viewMatrix"), false, this.matrix44Buffer);

				System.out.println(forwardDirectional.getUniformLocation("directionalLight.direction"));

				GL20.glUniform3f(forwardDirectional.getUniformLocation("directionalLight.base.color"), light.color.x, light.color.y, light.color.z);
				GL20.glUniform1f(forwardDirectional.getUniformLocation("directionalLight.base.intensity"), light.intensity);
				GL20.glUniform3f(forwardDirectional.getUniformLocation("directionalLight.direction"), light.direction.x, light.direction.y, light.direction.z);

				GL20.glUniform1f(forwardDirectional.getUniformLocation("specularIntensity"), specularIntensity);
				GL20.glUniform1f(forwardDirectional.getUniformLocation("specularPower"), specularPower);

				GL20.glUniform3f(forwardDirectional.getUniformLocation("eyePos"), mainCamera.getPosition().x, mainCamera.getPosition().y, mainCamera.getPosition().z);

				//forwardDirectional.matrix44Buffer = matrix44Buffer;
				root.render(forwardDirectional);
			}else if(lights.get(i) instanceof PointLight){
				PointLight light = (PointLight)lights.get(i);
				
				forwardPoint.bind();
				
				ARBShaderObjects.glUniform1iARB(forwardPoint.getUniformLocation("texture_normal"), 1);

				projectionMatrix.store(this.matrix44Buffer); this.matrix44Buffer.flip();
				GL20.glUniformMatrix4(forwardPoint.getUniformLocation("projectionMatrix"), false, this.matrix44Buffer);
				viewMatrix.store(this.matrix44Buffer); this.matrix44Buffer.flip();
				GL20.glUniformMatrix4(forwardPoint.getUniformLocation("viewMatrix"), false, this.matrix44Buffer);

				System.out.println(forwardPoint.getUniformLocation("pointLight.base.color"));

				GL20.glUniform3f(forwardPoint.getUniformLocation("pointLight.base.color"), light.color.x, light.color.y, light.color.z);
				GL20.glUniform1f(forwardPoint.getUniformLocation("pointLight.base.intensity"), light.intensity);
				GL20.glUniform1f(forwardPoint.getUniformLocation("pointLight.atten.constant"), light.constant);
				GL20.glUniform1f(forwardPoint.getUniformLocation("pointLight.atten.linear"), light.linear);
				GL20.glUniform1f(forwardPoint.getUniformLocation("pointLight.atten.exponent"), light.exponent);
				GL20.glUniform3f(forwardPoint.getUniformLocation("pointLight.position"), light.position.x, light.position.y, light.position.z);
				GL20.glUniform1f(forwardPoint.getUniformLocation("pointLight.range"), light.range);

				GL20.glUniform1f(forwardPoint.getUniformLocation("specularIntensity"), specularIntensity);
				GL20.glUniform1f(forwardPoint.getUniformLocation("specularPower"), specularPower);

				GL20.glUniform3f(forwardPoint.getUniformLocation("eyePos"), mainCamera.getPosition().x, mainCamera.getPosition().y, mainCamera.getPosition().z);

				//forwardDirectional.matrix44Buffer = matrix44Buffer;
				root.render(forwardPoint);
			}else if(lights.get(i) instanceof SpotLight){
				SpotLight light = (SpotLight)lights.get(i);
				
				forwardSpot.bind();
				
				ARBShaderObjects.glUniform1iARB(forwardSpot.getUniformLocation("texture_normal"), 1);

				projectionMatrix.store(this.matrix44Buffer); this.matrix44Buffer.flip();
				GL20.glUniformMatrix4(forwardSpot.getUniformLocation("projectionMatrix"), false, this.matrix44Buffer);
				viewMatrix.store(this.matrix44Buffer); this.matrix44Buffer.flip();
				GL20.glUniformMatrix4(forwardSpot.getUniformLocation("viewMatrix"), false, this.matrix44Buffer);

				System.out.println(forwardSpot.getUniformLocation("spotLight.pointLight.base.color"));

				GL20.glUniform3f(forwardSpot.getUniformLocation("spotLight.pointLight.base.color"), light.color.x, light.color.y, light.color.z);
				GL20.glUniform1f(forwardSpot.getUniformLocation("spotLight.pointLight.base.intensity"), light.intensity);
				GL20.glUniform1f(forwardSpot.getUniformLocation("spotLight.pointLight.atten.constant"), light.constant);
				GL20.glUniform1f(forwardSpot.getUniformLocation("spotLight.pointLight.atten.linear"), light.linear);
				GL20.glUniform1f(forwardSpot.getUniformLocation("spotLight.pointLight.atten.exponent"), light.exponent);
				GL20.glUniform3f(forwardSpot.getUniformLocation("spotLight.pointLight.position"), light.position.x, light.position.y, light.position.z);
				GL20.glUniform1f(forwardSpot.getUniformLocation("spotLight.pointLight.range"), light.range);
				GL20.glUniform3f(forwardSpot.getUniformLocation("spotLight.direction"), light.direction.x, light.direction.y, light.direction.z);
				GL20.glUniform1f(forwardSpot.getUniformLocation("spotLight.cutoff"), light.cutoff);

				GL20.glUniform1f(forwardSpot.getUniformLocation("specularIntensity"), specularIntensity);
				GL20.glUniform1f(forwardSpot.getUniformLocation("specularPower"), specularPower);

				GL20.glUniform3f(forwardSpot.getUniformLocation("eyePos"), mainCamera.getPosition().x, mainCamera.getPosition().y, mainCamera.getPosition().z);

				//forwardDirectional.matrix44Buffer = matrix44Buffer;
				root.render(forwardSpot);
			}
		}

		GL11.glDepthFunc(GL11.GL_LESS);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);

		GL20.glUseProgram(0);

		//    GL11.glDisable(GL11.GL_BLEND);
		//    GL11.glDisable(GL11.GL_DEPTH_TEST);

		//specularIntensity += 0.1f;
		//specularPower += 0.1f;
		//directionalIntensity += 0.1f;

		Display.sync(fps);
	}

	public void setClearColor(float r, float g, float b){
		GL11.glClearColor(r, g, b, 1.0f);
	}
	
	public void setClearColor(int r, int g, int b){
		GL11.glClearColor(r/255f, g/255f, b/255f, 1.0f);
	}
	
	public void setWidth(int width){
		gameWidth = width;
	}
	
	public void setHeight(int height){
		gameHeight = height;
	}
	
	public void setResolution(int width, int height){
		gameWidth = width;
		gameHeight = height;
	}
	
	public void setFPS(int fps){
		this.fps=fps;
	}

	public void setProjection(float aspectRatio){
		projectionMatrix = new Matrix4f();
		float fieldOfView = 60f;
		//aspectRatio = (float)gameWidth / (float)gameHeight;
		//aspectRatio = 1f;
		float near_plane = 0.1f;
		float far_plane = 1000f;

		float y_scale = Util.coTangent(Util.degreesToRadians(fieldOfView / 2f));
		float x_scale = y_scale / aspectRatio;
		float frustum_length = far_plane - near_plane;

		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((far_plane + near_plane) / frustum_length);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2 * near_plane * far_plane) / frustum_length);
		projectionMatrix.m33 = 0;
	}

	public void setAmbientLight(float r, float g, float b){
		ambientLight.x=r;
		ambientLight.y=g;
		ambientLight.z=b;
	}

	public void setCameraPosition(float x, float y, float z){
		mainCamera.cameraPosition.set(x, y, z);
	}
	
	public void addLight(Light light){
		lights.add(light);
	}

}
