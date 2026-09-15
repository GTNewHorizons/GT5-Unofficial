#version 330 core

uniform mat4 u_MVP;
uniform float u_SectionHeight;
uniform int u_BaseY;
uniform vec2 u_UV[2]; // {min, max}

out vec2 v_TexCoord;
flat out float v_yPos;

const float LONG_DISTANCE = 0.447076585625; // (1.0 + sqrt(2.0)) / 5.4
const float SHORT_DISTANCE = 0.185185185185; // 1.0f / 5.4f

const vec2 EDGE[8] = vec2[] (
    vec2( LONG_DISTANCE,  SHORT_DISTANCE), vec2( LONG_DISTANCE, -SHORT_DISTANCE),
    vec2( SHORT_DISTANCE, -LONG_DISTANCE), vec2(-SHORT_DISTANCE, -LONG_DISTANCE),
    vec2(-LONG_DISTANCE, -SHORT_DISTANCE), vec2(-LONG_DISTANCE,  SHORT_DISTANCE),
    vec2(-SHORT_DISTANCE,  LONG_DISTANCE), vec2( SHORT_DISTANCE,  LONG_DISTANCE)
);

const vec4 STRAND[4] = vec4[] (
    vec4( 1.0,  0.0, 0.0, 0.0),
    vec4( 0.0,  1.0, 0.0, 1.0),
    vec4(-1.0,  0.0, 1.0, 1.0),
    vec4( 0.0, -1.0, 1.0, 0.0)
);

const vec2 QUAD_SEL[6] = vec2[] (
    vec2(1.0, 0.0), vec2(1.0, 1.0), vec2(0.0, 1.0),
    vec2(1.0, 0.0), vec2(0.0, 1.0), vec2(0.0, 0.0)
);

const float SIDE = 2.0 / 5.4;

void main() {
    uint id = uint(gl_VertexID);
    uint quadRawVtxIdx = id % 6u;
    id /= 6u;
    uint inPart = id % 8u;
    id /= 8u;
    uint inStrand = id % 4u;
    uint curSegment = id / 4u;

    vec2 sel = QUAD_SEL[quadRawVtxIdx];
    vec2 edge = EDGE[sel.x > 0.0 ? (inPart + 1u) % 8u : inPart];

    vec3 p = vec3(
        0.5 + edge.x,
        SIDE * float(inPart) + SIDE * sel.x + 0.75 * sel.y + u_SectionHeight * float(curSegment),
        0.5 + edge.y
    );

    vec4 s = STRAND[inStrand];
    vec4 pos = vec4(p.x * s.x + p.z * s.y + s.z, p.y, -p.x * s.y + p.z * s.x + s.w, 1.0);

    gl_Position = u_MVP * pos;

    v_TexCoord = mix(u_UV[1], u_UV[0], sel);
    v_yPos = max(float(u_BaseY) + u_SectionHeight * float(curSegment), 0.0);
}
