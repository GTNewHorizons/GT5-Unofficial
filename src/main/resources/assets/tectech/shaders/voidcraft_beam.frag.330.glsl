#version 330 core

uniform vec4 u_Color;

layout(location = 0) out vec4 fragColor;

void main() {
    fragColor = u_Color;
}