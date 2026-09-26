#version 330 core

uniform float u_SegmentQuads;
uniform float u_Time;

//X = radius
//Y = z offset
//Z = transparency
uniform vec3 u_SegmentArray[11];
uniform float u_CameraAngle;
uniform mat4 u_MVP;

out vec2 v_TexCoord;
out vec2 v_localPosition;
out float v_Transparency;

const float PI = 3.1415926535897;

float getAngle(int quadID, int localID){
    int id_offset = (localID > 1 && localID < 5)? 0:1;
    return ((PI)*(quadID+id_offset))/u_SegmentQuads;
}

void main() {

    int id = gl_VertexID;
    int segments = u_SegmentArray.length()-1;
    int quads = int(u_SegmentQuads);

    int localID = id % 6; // Local id of the vertex within a face
    int quadID = (id / 6) % quads; // Local id of a quad within a segment
    int segmentID = id / (quads*6);
    segmentID = min(segmentID, segments-1);

    float radius0 = u_SegmentArray[segmentID].x;
    float radius1 = u_SegmentArray[segmentID+1].x;

    float offset0 = u_SegmentArray[segmentID].y;
    float offset1 = u_SegmentArray[segmentID+1].y;

    float trans0 = u_SegmentArray[segmentID].z;
    float trans1 = u_SegmentArray[segmentID+1].z;

    float angle = getAngle(quadID,localID) + (u_CameraAngle - PI/2);

    float offset = (localID > 0 && localID < 4)?offset0:offset1;
    float radius = (localID > 0 && localID < 4)?radius0:radius1;
    v_Transparency = (localID > 0 && localID < 4)?trans0:trans1;

    vec3 localPosition = vec3(cos(angle)*radius,sin(angle)*radius,offset);

    gl_Position = u_MVP * vec4(localPosition,1);

    v_localPosition = localPosition.xy;

    float timer = u_Time/240;

    float heightOffset = (offset/256) + timer;
    v_TexCoord = vec2(heightOffset,angle/(2*PI)+heightOffset/3 + timer);
}
