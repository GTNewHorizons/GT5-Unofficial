#version 120

uniform float u_Intensity;
uniform vec3 u_CameraPosition;
uniform sampler2D u_Texture;
uniform vec3 u_Color;


varying vec2 v_TexCoord;
varying vec2 v_localPosition;
varying float v_Transparency;

float luminanceTransform(vec3 color){
    return 0.2126*color.r+0.7152*color.g+0.0722*color.b;
    //return (color.r+color.g+color.b)/3.0;
}


void main() {
    float v_effect = dot(normalize(u_CameraPosition.xy),normalize(v_localPosition));
    float transparency = (v_effect-.5)*2;
    transparency = pow(transparency,u_Intensity);

    vec4 texColor = texture2D(u_Texture,v_TexCoord);
    float luminance = 1-luminanceTransform(texColor.xyz);
    luminance = mix(luminance,1,1-pow(transparency,6));
    gl_FragColor = vec4(u_Color,transparency*v_Transparency*luminance);
}
