#version 120

attribute float a_VertexID;


uniform float u_SegmentQuads;
uniform float u_Time;

//X = radius
//Y = z offset
//Z = transparency
uniform vec3 u_SegmentArray[11];
uniform vec3 u_CameraPosition;
uniform mat4 u_ModelMatrix;



varying vec2 v_TexCoord;
varying vec2 v_localPosition;
varying float v_Transparency;


const float PI = 3.1415926535897;

float getAngle(int quadID, int localID){
    int id_offset = (localID > 1 && localID < 5)? 0:1;
    return ((PI)*(quadID+id_offset))/u_SegmentQuads;
}

void main() {

    int id = int(a_VertexID);
    int segments = (u_SegmentArray.length()-1);
    int quads = int(u_SegmentQuads);

    int localID = int(mod(a_VertexID,6.0));   //Local id of the vertex within a face
    int quadID = int(mod(float(id/6),quads)); //Local id of a quad within a segment
    int segmentID = int(id/(quads*6));
    segmentID = int(min(float(segmentID),float(segments-1)));

    float radius0 = u_SegmentArray[segmentID].x;
    float radius1 = u_SegmentArray[segmentID+1].x;

    float offset0 = u_SegmentArray[segmentID].y;
    float offset1 = u_SegmentArray[segmentID+1].y;

    float trans0 = u_SegmentArray[segmentID].z;
    float trans1 = u_SegmentArray[segmentID+1].z;

    float slope = (radius1-radius0)/(offset1-offset0);


    float cameraAngle = atan(u_CameraPosition.y,u_CameraPosition.x);
    float staticAngle = getAngle(quadID,localID);


    float angle = staticAngle+(cameraAngle-PI/2);

    float offset = (localID > 0 && localID < 4)?offset0:offset1;
    float radius = (localID > 0 && localID < 4)?radius0:radius1;
    v_Transparency = (localID > 0 && localID < 4)?trans0:trans1;

    //radius = min(radius,length(u_CameraPosition.xy));

    vec3 localPosition = vec3(cos(angle)*radius,sin(angle)*radius,offset);
    vec4 worldPosition = u_ModelMatrix*vec4(localPosition,1);

    gl_Position = gl_ModelViewProjectionMatrix * worldPosition;

    v_localPosition = localPosition.xy;

    float timer = u_Time/240;

    float heightOffset = (offset/256) + timer;
    v_TexCoord = vec2(heightOffset,angle/(2*PI)+heightOffset/3 + timer);
}

