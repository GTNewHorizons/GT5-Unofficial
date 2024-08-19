#version 120

uniform float u_Time;
uniform float u_Height;
uniform vec4 u_UV;

varying vec2 uv;

void main() {
    gl_FragColor = vec4(u_Height, u_UV.a, u_Time, 1.0);
}
