#version 120

uniform float texOffset;

varying vec2 vTexCoord;

void main() {
    vTexCoord = (gl_MultiTexCoord0.st + vec2(texOffset, 0)) * vec2(3, 3);

    gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}
