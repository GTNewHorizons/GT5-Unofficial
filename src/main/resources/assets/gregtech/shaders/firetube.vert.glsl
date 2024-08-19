#version 120

uniform mat4 u_ModelProjection;

attribute vec3 position;
attribute vec2 uvIn;

varying vec2 uvOut;

void main() {

    uvOut = uvIn;
    gl_Position = gl_ModelViewProjectionMatrix * u_ModelProjection * vec4(position, 1.0);
}
