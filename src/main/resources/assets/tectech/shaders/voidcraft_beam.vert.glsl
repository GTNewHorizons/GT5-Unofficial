#version 120

// a_Position encodes a point of the laser rod: x is the 0..1 parameter along the
// start -> end axis, (y, z) is the corner in {-1, 1} x {-1, 1} spanning the rod's
// cross-section (unit axes u_P1 / u_P2, scaled by u_HalfWidth).
attribute vec3 a_Position;

uniform vec3 u_Start;
uniform vec3 u_End;
uniform vec3 u_P1;
uniform vec3 u_P2;
uniform float u_HalfWidth;

uniform mat4 u_ModelMatrix;

void main() {
    vec3 world = u_Start + (u_End - u_Start) * a_Position.x
        + u_HalfWidth * (u_P1 * a_Position.y + u_P2 * a_Position.z);
    gl_Position = gl_ModelViewProjectionMatrix * u_ModelMatrix * vec4(world, 1.0);
}