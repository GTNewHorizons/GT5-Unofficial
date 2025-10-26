#version 120

void main() {
    vec4 translatedPos = gl_Vertex + vec3(0, 8 * gl_InstanceID, 0);
    gl_Position = gl_ModelViewProjectionMatrix * translatedPos;
}
