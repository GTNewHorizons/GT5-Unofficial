uniform sampler2D u_Texture;
uniform vec4 u_Color;
uniform float u_Gamma;

varying vec2 v_TexCoord;

const float PI = 3.1415926535897;

//const vec3 baseAngle = PI/4;

vec3 toYIQ(vec3 rgb){
    return mat3( 0.299, 1.0, 0.40462981, 0.587, -0.46081557, -1.0, 0.114, -0.53918443, 0.59537019 ) * rgb;
}

vec3 toRGB(vec3 yiq){
    return mat3( 1.0, 1.0, 1.0, 0.5696804, -0.1620848, -0.6590654, 0.3235513, -0.3381869, 0.8901581 ) * yiq;
}

void main() {
    vec3 texture = texture2D(u_Texture, v_TexCoord).rgb;

    vec3 original = toYIQ(texture);

    if (length(original.xy) < .01){
        gl_FragColor = vec4(texture,1);
    } else {
        vec3 targetYIQ = toYIQ(u_Color);
        vec3 originalYIQ = toYIQ(texture);
        vec3 yiqColor = vec3(original.x,targetYIQ.yz);
        vec3 finalrgb = toRGB(yiqColor);
        finalrgb = pow(finalrgb,vec3(1/u_Gamma));
        gl_FragColor = vec4(finalrgb,u_Color.a);
    }
}
