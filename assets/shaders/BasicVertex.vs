#version 150 core
 
in vec3 in_Position;
in vec4 in_Color;
in vec2 in_TextureCoord;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

 
out vec4 pass_Color;
out vec2 pass_TextureCoord;
 
void main() {
	//gl_Position = in_Position;
    //gl_Position = vec4(in_Position[0]/1920.0,in_Position[1]/1080.0,in_Position[2], 1.0);
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(in_Position, 1.0);
    pass_Color = in_Color;
    pass_TextureCoord = in_TextureCoord;
}