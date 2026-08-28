#version 330 core

in vec3 a_Position;

uniform mat4 u_MVP;

void main() {
    gl_Position = u_MVP * vec4(a_Position, 1.0);
}