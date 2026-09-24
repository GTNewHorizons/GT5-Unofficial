#version 120

attribute vec3 a_Position;
attribute vec2 a_UV;

uniform mat4 u_ModelMatrix;

varying vec2 v_TexCoord;

uniform vec3 u_CameraPosition;

const float PI = 3.14159265358979323846;

void main() {
    v_TexCoord = a_UV;

    float xAngle = atan(u_CameraPosition.y, u_CameraPosition.z) - PI / 2.0;
    float c = cos(xAngle);
    float s = sin(xAngle);
    mat4 xRotate = mat4(
        1.0, 0.0, 0.0, 0.0,
        0.0,   c,  -s, 0.0,
        0.0,   s,   c, 0.0,
        0.0, 0.0, 0.0, 1.0
    );

    gl_Position = gl_ModelViewProjectionMatrix * (u_ModelMatrix * (xRotate * vec4(a_Position, 1.0)));
}
