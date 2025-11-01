#version 120

varying vec2 vTexCoord;

uniform vec2 texelSize;

uniform sampler2D texture;

vec3 tentFilter3x3(vec2 uv, vec2 texelSize) {
    vec2 dx = vec2(texelSize.x, 0.0);
    vec2 dy = vec2(0.0, texelSize.y);

    vec3 result = vec3(0.0);

    // Top row
    result += texture2D(texture, uv - dx - dy).rgb * 1.0;
    result += texture2D(texture, uv       - dy).rgb * 2.0;
    result += texture2D(texture, uv + dx - dy).rgb * 1.0;

    // Middle row
    result += texture2D(texture, uv - dx).rgb * 2.0;
    result += texture2D(texture, uv).rgb * 4.0;
    result += texture2D(texture, uv + dx).rgb * 2.0;

    // Bottom row
    result += texture2D(texture, uv - dx + dy).rgb * 1.0;
    result += texture2D(texture, uv       + dy).rgb * 2.0;
    result += texture2D(texture, uv + dx + dy).rgb * 1.0;

    return result / 32.0;
}

void main() { // TODO
    gl_FragColor.rgb = tentFilter3x3(vTexCoord, texelSize);
}
