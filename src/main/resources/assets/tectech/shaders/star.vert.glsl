#version 120


uniform mat4 u_ModelMatrix;
varying vec2 v_TexCoord;

void main() {
    v_TexCoord = gl_MultiTexCoord0.xy;
    gl_Position = gl_ModelViewProjectionMatrix * u_ModelMatrix * gl_Vertex;
}
