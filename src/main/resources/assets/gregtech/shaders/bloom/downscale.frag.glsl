#version 120

varying vec2 vTexCoord;

varying vec2 vTexelSize;

uniform sampler2D texture;

vec4 sampleTexel(vec2 texCoords, float weight) {
    vec4 tex = texture2D(texture, texCoords + vec2(-1, -1) * vTexelSize);
    tex += texture2D(texture, texCoords + vec2(-1, 1) * vTexelSize);
    tex += texture2D(texture, texCoords + vec2(1, 1) * vTexelSize);
    tex += texture2D(texture, texCoords + vec2(1, -1) * vTexelSize);
    return tex * weight * 0.25f;
}

void main() {
    vec4 tex = sampleTexel(vTexCoord, 0.5f);
    tex += sampleTexel(vTexCoord + vec2(-1, -1) * vTexelSize, 0.125f);
    tex += sampleTexel(vTexCoord + vec2(-1, 1) * vTexelSize, 0.125f);
    tex += sampleTexel(vTexCoord + vec2(1, 1) * vTexelSize, 0.125f);
    tex += sampleTexel(vTexCoord + vec2(1, -1) * vTexelSize, 0.125f);
    gl_FragColor = vec4(tex.rgb, 1.0);
}
