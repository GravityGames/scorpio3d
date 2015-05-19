package com.gravitygamesinteractive.scorpio3dgameengine;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import com.gravitygamesinteractive.scorpio3dgameengine.FontString.Alignment;
import com.gravitygamesinteractive.scorpio3dgameengine.Mesh;
import com.gravitygamesinteractive.scorpio3dgameengine.TexturedMesh;
import com.gravitygamesinteractive.scorpio3dgameengine.TexturedVertex;

public class HUDFontString extends HUDComponent{

	public static FontSpec fontspec;

	String text;
	TexturedMesh mesh;
	int font;
	int fontSize;
	float transparency = 1.0f;
	Vector3f color = new Vector3f(1.0f, 1.0f, 1.0f);

	public enum Alignment{ TopLeft, TopCenter, TopRight, Left, Center, Right, BottomLeft, BottomCenter, BottomRight};

	Alignment alignment;

	float x, y;

	public HUDFontString(float x, float y, String text, int font, int fontSize){
		this(x, y, text, font, fontSize, Alignment.TopLeft, 0, 0);
	}

	public HUDFontString(float x, float y, String text, int font, int fontSize, Alignment alignment){
		this(x, y, text, font, fontSize, alignment, 0, 0);
	}

	public HUDFontString(float x, float y, String text, int font, int fontSize, Alignment alignment, int textWidth, int textHeight){
		if(fontspec==null){
			fontspec=new FontSpec();
			fontspec.readFontSpec("assets/fonts/font.fnt");
		}
		this.x=x;
		this.y=y;
		this.alignment = alignment;
		setFont(font);
		setFontSize(fontSize);
		setText(text, textWidth, textHeight);
	}

	public void setPosition(float x, float y){
		this.x=x;
		this.y=y;
	}

	public void setTransparency(float transparency){
		this.transparency = transparency;
	}

	public void setColor(float r, float g, float b){
		color = new Vector3f(r, g, b);
	}

	public void setText(String text){
		setText(text, 0, 0);
	}

	public void setText(String text, int textWidth, int textHeight){
		this.text = text;
		char[] characters = text.toCharArray();
		boolean parsingCommand=false;
		String command = "";

		/*float xP=0, yP=0;

		if((alignment == Alignment.TopCenter) || (alignment == Alignment.Center) || (alignment == Alignment.BottomCenter)){
			xP = -(text.length()*fontSize)/2;
		}
		if((alignment == Alignment.TopRight) || (alignment == Alignment.Right) || (alignment == Alignment.BottomRight)){
			xP = -(text.length()*fontSize);
		}

		if((alignment == Alignment.Left) || (alignment == Alignment.Center) || (alignment == Alignment.Right)){
			yP = -fontSize/2;
		}

		if((alignment == Alignment.BottomLeft) || (alignment == Alignment.BottomCenter) || (alignment == Alignment.BottomRight)){
			yP = -fontSize;
		}*/

		float xP=0, yP=0;

		ArrayList<TexturedVertex> vertices = new ArrayList<TexturedVertex>();
		ArrayList<Integer> indices = new ArrayList<Integer>();

		float[] texCoords = {0.0f, 1.0f, 0.0f, 1.0f};
		float[] letterAttribs = {0.0f, 1.0f, 0.0f, 1.0f, 0f, 0f, 0f, 0f, 0f};
		//0 is x1, 1 is x2, 2 is y1, 3 is y2

		for(int i=0; i<characters.length; i++){
			if(parsingCommand){
				if(characters[i] == '*'){
					parsingCommand=false;
					if(command.equalsIgnoreCase("BREAK")){
						xP=0;
						yP-=fontSize;
					}
				}else{
					command = command + characters[i];
				}

			}else{
				if(characters[i] == '*'){
					parsingCommand=true;
				}else if(characters[i] == ' '){
					xP+=16;
				}else{
					letterAttribs = getLetterAttribs(characters[i]);
					texCoords = new float[]{letterAttribs[0], letterAttribs[1], letterAttribs[2], letterAttribs[3]};

					if(xP+letterAttribs[4]>textWidth && textWidth>0){
						xP=0;
						yP-=fontSize;
					}

					indices.add(vertices.size());
					indices.add(vertices.size()+1);
					indices.add(vertices.size()+2);
					indices.add(vertices.size()+2);
					indices.add(vertices.size()+3);
					indices.add(vertices.size());

					vertices.add(new TexturedVertex(x+(xP/16f)*fontSize, y+((yP-letterAttribs[7])/16f)*fontSize, 0.0f, color.x, color.y, color.z, transparency, texCoords[0], texCoords[2]));
					vertices.add(new TexturedVertex(x+(xP/16f)*fontSize, y-((-yP+letterAttribs[5]+letterAttribs[7])/16f)*fontSize, 0.0f, color.x, color.y , color.z, transparency, texCoords[0], texCoords[3]));
					vertices.add(new TexturedVertex(x+((xP+letterAttribs[4])/16f)*fontSize, y-((-yP+letterAttribs[5]+letterAttribs[7])/16f)*fontSize, 0.0f, color.x, color.y, color.z, transparency, texCoords[1], texCoords[3]));
					vertices.add(new TexturedVertex(x+((xP+letterAttribs[4])/16f)*fontSize, y+((yP-letterAttribs[7])/16f)*fontSize, 0.0f, color.x, color.y, color.z, transparency, texCoords[1], texCoords[2]));

					/*vertices.add(new TexturedVertex((xP/16f)*fontSize, 0, 0.0f, color.x, color.y, color.z, transparency, texCoords[0], texCoords[2]));
			vertices.add(new TexturedVertex((xP/16f)*fontSize, -fontSize, 0.0f, color.x, color.y , color.z, transparency, texCoords[0], texCoords[3]));
			vertices.add(new TexturedVertex(((xP+letterAttribs[4])/16f)*fontSize, -fontSize, 0.0f, color.x, color.y, color.z, transparency, texCoords[1], texCoords[3]));
			vertices.add(new TexturedVertex(((xP+letterAttribs[4])/16f)*fontSize, 0, 0.0f, color.x, color.y, color.z, transparency, texCoords[1], texCoords[2]));*/

					//if(i<characters.length-1){
					xP+=letterAttribs[4];
					//}
				}
			}
		}

		if((alignment == Alignment.TopCenter) || (alignment == Alignment.Center) || (alignment == Alignment.BottomCenter)){
			float xOffset=0;

			if(textWidth>0){
				xOffset = (textWidth/16f)*fontSize;
			}else{
				xOffset = (xP/16f)*fontSize;
			}

			for(int i=0; i<vertices.size(); i++){
				vertices.get(i).setX(vertices.get(i).getX()-(xOffset/2));
			}

		}

		TexturedVertex[] v = new TexturedVertex[vertices.size()];
		vertices.toArray(v);
		Integer[] j = new Integer[indices.size()];
		int[] i = new int[indices.size()];
		indices.toArray(j);
		for(int k=0; k<i.length; k++){
			i[k] = j[k].intValue();
		}

		mesh = new TexturedMesh(v, i);
	}

	public void setFont(int font){
		this.font = font;
	}

	public void setFontSize(int size){
		this.fontSize = size;
	}

	@Override
	public void render(){
		mesh.render(font);
	}

	public float[] getLetterAttribs(char character){
		float[] coords = new float[9];

		for(int i=0; i<fontspec.IDs.size(); i++){
			if(fontspec.IDs.get(i) == (int)character){
				//System.out.println("Character:   " + (int)character);
				//coords[0] = (float)(fontspec.xValues.get(i))/(float)(fontspec.imageWidth);
				//coords[1] = (float)(fontspec.yValues.get(i))/(float)(fontspec.imageHeight);
				//coords[2] = (float)(fontspec.xValues.get(i)+fontspec.widthValues.get(i))/(float)(fontspec.imageWidth);
				//coords[3] = (float)(fontspec.yValues.get(i)+fontspec.heightValues.get(i))/(float)(fontspec.imageHeight);
				coords[0] = fontspec.xValues.get(i)/256f;
				coords[2] = fontspec.yValues.get(i)/256f;
				coords[1] = (fontspec.xValues.get(i)+fontspec.widthValues.get(i))/256f;
				coords[3] = (fontspec.yValues.get(i)+fontspec.heightValues.get(i))/256f;
				coords[4] = fontspec.widthValues.get(i);
				coords[5] = fontspec.heightValues.get(i);
				coords[6] = fontspec.xOffsets.get(i);
				coords[7] = fontspec.yOffsets.get(i);
				coords[8] = fontspec.xAdvances.get(i);
				//System.out.println("xPos:   " + (float)(fontspec.xValues.get(i))/(float)(fontspec.imageWidth));
				//System.out.println("yPos:   " + (float)(fontspec.yValues.get(i))/(float)(fontspec.imageHeight));
				//System.out.println("xPos2:   " + (float)(fontspec.xValues.get(i)+fontspec.widthValues.get(i))/(float)(fontspec.imageWidth));
				//System.out.println("yPos2:   " + (float)(fontspec.yValues.get(i)+fontspec.heightValues.get(i))/(float)(fontspec.imageHeight));
				break;
			}
		}

		return coords;
	}

}
