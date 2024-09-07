#version 120

uniform sampler2D u_Texture;
uniform vec3 u_Color;

varying vec2 v_TexCoord;

vec3 toYIQ(vec3 rgb){
    return mat3( 0.299, 1.0, 0.40462981, 0.587, -0.46081557, -1.0, 0.114, -0.53918443, 0.59537019 ) * rgb;
}

vec3 toRGB(vec3 yiq){
    return mat3( 1.0, 1.0, 1.0, 0.5696804, -0.1620848, -0.6590654, 0.3235513, -0.3381869, 0.8901581 ) * yiq;
}

void main() {
    vec4 texture = texture2D(u_Texture, v_TexCoord);

    vec3 targetYIQ = toYIQ(u_Color);
    vec3 originalYIQ = toYIQ(texture.xyz);
    vec3 yiqColor = vec3(originalYIQ.x,normalize(targetYIQ.yz)*length(originalYIQ.yz));
    vec3 finalrgb = toRGB(yiqColor);

    gl_FragColor = vec4(finalrgb,1);
}
