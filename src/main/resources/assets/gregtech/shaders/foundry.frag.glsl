#version 120

varying vec2 vTexCoord;
varying vec3 vertexColor;

uniform vec3 u_Color;
uniform sampler2D texture;

void main() {
    //float noise = texture2D(texture, vTexCoord).r;
    //float brightness = pow(1.0 + 0.3 * noise, 1.0);  // slightly increases intensity


    gl_FragColor = vec4(u_Color, 1.0f);
    //gl_FragColor = vec4(vec3(noise), 1.0);
    //gl_FragColor = vec4(mix(color, vec3(1, 1, 1), smoothstep(0, 1, scaledNoise)), 1.0);
    //gl_FragColor = vec4(color + (scaledNoise), 1);
}
