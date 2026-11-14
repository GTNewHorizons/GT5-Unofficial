#version 330 core

in vec3 a_Position;
in vec2 a_UV;

uniform mat4 u_MVP;

out vec2 v_TexCoord;

uniform float u_Time;
uniform float u_Stability;
uniform float u_Scale;
uniform vec3 u_CameraPosition;

const float PI = 3.14159265358979323846;
const float HORIZON_EDGE = 2.6;

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

    v_TexCoord = a_UV;

    bool isDisk = (abs(v_TexCoord.y-.5)>.245);
    bool isBack = (abs(v_TexCoord.x-.5)>.245) && isDisk;
    bool isFront = (abs(v_TexCoord.x-.5)<.255) && isDisk;
    bool isBot = (v_TexCoord.y < .5) && isBack;

    float yAngle = atan(u_CameraPosition.z,u_CameraPosition.x) - PI/2.0;
    float c = cos(yAngle);
    float s = sin(yAngle);
    mat4 yRotate = mat4(
        vec4(c, 0.0, s, 0.0),
        vec4(0.0, 1.0, 0.0, 0.0),
        vec4(-s, 0.0, c, 0.0),
        vec4(0.0, 0.0, 0.0, 1.0)
    );

    float base = length(a_Position);
    float stab = (base>HORIZON_EDGE)?u_Stability:1.0;

    float scale = ((base-HORIZON_EDGE)*stab+HORIZON_EDGE)/base;
    scale = max(scale, .1);
    scale *= u_Scale;

    vec4 rotated = yRotate * vec4(a_Position*scale, 1.0);

    vec3 cameraDirection = normalize(u_CameraPosition);
    cameraDirection = !isBot?cameraDirection:-cameraDirection;
    vec3 rotateAxis = cross(cameraDirection,vec3(0,1,0));
    float angle = acos(dot(cameraDirection,vec3(0,1,0)));
    if (isFront) angle = 0.0;

    float instabilityRotation = (u_Stability<=0.0)?u_Time/10.0:0.0;

    mat4 rotate = rotateMatrix(angle,normalize(rotateAxis));
    mat4 rotateB = rotateMatrix(instabilityRotation,normalize(u_CameraPosition));

    gl_Position = u_MVP * (rotateB * (rotate * rotated));
}
