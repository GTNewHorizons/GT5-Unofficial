#version 120

varying vec2 vTexCoord;

uniform float multiplier;

uniform sampler2D uScene;
uniform sampler2D uOverlay;

vec3 acesFilter(vec3 x) {
    const float a = 2.51;
    const float b = 0.03;
    const float c = 2.43;
    const float d = 0.59;
    const float e = 0.14;
    return clamp((x*(a*x + b)) / (x*(c*x + d) + e), 0.0, 1.0);
}

vec3 InverseACESFilm(vec3 y) {
    const float a = 2.51;
    const float b = 0.03;
    const float c = 2.43;
    const float d = 0.59;
    const float e = 0.14;

    vec3 numerator = -(b - y * d);
    vec3 discriminant = (b - y * d) * (b - y * d) - 4.0 * (a - y * c) * (-y * e);
    vec3 sqrtDisc = sqrt(max(discriminant, vec3(0.0)));
    vec3 denom = 2.0 * (a - y * c);

    return (numerator + sqrtDisc) / denom;
}

void main() {
    vec4 scene = texture2D(uScene, vTexCoord);

    vec3 sceneColor = scene.rgb;
    sceneColor = InverseACESFilm(sceneColor);

    sceneColor += pow(texture2D(uOverlay, vTexCoord).rgb * multiplier, vec3(1 / 1.9));

    sceneColor = acesFilter(sceneColor);
    gl_FragColor = vec4(sceneColor, scene.a);
}
