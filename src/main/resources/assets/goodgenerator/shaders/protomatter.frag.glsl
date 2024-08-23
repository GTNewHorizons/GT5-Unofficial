#version 120
uniform vec3 u_Color;
uniform float u_Opacity;

void main() {
    // Color ramp from 0 to 1
    gl_FragColor = vec4(u_Color,1);
}
