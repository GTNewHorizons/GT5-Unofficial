#version 120

uniform float time;

varying vec4 v_Colour;

void main() {
    float t = time / 2.5 * 2 * 3.14159;

    float theta = ((gl_TexCoord[0].x + gl_TexCoord[0].y) * 5.0);
    float k = (sin(theta + t) + 1) * 0.5 + 0.25;

    gl_FragColor = mix(v_Colour * 0.25, v_Colour, k);
}
