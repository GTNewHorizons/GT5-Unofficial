#version 330 core

uniform float u_Opacity;

in vec3 v_Color;

layout(location = 0) out vec4 fragColor;

void main() {
    fragColor = vec4(v_Color.rgb, u_Opacity);
}
