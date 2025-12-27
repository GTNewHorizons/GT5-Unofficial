#version 120

uniform vec3 u_Color;
uniform sampler2D texture;

void main() {
    gl_FragColor = vec4(u_Color, 1.0f);
}
