#version 120

uniform mat4 u_ModelProjection;

attribute vec3 pos;
attribute vec2 uvIn;
attribute int tIn;

varying vec2 uv;
varying float y;
flat int type;

void main() {

    uv = uvIn;
    type = tIn;
    y = pos.y;
    gl_Position = gl_ModelViewProjectionMatrix * u_ModelProjection * vec4(pos, 1.0);
}
