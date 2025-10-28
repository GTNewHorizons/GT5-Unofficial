#version 120

varying vec2 vTexCoord;

uniform vec2 texelSize;
uniform float multiplier;

uniform sampler2D texture;

vec3 tentFilter3x3(vec2 uv, vec2 texelSize) {
    const float kernel[9] = float[9](
    1.0, 2.0, 1.0,
    2.0, 4.0, 2.0,
    1.0, 2.0, 1.0
    );

    vec3 result = vec3(0.0);

    for (int y = -1; y <= 1; y++) {
        for (int x = -1; x <= 1; x++) {
            vec2 offset = vec2(float(x), float(y)) * texelSize;
            int index = (y+1) * 3 + (x+1);
            float weight = kernel[index];
            result += texture2D(texture, uv + offset).rgb * weight;
        }
    }

    return result / 16;
}

void main() {
    gl_FragColor.rgb = tentFilter3x3(vTexCoord, texelSize) * multiplier;
}
