#version 120

uniform float u_Time;
uniform float u_Stability;
uniform sampler2D u_Texture;
varying vec2 v_TexCoord;

const vec3 STABLE_COLOR = vec3(1,0.85,0.043);
const vec3 UNSTABLE_COLOR = vec3(.4,0,0);

vec3 toYIQ(vec3 rgb){
    return mat3( 0.299, 1.0, 0.40462981, 0.587, -0.46081557, -1.0, 0.114, -0.53918443, 0.59537019 ) * rgb;
}

vec3 toRGB(vec3 yiq){
    return mat3( 1.0, 1.0, 1.0, 0.5696804, -0.1620848, -0.6590654, 0.3235513, -0.3381869, 0.8901581 ) * yiq;
}


void main() {
    //Extremely fragile system to deteriminte if isolate parts of the blackhole
    //Would break with any uv remaping or atlas stiching
    bool isDisk = (abs(v_TexCoord.y-.5f)>.245);
    bool isBack = (abs(v_TexCoord.x-.5f)>.245) && isDisk;
    bool isTinyRing = (v_TexCoord.y > .66) && !isDisk;

    vec2 texCoord = v_TexCoord;
    if (isDisk || isTinyRing){
        texCoord.x = mod(texCoord.x+u_Time/30,1);
    }

    vec4 texture = texture2D(u_Texture, texCoord);
    if (isDisk || isTinyRing){
        vec3 targetYIQ = toYIQ(STABLE_COLOR);
        vec3 originalYIQ = toYIQ(texture.xyz);
        vec3 yiqColor = vec3(originalYIQ.x,normalize(targetYIQ.yz)*length(originalYIQ.yz));
        vec3 finalrgb = toRGB(yiqColor);
        texture = vec4(mix(UNSTABLE_COLOR,finalrgb,pow(u_Stability,.5)),1);
    }

    gl_FragColor = texture;
}
