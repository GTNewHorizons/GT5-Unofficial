#version 330 core

uniform sampler2D u_Texture;
uniform vec4 u_Color;
uniform float u_Gamma;

in vec2 v_TexCoord;

layout(location = 0) out vec4 fragColor;

vec3 toYIQ(vec3 rgb){
    return mat3( 0.299, 1.0, 0.40462981, 0.587, -0.46081557, -1.0, 0.114, -0.53918443, 0.59537019 ) * rgb;
}

vec3 toRGB(vec3 yiq){
    return mat3( 1.0, 1.0, 1.0, 0.5696804, -0.1620848, -0.6590654, 0.3235513, -0.3381869, 0.8901581 ) * yiq;
}

void main() {
    vec3 tex = texture(u_Texture, v_TexCoord).rgb;

    vec3 original = toYIQ(tex);

    vec4 color;
    if (length(original.xy) < .01){
        color = vec4(tex, 1.0);
    } else {
        vec3 targetYIQ = toYIQ(u_Color.rgb);
        vec3 yiqColor = vec3(original.x, targetYIQ.yz);
        vec3 finalrgb = toRGB(yiqColor);
        finalrgb = pow(finalrgb, vec3(1.0 / u_Gamma));
        color = vec4(finalrgb, u_Color.a);
    }

    if (color.a < 0.1) discard;
    fragColor = color;
}
