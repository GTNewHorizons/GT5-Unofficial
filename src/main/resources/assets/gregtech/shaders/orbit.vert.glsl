#version 120

attribute vec3 a_Position;
attribute vec2 a_UV;
attribute float a_Instance;

uniform vec3 u_Objects[32];
uniform mat4 u_ModelMatrix;

varying vec2 v_TexCoord;

vec3 rotateX(vec3 v, float a) {
    float s = sin(a);
    float c = cos(a);
    return vec3(v.x, c * v.y - s * v.z, s * v.y + c * v.z);
}

vec3 rotateY(vec3 v, float a) {
    float s = sin(a);
    float c = cos(a);
    return vec3(c * v.x + s * v.z, v.y, c * v.z - s * v.x);
}

vec3 rotateZ(vec3 v, float a) {
    float s = sin(a);
    float c = cos(a);
    return vec3(c * v.x - s * v.y, s * v.x + c * v.y, v.z);
}

void main() {
    int i = min(2 * int(a_Instance + 0.5), 30);
    vec3 angles = u_Objects[i];
    vec3 orbit = u_Objects[i + 1];

    vec3 pos = a_Position * orbit.z;
    pos = rotateY(pos, orbit.x);
    pos.x += orbit.y;
    pos = rotateY(pos, angles.z);
    pos = rotateX(pos, angles.y);
    pos = rotateZ(pos, angles.x);

    v_TexCoord = a_UV;
    gl_Position = gl_ModelViewProjectionMatrix * u_ModelMatrix * vec4(pos, 1.0);
}
