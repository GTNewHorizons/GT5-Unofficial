#version 120

varying vec2 vTexCoord;
varying vec2 vTexelSize;

uniform vec2 texelSize;

void main() {
    vTexCoord = gl_MultiTexCoord0.st;
    vTexelSize = texelSize;

    gl_Position = gl_Vertex;
}
