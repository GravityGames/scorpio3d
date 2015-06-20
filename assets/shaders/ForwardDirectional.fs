#version 150 core

uniform sampler2D texture_diffuse;
uniform sampler2D texture_normal;

in vec4 pass_Color;
in vec2 pass_TextureCoord;
in vec3 pass_Normal;
in vec3 worldPos0;

struct Light{
    vec3 color;
    float intensity;
};

struct DirectionalLight{
    Light base;
    vec3 direction;
};

uniform float specularIntensity;
uniform float specularPower;
uniform vec3 eyePos;

uniform DirectionalLight directionalLight;

out vec4 out_Color;

vec4 calcLight(Light base, vec3 direction, vec3 normal){
    //float diffuseFactor = dot(normal, -direction);
    vec3 halfDir = normalize(direction - eyePos);
    float diffuseFactor = max(dot(normal, halfDir), 0.0);
    
    vec4 diffuseColor = vec4(0.0, 0.0, 0.0, 0.0);
    vec4 specularColor = vec4(0,0,0,0);
    
    if(diffuseFactor > 0){
        diffuseColor = vec4(base.color, 1.0) * base.intensity * diffuseFactor;
        
        vec3 directionToEye = normalize(eyePos - worldPos0);
        vec3 reflectDirection = normalize(reflect(direction, normal));
        
        float specularFactor = dot(directionToEye, reflectDirection);
        specularFactor = pow(specularFactor, specularPower);
        
        if(specularFactor > 0){
            specularColor = vec4(base.color, 1.0) * specularIntensity * specularFactor;
        }
    }
    
    return diffuseColor;
}

vec4 calcDirectionalLight(DirectionalLight directionalLight, vec3 normal){
    return calcLight(directionalLight.base, -directionalLight.direction, normal);
}

void main() {
	//out_Color = pass_Color *  texture(texture_diffuse, pass_TextureCoord) * calcDirectionalLight(directionalLight, normalize(pass_Normal));
	out_Color = pass_Color *  texture(texture_diffuse, pass_TextureCoord) * calcDirectionalLight(directionalLight, normalize(texture(texture_normal, pass_TextureCoord).rgb * 2.0 - 1.0));
}