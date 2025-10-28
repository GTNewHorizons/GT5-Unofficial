#version 120

void main() {
    vec4 translatedPos = gl_Vertex;
    gl_Position = gl_ModelViewProjectionMatrix * translatedPos;
}
