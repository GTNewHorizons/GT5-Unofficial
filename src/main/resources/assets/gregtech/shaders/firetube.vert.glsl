#version 120

uniform mat4 u_ModelProjection;

attribute vec3 pos;
attribute vec2 uvIn;

varying vec2 uv;

void main() {

    uv = uvIn;
    gl_Position = gl_ModelViewProjectionMatrix * u_ModelProjection * vec4(pos, 1.0);
}
