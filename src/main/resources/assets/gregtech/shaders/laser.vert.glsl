#version 120

uniform vec3 u_CameraPosition;
uniform mat4 u_ModelMatrix;

varying vec2 v_TexCoord;

const float PI = 3.14159265358979323846;



mat4 rotateMatrix(float angle, vec3 axis){
    float x = axis.x;
    float y = axis.y;
    float z = axis.z;

    float c = cos(angle);
    float s = sin(angle);
    float t = 1.0 - c;

    return mat4(
        c+x*x*t,     t*x*y - s*z, t*x*z + s*y, 0.0,
        t*x*y + s*z,   t*y*y + c,   t*y*z - s*x, 0.0,
        t*x*z - s*y,   t*y*z + s*x, t*z*z + c,   0.0,
        0.0,           0.0,         0.0,         1.0
    );
}


void main() {

    v_TexCoord = gl_MultiTexCoord0.xy;

    float xAngle = atan(u_CameraPosition.y,u_CameraPosition.z) - PI/2;
    float c = cos(xAngle);
    float s = sin(xAngle);
    mat4 xRotate = mat4(
        1.0, 0.0, 0.0, 0.0,
        0.0,    c,   -s, 0.0,
        0.0,    s,    c, 0.0,
        0.0, 0.0, 0.0, 1.0
    );


    gl_Position = gl_ModelViewProjectionMatrix * (u_ModelMatrix*(xRotate*gl_Vertex));
}

