#version 120

varying vec2 vTexCoord;

uniform sampler2D texture;

void main() {
    gl_FragColor = texture2D(texture, vTexCoord);
}
