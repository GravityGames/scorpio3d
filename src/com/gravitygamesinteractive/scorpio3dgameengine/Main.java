package com.gravitygamesinteractive.scorpio3dgameengine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.gravitygamesinteractive.scorpio3dgameengine.rendering.ColoredMesh;
import com.gravitygamesinteractive.scorpio3dgameengine.rendering.Mesh;
import com.gravitygamesinteractive.scorpio3dgameengine.rendering.TexturedMesh;
import com.gravitygamesinteractive.scorpio3dgameengine.rendering.Vertex;

public class Main {
	
	private static String OS = "Windows";
	private static String DocumentsFolder;
	public static ColoredMesh m;
	public static Mesh m2;
	public static TexturedMesh m3;
	//public static ColoredMesh m4;
	private Path userfolder;
	private Path modsfolder;
	private Path savesfolder;
	
	public static Vector3f cameraPos;
	
	/*public static Vertex[] defaultVertices2 = {new Vertex(-150f, 50f, 0f),
		new Vertex(-150f, -50f, 0f),
		new Vertex(-50f, -50f, 0f),
		new Vertex(-50f, 50f, 0f)};*/
	
	public static Vertex[] defaultVertices2 = {new Vertex(-0.5f, 1.7018f, 0f),
		new Vertex(-0.5f, 0f, 0f),
		new Vertex(0.5f, 0f, 0f),
		new Vertex(0.5f, 1.7018f, 0f)};
	
	public static int screenWidth = 1920/2;
	public static int screenHeight = 1080/2;
	
	public static int gameWidth = 1920;
	public static int gameHeight = 1080;
	
	public static int vsId, fsId, pId;
	
	public static boolean isWindows;
	
	public static FloatBuffer matrix44Buffer = null;
	
	private final static double PI = 3.14159265358979323846;
	
	Matrix4f projectionMatrix, viewMatrix, modelMatrix;
	
	int projectionMatrixLocation, viewMatrixLocation;
	
	public static int modelMatrixLocation;
	
	private Vector3f modelPos = null;
	private Vector3f modelAngle = null;
	private Vector3f modelScale = null;
	
	public void start(){
		OS = System.getProperty("os.name");
		isWindows = OS.startsWith("Windows");
		System.out.println(OS);
		DocumentsFolder = System.getProperty("user.home")+"\\Documents\\";
		System.out.println(DocumentsFolder);
		userfolder = FileSystems.getDefault().getPath(DocumentsFolder, "Chomp's Wacky Worlds");
		if (Files.notExists(userfolder)) {
			new File(userfolder.toString()).mkdir();
			System.out.println(userfolder.toString() + " does not exist.");
		}else{
			System.out.println(userfolder.toString() + " already exists. It does not need to be created at this time.");
		}
		modsfolder = FileSystems.getDefault().getPath(userfolder.toString(), "Mods");
		savesfolder = FileSystems.getDefault().getPath(userfolder.toString(), "Saves");
		if (Files.notExists(modsfolder)) {
			System.out.println(modsfolder.toString() + " does not exist.");
			new File(modsfolder.toString()).mkdir();
		}else{
			System.out.println(modsfolder.toString() + " already exists. It does not need to be created at this time.");
		}
		
		if (Files.notExists(savesfolder)) {
			new File(savesfolder.toString()).mkdir();
			//new File(savesfolder.toString()+"\\Chomp's Wacky Worlds\\").mkdir();
			System.out.println(savesfolder.toString() + " does not exist.");
		}else{
			System.out.println(savesfolder.toString() + " already exists. It does not need to be created at this time.");
		}
		
		//LWJGL OpenGL Stuff Starts here:
		try {
            Display.setDisplayMode(new DisplayMode(screenWidth,screenHeight));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
		
		
        
        //GL11.glMatrixMode(GL11.GL_PROJECTION);
        //GL11.glLoadIdentity();
        //GL11.glFrustum(0, screenWidth*2, screenHeight*2, 0, 1000, -1000);
        //GLU.gluPerspective(90f, 1920f/1080f, 1000f, -1000f);
        //GL11.glOrtho(0, screenWidth*2, screenHeight*2, 0, 1000, -1000);
        //GL11.glViewport(0, 0, screenWidth, screenHeight);
        //GL11.glOrtho(0, 1, 1, 0, 1, -1);
        //GL11.glMatrixMode(GL11.GL_MODELVIEW);
        
        //System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
        
        m = new ColoredMesh();
        m2 = new Mesh(defaultVertices2);
        m3 = new TexturedMesh();
        //m4 = OBJLoader.loadModel("models/Test.obj");
        
        GL11.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
        
     // Setup projection matrix
        projectionMatrix = new Matrix4f();
        float fieldOfView = 60f;
        float aspectRatio = (float)gameWidth / (float)gameHeight;
        float near_plane = 0.1f;
        float far_plane = 100f;
         
        float y_scale = this.coTangent(this.degreesToRadians(fieldOfView / 2f));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = far_plane - near_plane;
         
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((far_plane + near_plane) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * near_plane * far_plane) / frustum_length);
        projectionMatrix.m33 = 0;
        
        // Setup view matrix
        viewMatrix = new Matrix4f();
         
        // Setup model matrix
        modelMatrix = new Matrix4f();
        
     // Create a FloatBuffer with the proper size to store our matrices later
        matrix44Buffer = BufferUtils.createFloatBuffer(16);
        
        Vector3f cameraPos = new Vector3f(0, -1, -8);
        
        Matrix4f.translate(cameraPos, viewMatrix, viewMatrix);
        
        modelPos = new Vector3f(0, 0, 0);
        modelAngle = new Vector3f(0, 0, 0);
        modelScale = new Vector3f(1, 1, 1);
        //cameraPos = new Vector3f(0, 0, -1);
        
        projectionMatrixLocation = GL20.glGetUniformLocation(pId, "projectionMatrix");
        viewMatrixLocation = GL20.glGetUniformLocation(pId, "viewMatrix");
        modelMatrixLocation = GL20.glGetUniformLocation(pId, "modelMatrix");
        
     // Load the vertex shader
        
        float yPos = 0;
        
        int texIDs[] = new int[3];
        
        texIDs[0] = TextureLoader.loadPNG("assets/textures/ash_uvgrid02.png", GL13.GL_TEXTURE0);
        texIDs[1] = TextureLoader.loadPNG("assets/textures/test.png", GL13.GL_TEXTURE0);
        texIDs[2] = TextureLoader.loadOBG("assets/textures/test2.obg", GL13.GL_TEXTURE0);
        
        
        Player p = new Player(0f, 0f, texIDs, matrix44Buffer, modelMatrixLocation);
        
        Player p2 = new Player(1f, 1f, texIDs, matrix44Buffer, modelMatrixLocation);
        
        
        while (!Display.isCloseRequested()) {
        	
        	//yPos -= 0.001f; 
        	
        	modelPos = new Vector3f(0, yPos, 0);
        	
        	
            
            // render OpenGL here
             
            Display.update();
            
            p.input();
            
            p2.input();
            
            p2.update();
            
            p.update();
            
            //input();
            
            //p.update();
            
         // Clear the screen and depth buffer
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            
            GL20.glUseProgram(Main.pId);
            
            projectionMatrix.store(matrix44Buffer); matrix44Buffer.flip();
            GL20.glUniformMatrix4(projectionMatrixLocation, false, matrix44Buffer);
            viewMatrix.store(matrix44Buffer); matrix44Buffer.flip();
            GL20.glUniformMatrix4(viewMatrixLocation, false, matrix44Buffer);
            
            
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            
            //GL20.glUseProgram(pId);
            
            //m.render();
            
            //m2.render();
            
            //p.render(matrix44Buffer, modelMatrixLocation);
            
            //p2.render(matrix44Buffer, modelMatrixLocation);
            
            //m3.render();
            
            //render();
            
            //p.render();
            
            //GL20.glUseProgram(0);
            
            GL20.glUseProgram(0);
            
            GL11.glDisable(GL11.GL_BLEND);
            
            Display.sync(60);
            
            //System.out.println("Test");
        }
        m.destroy(); 
        m2.destroy();
        m3.destroy();
        
        GL20.glUseProgram(0);
        GL20.glDetachShader(pId, vsId);
        GL20.glDetachShader(pId, fsId);
         
        GL20.glDeleteShader(vsId);
        GL20.glDeleteShader(fsId);
        GL20.glDeleteProgram(pId);
        
        Display.destroy();
        
	}
	
	public static void main(String[] argv) {
        Main main = new Main();
        main.start();
    }
	
	public static int loadShader(String filename, int type) {
	    StringBuilder shaderSource = new StringBuilder();
	    int shaderID = 0;
	     
	    try {
	        BufferedReader reader = new BufferedReader(new FileReader(filename));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            shaderSource.append(line).append("\n");
	        }
	        reader.close();
	    } catch (IOException e) {
	        System.err.println("Could not read file.");
	        e.printStackTrace();
	        System.exit(-1);
	    }
	     
	    shaderID = GL20.glCreateShader(type);
	    GL20.glShaderSource(shaderID, shaderSource);
	    GL20.glCompileShader(shaderID);
	     
	    return shaderID;
	}
	
	private float coTangent(float angle) {
        return (float)(1f / Math.tan(angle));
    }
     
    public static float degreesToRadians(float degrees) {
        return degrees * (float)(PI / 180d);
    }
}
