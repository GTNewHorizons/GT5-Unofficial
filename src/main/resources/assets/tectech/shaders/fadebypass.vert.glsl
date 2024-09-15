#version 120
varying vec2 v_TexCoord;

void main() {
    v_TexCoord = gl_MultiTexCoord0.xy;
    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}
