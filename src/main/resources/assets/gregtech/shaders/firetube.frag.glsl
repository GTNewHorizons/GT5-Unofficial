#version 120

uniform float u_Time;
uniform float u_Height;
uniform vec2 u_minUV;
uniform vec2 u_dUV;
uniform sampler2D u_BlockAtlas;

varying vec2 uv;
varying float y;

// The color multiplier applied to water in plains, #F3F3F3
//const vec4 colorMult = vec4(63 / 255.0, 118 / 255.0, 228.0 / 255.0, 1.0);
const vec4 colorMult = vec4(243.0 / 255.0, 243.0 / 255.0, 243.0 / 255.0, 1.0);

void main() {

    // Lerp between u-U based on time
    float u = u_minUV.x;
    float v = u_minUV.y;
    float dU = u_dUV.x;
    float dV = u_dUV.y;

    // Shift by time, then shift by position, then mod so we're still accessing the same texture
    u += uv.x * dU;
    v += fract(uv.y) * dV;

    gl_FragColor = texture2D(u_BlockAtlas, vec2(u, v)).rgba * colorMult;//y <= u_Height ? texture2D(u_BlockAtlas, vec2(u, v)).rgba * colorMult : vec4(0.0, 0.0, 0.0, 0.0);
}
