#version 120

attribute vec3 a_Position;
attribute vec2 a_UV;

uniform mat4 u_ModelMatrix;

varying vec2 v_TexCoord;

void main() {
    v_TexCoord = a_UV;
    gl_Position = gl_ModelViewProjectionMatrix * u_ModelMatrix * vec4(a_Position, 1.0);
}
