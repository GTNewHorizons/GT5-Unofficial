#version 120

uniform float u_Time;
uniform float u_Height;
uniform vec2 u_minUV;
uniform vec2 u_dUV;
uniform sampler2D u_BlockAtlas;

varying vec2 uv;
varying float y;

void main() {

    // Lerp between u-U based on time
    float u = u_minUV.x;
    float v = u_minUV.y;
    float dU = u_dUV.x;
    float dV = u_dUV.y;

    // Shift by time, then shift by position, then mod so we're still accessing the same texture
    u += uv.x * dU;
    v += fract(u_Time + uv.y) * dV;

    gl_FragColor = y <= u_Height ? texture2D(u_BlockAtlas, vec2(u, v)) : vec4(0.0, 0.0, 0.0, 0.0);
}
