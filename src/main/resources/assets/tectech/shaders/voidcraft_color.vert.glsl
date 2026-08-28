#version 120

attribute vec3 a_Position;

uniform mat4 u_ModelMatrix;

void main() {
    gl_Position = gl_ModelViewProjectionMatrix * u_ModelMatrix * vec4(a_Position, 1.0);
}