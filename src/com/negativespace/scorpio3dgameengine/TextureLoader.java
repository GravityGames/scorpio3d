package com.negativespace.scorpio3dgameengine;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;

public class TextureLoader {
	
	public static int loadPNG(String filename, int textureUnit){
		return loadPNG(filename, textureUnit, "GL_NEAREST", "GL_NEAREST");
	}

	public static int loadPNG(String filename, int textureUnit, String nearFilter, String farFilter){
		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;

		try {
			// Open the PNG file as an InputStream
			InputStream in = new FileInputStream(filename);
			// Link the PNG decoder to this stream
			PNGDecoder decoder = new PNGDecoder(in);

			// Get the width and height of the texture
			tWidth = decoder.getWidth();
			tHeight = decoder.getHeight();


			// Decode the PNG file in a ByteBuffer
			buf = ByteBuffer.allocateDirect(
					4 * decoder.getWidth() * decoder.getHeight());
			buf.order(ByteOrder.nativeOrder());
			decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
			//buf.flip();
			
			
			buf.position(0);

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// Create a new texture object in memory and bind it
		int texId = GL11.glGenTextures();
		GL13.glActiveTexture(textureUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

		// All RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tWidth, tHeight, 0,
				GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
		
		if(farFilter == "GL_NEAREST_MIPMAP_NEAREST" || farFilter == "GL_NEAREST_MIPMAP_LINEAR" || farFilter == "GL_LINEAR_MIPMAP_NEAREST" || farFilter == "GL_LINEAR_MIPMAP_LINEAR"){
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		}

		// Setup the ST coordinate system
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		// Setup what to do when the texture has to be scaled
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
		//		GL11.GL_NEAREST);
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
		//		GL11.GL_LINEAR_MIPMAP_LINEAR);
		
		// Setup what to do when the texture has to be scaled
		switch(nearFilter){
		case "GL_NEAREST":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
					GL11.GL_NEAREST);
			break;
		case "GL_LINEAR":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
					GL11.GL_LINEAR);
			break;
		default:
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
					GL11.GL_NEAREST);
			break;
		}
		
		switch(farFilter){
		case "GL_NEAREST":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST);
			break;
		case "GL_LINEAR":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_LINEAR);
			break;
		case "GL_NEAREST_MIPMAP_NEAREST":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST_MIPMAP_NEAREST);
			break;
		case "GL_NEAREST_MIPMAP_LINEAR":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST_MIPMAP_LINEAR);
			break;
		case "GL_LINEAR_MIPMAP_NEAREST":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_LINEAR_MIPMAP_NEAREST);
			break;
		case "GL_LINEAR_MIPMAP_LINEAR":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_LINEAR_MIPMAP_LINEAR);
			break;
		default:
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST);
			break;
		}
				
				

		//this.exitOnGLError("loadPNGTexture");

		return texId;
	}
	
	public static int loadRGBPNG(String filename, int textureUnit, byte r, byte g, byte b){
		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;

		try {
			// Open the PNG file as an InputStream
			InputStream in = new FileInputStream(filename);
			// Link the PNG decoder to this stream
			PNGDecoder decoder = new PNGDecoder(in);

			// Get the width and height of the texture
			tWidth = decoder.getWidth();
			tHeight = decoder.getHeight();


			// Decode the PNG file in a ByteBuffer
			buf = ByteBuffer.allocateDirect(
					4 * decoder.getWidth() * decoder.getHeight());
			buf.order(ByteOrder.nativeOrder());
			decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
			
			for(int i=0; i < buf.capacity(); i+=4){
				if(buf.get(i)==(byte)255 && buf.get(i+1)==(byte)0 && buf.get(i+2)==(byte)255){
					buf.put(i+3, (byte)0);
				}
			}
			
			buf.position(0);

			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// Create a new texture object in memory and bind it
		int texId = GL11.glGenTextures();
		GL13.glActiveTexture(textureUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

		// All RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);

		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tWidth, tHeight, 0,
				GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

		// Setup the ST coordinate system
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		// Setup what to do when the texture has to be scaled
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
		//		GL11.GL_NEAREST);
		//GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
		//		GL11.GL_LINEAR_MIPMAP_LINEAR);
		
		// Setup what to do when the texture has to be scaled
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
						GL11.GL_NEAREST);
				GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
						GL11.GL_NEAREST);

		//this.exitOnGLError("loadPNGTexture");

		return texId;
	}

	public static int loadOBG(String filename, int textureUnit){
		return loadOBG(filename, textureUnit, "GL_NEAREST", "GL_NEAREST", null);
	}
	
	public static int loadOBG(String filename, int textureUnit, String nearFilter, String farFilter){
		return loadOBG(filename, textureUnit, nearFilter, farFilter, null);
	}

	public static int loadOBG(String filename, int textureUnit, String nearFilter, String farFilter, ScorpioPalette sp){
		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;

		try {
			// Open the PNG file as an InputStream
			InputStream in = new FileInputStream(filename);
			// Link the PNG decoder to this stream
			//PNGDecoder decoder = new PNGDecoder(in);

			byte revision = (byte)in.read();

			if(revision == 0){

				byte properties = (byte)in.read();

				boolean inlinePalette = (properties & (1 << 7)) != 0;
				boolean color1transparency = (properties & (1 << 6)) != 0;
				boolean color2transparency = (properties & (1 << 5)) != 0;
				
				//System.out.println(inlinePalette);
				
				ScorpioPalette inlinepalette = new ScorpioPalette();
				
				if(inlinePalette){
					if(color1transparency){
						inlinepalette.addColor((byte)0, (byte)0, (byte)0, (byte)0);
					}else{
						inlinepalette.addColor((byte)in.read(), (byte)in.read(), (byte)in.read(), (byte)in.read());
					}
					if(color2transparency){
						inlinepalette.addColor((byte)0, (byte)0, (byte)0, (byte)0);
					}else{
						inlinepalette.addColor((byte)in.read(), (byte)in.read(), (byte)in.read(), (byte)in.read());
					}
				}

				// Get the width and height of the texture
				tWidth = in.read();
				tHeight = in.read();

				ArrayList<Boolean> imageData = new ArrayList<Boolean>();

				byte currentImageByte = 0;

				for(int i=0; i<tWidth*tHeight; i+=8){
					currentImageByte = (byte)in.read();
					imageData.add((currentImageByte & (1 << 7)) != 0);
					imageData.add((currentImageByte & (1 << 6)) != 0);
					imageData.add((currentImageByte & (1 << 5)) != 0);
					imageData.add((currentImageByte & (1 << 4)) != 0);
					imageData.add((currentImageByte & (1 << 3)) != 0);
					imageData.add((currentImageByte & (1 << 2)) != 0);
					imageData.add((currentImageByte & (1 << 1)) != 0);
					imageData.add((currentImageByte & (1 << 0)) != 0);
				}           

				// Decode the PNG file in a ByteBuffer
				buf = ByteBuffer.allocateDirect((4) * tWidth * tHeight);
				buf.order(ByteOrder.nativeOrder());
				for(int i=0; i<imageData.size(); i++){
					if(imageData.get(i)){
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(1f);
						if(sp == null){
							if(inlinePalette){
								buf.put(inlinepalette.colors.get(1));
							}else{
								buf.put((byte) 255);
								buf.put((byte) 255);
								buf.put((byte) 255);
								buf.put((byte) 255);
							}
						}else{
							buf.put(sp.colors.get(1));
						}
					}else{
						//buf.asFloatBuffer().put(1f);
						//buf.asFloatBuffer().put(1f);
						//buf.asFloatBuffer().put(1f);
						//buf.asFloatBuffer().put(1f);
						if(sp == null){
							if(inlinePalette){
								buf.put(inlinepalette.colors.get(0));
							}else{
								buf.put((byte) 0);
								buf.put((byte) 0);
								buf.put((byte) 0);
								buf.put((byte) 255);
							}
						}else{
							buf.put(sp.colors.get(0));
						}
					}
				}
				//decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
				buf.position(0);
			}else if(revision == 1){

				byte properties = (byte)in.read();

				boolean inlinePalette = (properties & (1 << 7)) != 0;
				boolean color1transparency = (properties & (1 << 6)) != 0;
				boolean color2transparency = (properties & (1 << 5)) != 0;
				
				//System.out.println(inlinePalette);
				
				ScorpioPalette inlinepalette = new ScorpioPalette();
				
				if(inlinePalette){
					if(color1transparency){
						inlinepalette.addColor((byte)0, (byte)0, (byte)0, (byte)0);
					}else{
						inlinepalette.addColor((byte)in.read(), (byte)in.read(), (byte)in.read(), (byte)in.read());
					}
					if(color2transparency){
						inlinepalette.addColor((byte)0, (byte)0, (byte)0, (byte)0);
					}else{
						inlinepalette.addColor((byte)in.read(), (byte)in.read(), (byte)in.read(), (byte)in.read());
					}
				}

				// Get the width and height of the texture
				tWidth = ByteBuffer.wrap(new byte[]{(byte) 0, (byte) 0,(byte) in.read(), (byte) in.read()}).getInt();
				tHeight = ByteBuffer.wrap(new byte[]{(byte) 0, (byte) 0,(byte) in.read(), (byte) in.read()}).getInt();
				//tHeight = in.read();

				ArrayList<Boolean> imageData = new ArrayList<Boolean>();

				byte currentImageByte = 0;

				for(int i=0; i<tWidth*tHeight; i+=8){
					currentImageByte = (byte)in.read();
					imageData.add((currentImageByte & (1 << 7)) != 0);
					imageData.add((currentImageByte & (1 << 6)) != 0);
					imageData.add((currentImageByte & (1 << 5)) != 0);
					imageData.add((currentImageByte & (1 << 4)) != 0);
					imageData.add((currentImageByte & (1 << 3)) != 0);
					imageData.add((currentImageByte & (1 << 2)) != 0);
					imageData.add((currentImageByte & (1 << 1)) != 0);
					imageData.add((currentImageByte & (1 << 0)) != 0);
				}           

				// Decode the PNG file in a ByteBuffer
				buf = ByteBuffer.allocateDirect((4) * tWidth * tHeight);
				buf.order(ByteOrder.nativeOrder());
				for(int i=0; i<imageData.size(); i++){
					if(imageData.get(i)){
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(1f);
						if(sp == null){
							if(inlinePalette){
								buf.put(inlinepalette.colors.get(1));
							}else{
								buf.put((byte) 255);
								buf.put((byte) 255);
								buf.put((byte) 255);
								buf.put((byte) 255);
							}
						}else{
							buf.put(sp.colors.get(1));
						}
					}else{
						//buf.asFloatBuffer().put(1f);
						//buf.asFloatBuffer().put(1f);
						//buf.asFloatBuffer().put(1f);
						//buf.asFloatBuffer().put(1f);
						if(sp == null){
							if(inlinePalette){
								buf.put(inlinepalette.colors.get(0));
							}else{
								buf.put((byte) 0);
								buf.put((byte) 0);
								buf.put((byte) 0);
								buf.put((byte) 255);
							}
						}else{
							buf.put(sp.colors.get(0));
						}
					}
				}
				//decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
				buf.position(0);
			}

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// Create a new texture object in memory and bind it
		int texId = GL11.glGenTextures();
		GL13.glActiveTexture(textureUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

		// All RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		//GL11.glPixelStoref(GL11.GL_UNPACK_ALIGNMENT, 4);

		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tWidth, tHeight, 0,
				GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
		if(farFilter == "GL_NEAREST_MIPMAP_NEAREST" || farFilter == "GL_NEAREST_MIPMAP_LINEAR" || farFilter == "GL_LINEAR_MIPMAP_NEAREST" || farFilter == "GL_LINEAR_MIPMAP_LINEAR"){
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		}

		// Setup the ST coordinate system
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

		switch(nearFilter){
		case "GL_NEAREST":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
					GL11.GL_NEAREST);
			break;
		case "GL_LINEAR":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
					GL11.GL_LINEAR);
			break;
		default:
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
					GL11.GL_NEAREST);
			break;
		}
		
		switch(farFilter){
		case "GL_NEAREST":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST);
			break;
		case "GL_LINEAR":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_LINEAR);
			break;
		case "GL_NEAREST_MIPMAP_NEAREST":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST_MIPMAP_NEAREST);
			break;
		case "GL_NEAREST_MIPMAP_LINEAR":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST_MIPMAP_LINEAR);
			break;
		case "GL_LINEAR_MIPMAP_NEAREST":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_LINEAR_MIPMAP_NEAREST);
			break;
		case "GL_LINEAR_MIPMAP_LINEAR":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_LINEAR_MIPMAP_LINEAR);
			break;
		default:
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST);
			break;
		}

		//this.exitOnGLError("loadPNGTexture");

		return texId;
	}
	
	public static int loadTBG(String filename, int textureUnit){
		return loadTBG(filename, textureUnit, "GL_NEAREST", "GL_NEAREST", null);
	}
	
	public static int loadTBG(String filename, int textureUnit, String nearFilter, String farFilter){
		return loadTBG(filename, textureUnit, nearFilter, farFilter, null);
	}
	
	public static int loadTBG(String filename, int textureUnit, String nearFilter, String farFilter, ScorpioPalette sp){
		ByteBuffer buf = null;
		int tWidth = 0;
		int tHeight = 0;

		try {
			// Open the PNG file as an InputStream
			InputStream in = new FileInputStream(filename);
			// Link the PNG decoder to this stream
			//PNGDecoder decoder = new PNGDecoder(in);

			byte revision = (byte)in.read();

			if(revision == 0){

				byte properties = (byte)in.read();

				boolean inlinePalette = (properties & (1 << 7)) != 0;
				boolean color1transparency = (properties & (1 << 6)) != 0;
				boolean color2transparency = (properties & (1 << 5)) != 0;
				
				//System.out.println(inlinePalette);
				
				ScorpioPalette inlinepalette = new ScorpioPalette();
				
				if(inlinePalette){
					if(color1transparency){
						inlinepalette.addColor((byte)0, (byte)0, (byte)0, (byte)0);
					}else{
						inlinepalette.addColor((byte)in.read(), (byte)in.read(), (byte)in.read(), (byte)in.read());
					}
					if(color2transparency){
						inlinepalette.addColor((byte)0, (byte)0, (byte)0, (byte)0);
					}else{
						inlinepalette.addColor((byte)in.read(), (byte)in.read(), (byte)in.read(), (byte)in.read());
					}
				}

				// Get the width and height of the texture
				tWidth = in.read();
				tHeight = in.read();

				//ArrayList<Boolean> imageData = new ArrayList<Boolean>();
				ArrayList<Byte> imageData = new ArrayList<Byte>();

				byte currentImageByte = 0;

				for(int i=0; i<tWidth*tHeight; i+=4){
					currentImageByte = (byte)in.read();
					imageData.add((byte)((currentImageByte & 128+64) >>> 6));
					imageData.add((byte)((currentImageByte & 32+16) >>> 4));
					imageData.add((byte)((currentImageByte & 8+4) >>> 2));
					imageData.add((byte)(currentImageByte & 2+1));
					//System.out.println(currentImageByte & (1 << 7));
					//imageData.add((byte)(currentImageByte & (1 >>> 6)));
					//imageData.add((byte)(currentImageByte & (1 << 2 >>> 6)));
					//imageData.add((byte)(currentImageByte & (1 << 4 >>> 6)));
					//imageData.add((byte)(currentImageByte & (1 << 6 >>> 6)));
					//imageData.add((byte)(currentImageByte << 2 >>> 6));
					//imageData.add((byte)(currentImageByte << 4 >>> 6));
					//imageData.add((byte)(currentImageByte << 6 >>> 6));
					//imageData.add((currentImageByte & (1 << 7)) != 0);
					//imageData.add((currentImageByte & (1 << 6)) != 0);
					//imageData.add((currentImageByte & (1 << 5)) != 0);
					//imageData.add((currentImageByte & (1 << 4)) != 0);
					//imageData.add((currentImageByte & (1 << 3)) != 0);
					//imageData.add((currentImageByte & (1 << 2)) != 0);
					//imageData.add((currentImageByte & (1 << 1)) != 0);
					//imageData.add((currentImageByte & (1 << 0)) != 0);
				}           

				// Decode the PNG file in a ByteBuffer
				buf = ByteBuffer.allocateDirect((4) * tWidth * tHeight);
				for(int i=0; i<imageData.size(); i++){
					if(imageData.get(i) == 0){
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(1f);
						if(sp == null){
							if(inlinePalette){
								buf.put(inlinepalette.colors.get(0));
							}else{
								buf.put((byte) 0);
								buf.put((byte) 0);
								buf.put((byte) 0);
								buf.put((byte) 255);
							}
						}else{
							buf.put(sp.colors.get(0));
						}
					}else if(imageData.get(i) == 1){
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(1f);
						if(sp == null){
							if(inlinePalette){
								buf.put(inlinepalette.colors.get(1));
							}else{
								buf.put((byte) 255);
								buf.put((byte) 255);
								buf.put((byte) 255);
								buf.put((byte) 255);
							}
						}else{
							buf.put(sp.colors.get(1));
						}
					}else if(imageData.get(i) == 2){
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(1f);
						if(sp == null){
							if(inlinePalette){
								buf.put(inlinepalette.colors.get(2));
							}else{
								buf.put((byte) 0);
								buf.put((byte) 0);
								buf.put((byte) 0);
								buf.put((byte) 255);
							}
						}else{
							buf.put(sp.colors.get(2));
						}
					}else if(imageData.get(i) == 3){
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(0f);
						//buf.asFloatBuffer().put(1f);
						if(sp == null){
							if(inlinePalette){
								buf.put(inlinepalette.colors.get(3));
							}else{
								buf.put((byte) 255);
								buf.put((byte) 255);
								buf.put((byte) 255);
								buf.put((byte) 255);
							}
						}else{
							buf.put(sp.colors.get(3));
						}
					}else{
						//buf.asFloatBuffer().put(1f);
						//buf.asFloatBuffer().put(1f);
						//buf.asFloatBuffer().put(1f);
						//buf.asFloatBuffer().put(1f);
						if(sp == null){
							if(inlinePalette){
								buf.put(inlinepalette.colors.get(0));
							}else{
								buf.put((byte) 0);
								buf.put((byte) 0);
								buf.put((byte) 0);
								buf.put((byte) 255);
							}
						}else{
							buf.put(sp.colors.get(0));
						}
					}
				}
				//decoder.decode(buf, decoder.getWidth() * 4, Format.RGBA);
				buf.flip();
			}

			in.close();

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		// Create a new texture object in memory and bind it
		int texId = GL11.glGenTextures();
		GL13.glActiveTexture(textureUnit);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

		// All RGB bytes are aligned to each other and each component is 1 byte
		GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		//GL11.glPixelStoref(GL11.GL_UNPACK_ALIGNMENT, 4);

		// Upload the texture data and generate mip maps (for scaling)
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, tWidth, tHeight, 0,
				GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
		if(farFilter == "GL_NEAREST_MIPMAP_NEAREST" || farFilter == "GL_NEAREST_MIPMAP_LINEAR" || farFilter == "GL_LINEAR_MIPMAP_NEAREST" || farFilter == "GL_LINEAR_MIPMAP_LINEAR"){
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		}

		// Setup the ST coordinate system
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

//		// Setup what to do when the texture has to be scaled
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
//				GL11.GL_NEAREST);
//		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
//				GL11.GL_LINEAR_MIPMAP_LINEAR);
		
		// Setup what to do when the texture has to be scaled
		switch(nearFilter){
		case "GL_NEAREST":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
					GL11.GL_NEAREST);
			break;
		case "GL_LINEAR":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
					GL11.GL_LINEAR);
			break;
		default:
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
					GL11.GL_NEAREST);
			break;
		}
		
		switch(farFilter){
		case "GL_NEAREST":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST);
			break;
		case "GL_LINEAR":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_LINEAR);
			break;
		case "GL_NEAREST_MIPMAP_NEAREST":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST_MIPMAP_NEAREST);
			break;
		case "GL_NEAREST_MIPMAP_LINEAR":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST_MIPMAP_LINEAR);
			break;
		case "GL_LINEAR_MIPMAP_NEAREST":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_LINEAR_MIPMAP_NEAREST);
			break;
		case "GL_LINEAR_MIPMAP_LINEAR":
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_LINEAR_MIPMAP_LINEAR);
			break;
		default:
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
					GL11.GL_NEAREST);
			break;
		}

		//this.exitOnGLError("loadPNGTexture");

		return texId;
	}

}
