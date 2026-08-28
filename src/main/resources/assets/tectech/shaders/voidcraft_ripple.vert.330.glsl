#version 330 core

// a_Position holds the billboard triangle in local plane coordinates (x, y, z = 0):
// an equilateral triangle of circumradius 1 (apex up). u_Right / u_Up are the
// camera's right and up axes, so the triangle always faces the camera.
in vec3 a_Position;

uniform vec3 u_Center;
uniform vec3 u_Right;
uniform vec3 u_Up;
uniform float u_Scale;

uniform mat4 u_MVP;

void main() {
    vec3 world = u_Center + (u_Right * a_Position.x + u_Up * a_Position.y) * u_Scale;
    gl_Position = u_MVP * vec4(world, 1.0);
}