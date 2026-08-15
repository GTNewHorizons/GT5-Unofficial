#version 330 core

uniform sampler2D u_Texture;

in vec2 v_TexCoord;

layout(location = 0) out vec4 fragColor;

void main() {
    vec4 color = texture(u_Texture, v_TexCoord);
    if (color.a < 0.1) discard;
    fragColor = color;
}
