#version 120

uniform float u_Time;
uniform float u_Stability;
uniform float u_Scale;
uniform vec3 u_CameraPosition;

varying vec2 v_TexCoord;

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

    v_TexCoord = gl_MultiTexCoord0.xy;

    //Extremely fragile system to deteriminte if isolate parts of the blackhole
    //Would break with any uv remaping or atlas stiching
    bool isDisk = (abs(v_TexCoord.y-.5f)>.245);
    bool isBack = (abs(v_TexCoord.x-.5f)>.245) && isDisk;
    bool isFront = (abs(v_TexCoord.x-.5f)<.255) && isDisk;
    bool isBot = (v_TexCoord.y < .5) && isBack;

    float yAngle = atan(u_CameraPosition.z,u_CameraPosition.x) - PI/2;
    float c = cos(yAngle);
    float s = sin(yAngle);
    mat4 yRotate = mat4(
        vec4(c, 0.0, s, 0.0),   // First column
        vec4(0.0, 1.0, 0.0, 0.0), // Second column
        vec4(-s, 0.0, c, 0.0),  // Third column
        vec4(0.0, 0.0, 0.0, 1.0) // Fourth column
    );


    float base = length(gl_Vertex.xyz);
    float stab = (base>HORIZON_EDGE)?u_Stability:1;

    float scale = ((base-HORIZON_EDGE)*stab+HORIZON_EDGE)/base;
    scale = max(scale, .1);
    scale*=u_Scale;


    vec4 rotated = yRotate * vec4((gl_Vertex.xyz*scale),1);

    vec3 cameraDirection = normalize(u_CameraPosition);
    cameraDirection = !isBot?cameraDirection:-cameraDirection;
    vec3 rotateAxis = cross(cameraDirection,vec3(0,1,0));
    float angle = acos(dot(cameraDirection,vec3(0,1,0)));
    if (isFront) angle = 0;

    float instabilityRotation =(u_Stability<=0)?u_Time/10:0;

    mat4 rotate = rotateMatrix(angle,normalize(rotateAxis));


    mat4 rotateB = rotateMatrix(instabilityRotation,normalize(u_CameraPosition));

    gl_Position = gl_ModelViewProjectionMatrix * (rotateB* (rotate * (rotated)));
}
