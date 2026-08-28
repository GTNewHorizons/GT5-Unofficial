#version 120

// a_Position holds the billboard triangle in local plane coordinates (x, y, z = 0):
// an equilateral triangle of circumradius 1 (apex up). u_Right / u_Up are the
// camera's right and up axes, so the triangle always faces the camera.
attribute vec3 a_Position;

uniform vec3 u_Center;
uniform vec3 u_Right;
uniform vec3 u_Up;
uniform float u_Scale;

uniform mat4 u_ModelMatrix;

void main() {
    vec3 world = u_Center + (u_Right * a_Position.x + u_Up * a_Position.y) * u_Scale;
    gl_Position = gl_ModelViewProjectionMatrix * u_ModelMatrix * vec4(world, 1.0);
}