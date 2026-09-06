#version 120

attribute vec3 a_Position;

varying vec3 v_Color;

uniform float u_Scale;
uniform float u_ScaleSnapshot;
uniform vec3 u_ColorCore;
uniform vec3 u_ColorSpike;
uniform float u_Time;
uniform float u_TransitionProgress;
uniform mat4 u_ModelMatrix;

float lazyHash(vec3 toHash){
    vec3 v = fract(toHash*1.23456 + 3.1456);
    v*=7.0;
    return fract(v.y + v.x*(v.z+1.0));
}

float triangle(float x){
    return 1.0 - abs(2.0 * (x - 0.5));
}

void main() {
    vec3 pos = a_Position;

    vec3 currentCoreColor = mix(u_ColorCore,u_ColorSpike,triangle(mod((u_Time/4.0 + lazyHash(pos)/2.0),1.0)));
    v_Color = currentCoreColor;

    float scale = mix(u_ScaleSnapshot,u_Scale,u_TransitionProgress);
    mat4 mScale = mat4(
        scale,0,0,0,
        0,scale,0,0,
        0,0,scale,0,
        0,0,0,1);

    gl_Position = gl_ModelViewProjectionMatrix * u_ModelMatrix * mScale * vec4(a_Position, 1.0);
}
