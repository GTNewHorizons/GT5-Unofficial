#version 120

varying vec2 vTexCoord;

void main() {
    vTexCoord = gl_MultiTexCoord0.st;
    gl_Position = gl_Vertex;
}
