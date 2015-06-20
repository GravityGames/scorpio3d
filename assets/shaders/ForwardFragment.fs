#version 150 core

uniform sampler2D texture_diffuse;

uniform vec3 ambient_Light;
 
in vec4 pass_Color;
in vec2 pass_TextureCoord;
 
out vec4 out_Color;
 
void main() {
	out_Color = pass_Color *  texture(texture_diffuse, pass_TextureCoord) * vec4(ambient_Light, 1.0);
}