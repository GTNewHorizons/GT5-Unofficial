#version 120


uniform float u_Scale;
uniform float u_ScaleSnapshot;
uniform vec3 u_ColorCore;
uniform vec3 u_ColorSpike;
uniform float u_Time;
uniform float u_TimeSnapshot;
varying vec3 v_Color;

/*
float wave ( vec3 input){
    return (cos(input.x + u_Time*1.2)*cos(input.y + u_Time*1.3)*cos(input.z + u_Time*1.4));
}
*/
const float PI = 3.14159265358979323846;


float lazyHash(vec3 toHash){
    vec3 v = fract(toHash*1.23456 + 3.1456);
    v*=7;
    return fract(v.y + v.x*(v.z+1));
}

float triangle(float x){
    return 1.0 - abs(2.0 * (x - 0.5));
}

void main() {
    //grab local position
    vec3 pos = gl_Vertex.xyz;
    //Grabs how far the vertex is for center
    //Antimatter.model has spikes that are 2 unit away, and the 'core' is 1 unit away
    float len = length(pos);

    vec3 currentCoreColor = mix(u_ColorCore,u_ColorSpike,triangle(mod((u_Time/4.0 + lazyHash(pos)/2),1.0)));
    vec3 currentSpikeColor = mix(u_ColorCore,u_ColorSpike,triangle(mod((u_Time/2.0 + lazyHash(pos)),1.0)));
    //v_Color = mix(u_ColorCore,u_ColorSpike,extension*spike);
    //v_Color = mix(currentCoreColor,currentSpikeColor,extension*spike);
    v_Color = currentCoreColor;
    float timelerp = clamp(1,0,(u_Time-u_TimeSnapshot)/2.5);
    float scale = mix(u_ScaleSnapshot,u_Scale,timelerp);
    mat4 mScale = mat4(
        scale,0,0,0,
        0,scale,0,0,
        0,0,scale,0,
        0,0,0,1);


    gl_Position = gl_ModelViewProjectionMatrix * mScale * gl_Vertex;
}
