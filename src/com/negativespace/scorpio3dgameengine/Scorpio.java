package com.negativespace.scorpio3dgameengine;

import java.io.File;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.negativespace.scorpio3dgameengine.GameObject;
import com.negativespace.scorpio3dgameengine.rendering.DirectionalLight;
import com.negativespace.scorpio3dgameengine.rendering.RenderingEngine;

public class Scorpio {
	
	private static String OS = "Windows";
	
	static boolean isWindows, isMac;
	
	public static GameObject rootObject;
	
	public static RenderingEngine renderingEngine;
	
	public static void init(int screenWidth, int screenHeight, String gameName, GameObject root){
		getOS();
		createWindow(screenWidth, screenHeight, gameName);
        rootObject = root;
        createRenderingEngine();
	}
	
	public static void getOS(){
		OS = System.getProperty("os.name");
		isWindows = OS.startsWith("Windows");
		isMac = OS.startsWith("Mac");
		
		if(isWindows){
			System.setProperty("org.lwjgl.librarypath", new File("native/windows").getAbsolutePath());
		}
	}
	
	public static void createWindow(int screenWidth, int screenHeight, String gameName){
		Display.destroy();
		try {
            Display.setDisplayMode(new DisplayMode(screenWidth,screenHeight));
            Display.setTitle(gameName);
            Display.setFullscreen(true);
            Display.create();
			
			//Display.setDisplayMode(displayMode);
			//Display.setDisplayMode(new DisplayMode(800,600));
			//Display.setFullscreen(true);
			//setDisplayMode(1280, 720, true);
			//Display.create();
            
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }
	}
	public static void createRenderingEngine(){
		createRenderingEngine(0, 0, 0, 0.5f, 0.5f, 0.5f);
	}
	
	public static void createRenderingEngine(float camX, float camY, float camZ, 
			float clearR, float clearG, float clearB){
		renderingEngine = new RenderingEngine();
		
		//renderingEngine.setCameraPosition(0, 7, -9f);
		renderingEngine.setCameraPosition(camX, camY, camZ);
		
		renderingEngine.setClearColor(clearR, clearG, clearB);
		
		//renderingEngine.setAmbientLight(0f, 0f, 0f);
		
		//renderingEngine.addLight(new DirectionalLight(1, 1, 1, 0.8f, 1, 1, 1));
	}
	
	public static void start(){
		while (!Display.isCloseRequested()) {
             
            Display.update();
            
            input();
            
            update();
            
            render();
            
        }  
	}
	
	public static void input(){
		rootObject.input();
	}
	
	public static void update(){
		rootObject.update();
	}
	
	public static void render(){
		renderingEngine.render(rootObject);
		//rootObject.render();
	}

}
