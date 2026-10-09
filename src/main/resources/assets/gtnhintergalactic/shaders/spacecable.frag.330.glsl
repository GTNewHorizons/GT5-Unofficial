#version 330 core

in vec2 v_TexCoord; // The tex coord
flat in float v_yPos; // The y-coord in world space
uniform sampler2D u_BlockTex; // The block texture sampler
uniform float u_Time; // loops from 0 to 1 every second
uniform vec2 u_GlowU; // Bounds of the glowy bit's U, [min, max]
uniform vec2 u_GlowV; // Bounds of the glowy bit's V, [min, max]

layout(location = 0) out vec4 fragColor;

const vec3 MAX = vec3(1.0, 1.0, 1.0);
const vec3 glow_color = vec3(0.0, 0.65, 1.0); // The glow color, RGB
const float cableHeight = 512.0;

vec3 frontWave(in vec3 color, in float t) {
    float front = t;
    float y = v_yPos / cableHeight;
    float dist = abs(front - y);
    //Disable lights if they are too far from the front
    float lightsOn = dist <= 0.03 ? 1.0 : 0.0;
    float sy = sin(1.57 + (dist*33) * 1.57);
    return color * pow(sy, 2.0) * lightsOn;
}

void main() {

    vec4 tex = texture(u_BlockTex, v_TexCoord);
    if (tex.a < 0.5) discard;

    float glowMul = (v_TexCoord.x >= u_GlowU.x && v_TexCoord.x <= u_GlowU.y && v_TexCoord.y >= u_GlowV.x && v_TexCoord.y <= u_GlowV.y) ? 1.0 : 0.0;

    vec3 col = frontWave(glow_color, u_Time);
    fragColor = vec4(min(tex.rgb + col * glowMul, MAX), tex.a);
}
