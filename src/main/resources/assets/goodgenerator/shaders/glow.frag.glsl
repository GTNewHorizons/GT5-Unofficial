#version 120
uniform vec3 u_Color;

void main() {
    gl_FragColor = vec4(u_Color,1);
}
