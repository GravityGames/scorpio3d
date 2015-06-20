#version 150 core
 
in vec3 in_Position;
in vec4 in_Color;
in vec2 in_TextureCoord;
in vec3 in_Normal;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

out vec4 pass_Color;
out vec2 pass_TextureCoord;
out vec3 pass_Normal;
out vec3 worldPos0;
 
void main() {
	//gl_Position = in_Position;
    //gl_Position = vec4(in_Position[0]/1920.0,in_Position[1]/1080.0,in_Position[2], 1.0);
    gl_Position = projectionMatrix * viewMatrix * modelMatrix * vec4(in_Position, 1.0);
    pass_Color = in_Color;
    pass_TextureCoord = in_TextureCoord;
    pass_Normal = (modelMatrix * vec4(in_Normal, 0.0)).xyz;
    worldPos0 = (modelMatrix * vec4(in_Position, 1.0)).xyz;
}