#version 120

varying vec2 vTexCoord;

uniform vec2 texelSize;

void main() {
    vTexCoord = gl_MultiTexCoord0.st;

    gl_Position = gl_Vertex;
}
