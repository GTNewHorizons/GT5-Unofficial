#version 120

varying vec4 v_Colour;

void main() {
    gl_TexCoord[0] = gl_MultiTexCoord0;
    v_Colour = gl_Color;

    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}
