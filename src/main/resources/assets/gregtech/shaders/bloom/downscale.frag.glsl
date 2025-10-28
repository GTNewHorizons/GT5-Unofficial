#version 120

varying vec2 vTexCoord;

uniform vec2 texelSize;

uniform sampler2D texture;

vec4 sampleTexel(vec2 texCoords, float weight) {
    vec4 tex = texture2D(texture, texCoords + vec2(-1, -1) * texelSize);
    tex += texture2D(texture, texCoords + vec2(-1, 1) * texelSize);
    tex += texture2D(texture, texCoords + vec2(1, 1) * texelSize);
    tex += texture2D(texture, texCoords + vec2(1, -1) * texelSize);
    return tex * weight * 0.25f;
}

void main() {
    vec4 tex = sampleTexel(vTexCoord, 0.5f);
    tex += sampleTexel(vTexCoord + vec2(-1, -1) * texelSize, 0.125f);
    tex += sampleTexel(vTexCoord + vec2(-1, 1) * texelSize, 0.125f);
    tex += sampleTexel(vTexCoord + vec2(1, 1) * texelSize, 0.125f);
    tex += sampleTexel(vTexCoord + vec2(1, -1) * texelSize, 0.125f);
    gl_FragColor.rgb = tex.rgb;
}
