#version 120

attribute vec3 position;
attribute vec2 uvIn;

varying vec2 uvOut;

void main() {

    uvOut = uvIn;
    gl_Position = vec4(position, 1.0);
}
