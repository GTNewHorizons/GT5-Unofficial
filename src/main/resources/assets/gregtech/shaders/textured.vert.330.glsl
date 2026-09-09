#version 330 core

in vec3 a_Position;
in vec2 a_UV;

uniform mat4 u_MVP;

out vec2 v_TexCoord;

void main() {
    v_TexCoord = a_UV;
    gl_Position = u_MVP * vec4(a_Position, 1.0);
}
