#version 120

uniform sampler2D u_Texture;
uniform vec4 u_Tint;

varying vec2 v_TexCoord;

void main() {
    gl_FragColor = texture2D(u_Texture, v_TexCoord) * u_Tint;
}
