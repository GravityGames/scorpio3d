#version 150 core

uniform sampler2D texture_diffuse;
 
in vec4 pass_Color;
in vec2 pass_TextureCoord;
 
out vec4 out_Color;
 
void main() {
	//gl_FragColor = pass_Color;
	//gl_FragColor = vec4(0.5, 0.8, 1, 1);
	if(pass_Color == vec4(0, 0, 0, 1)){
		out_Color = vec4(1, 1, 1, 1);
	}else{
			//out_Color = vec4(vec3(pass_Color.rgb * texture(texture_diffuse, pass_TextureCoord).rgb), 1.0);
			out_Color = pass_Color *  texture(texture_diffuse, pass_TextureCoord);
    		//out_Color = pass_Color;
    	//if(texture_diffuse != null){
    	//if(pass_TextureCoord != vec2(0, 0)){
    		//out_Color = texture(texture_diffuse, pass_TextureCoord);
    	
    	//}
    	//}
    }
}