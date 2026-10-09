#version 330 core

uniform float u_CubeCount;
uniform float u_Time;
uniform float u_Scale;
uniform float u_SpiralRadius;
uniform vec3 u_Vertices[24];
uniform mat4 u_MVP;

const float PI = 3.14159265358979323846;

int idVertexTransform(int i) {
    int l = i % 6;        // Local vertex id for face: 0..5 inclusive
    int v = (l - l / 3) % 4; // Transform 0,1,2,3,4,5 to 0,1,2,2,3,0
    int f = (i / 6) * 4;  // Grab face offset for vertex
    return f + v;
}

float positionEquation(float tickTime) {
    float x = tickTime / 20;
    float y1 = 7.6331796059e-12 * pow(x, 1.7827335640e+01);
    float y2 = x / 10.0;
    float y = max(y1, y2);
    return y;
}

void main() {
    const float maxDistance = 22.5;

    int cube_id = gl_VertexID / 36;
    int local_id = idVertexTransform(gl_VertexID % 36);
    vec3 pos = u_Vertices[local_id];

    float cubeF = float(cube_id);

    //Beam particles y speed
    const float loopTime = 100.0;
    float cycleOffset = (cubeF / u_CubeCount) * loopTime;
    float cubeTime = mod(u_Time + cycleOffset, loopTime);
    float dist = positionEquation(cubeTime);

    //beam particles position x&z
    float halfCycle = PI * float(cube_id % 2);
    float x_offset = sin(cubeF / u_CubeCount * 3 * PI + halfCycle) * u_SpiralRadius;
    float z_offset = cos(cubeF / u_CubeCount * 3 * PI + halfCycle) * u_SpiralRadius;
    float y_offset = maxDistance - dist;

    //scale
    float size = min((cubeTime / loopTime) / .8, 1) * u_Scale;
    float tall = size * max(1, maxDistance - y_offset);
    float width = min(size / sqrt(tall), size);

    float y_trim = max(0, tall / 2 - y_offset);

    tall -= y_trim;
    y_offset += y_trim / 2;

    gl_Position = u_MVP * vec4(pos * vec3(width, tall, width) + vec3(x_offset, y_offset, z_offset), 1.0);
}
