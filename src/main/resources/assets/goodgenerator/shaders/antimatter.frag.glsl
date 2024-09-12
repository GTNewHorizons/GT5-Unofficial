#version 120
uniform float u_Opacity;
varying vec3 v_Color;


void main() {
    // Color ramp from 0 to 1
    gl_FragColor = vec4(v_Color.rgb,u_Opacity);
}
