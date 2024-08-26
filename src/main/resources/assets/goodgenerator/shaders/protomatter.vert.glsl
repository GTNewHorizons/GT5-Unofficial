#version 120
attribute float a_VertexID;

uniform float u_CubeCount;
uniform float u_Time;
uniform float u_Scale;
uniform float u_SpiralRadius;
uniform vec3 u_Vertices[24];

const float PI = 3.14159265358979323846;

int modulo(int a, int b){
    return int(mod(float(a),float(b)));
}

int idVertexTransform(float id){
    int i = int(id);
    int l = modulo(i,6);       // Local vertex id for face: 0..5 inclusive
    int v = modulo((l-l/3),4); // Transform 0,1,2,3,4,5 to 0,1,2,2,3,0
    int f = (i/6)*4;           // Grab face offset for vertex
    return f+v;
}


//Best fit equation, needs to be manually calculated
//Alternatives should be looked if shader is going to be multi purpose
float positionEquation(float tickTime){
    float x = tickTime/20;
    float y1 =  7.6331796059e-12*pow(x,1.7827335640e+01);
    float y2 = x/10.0;
    float y = max(y1,y2);
    return y;
}

float velocityEquation(float tickTime){
    return (positionEquation(tickTime + 0.001)-positionEquation(tickTime))/.001;
}

void main() {
    float cubeCount = u_CubeCount;
    float maxDistance = 22.5;

    //This probably needs a better method for generating cubes
    int cube_id = int(a_VertexID)/36;
    int local_id = idVertexTransform(mod(a_VertexID, 36));
    vec3 pos = u_Vertices[local_id];


    //Beam particles y speed
    float loopTime = 100;
    float cycleOffset = (cube_id/cubeCount) * loopTime;
    float cubeTime = mod(u_Time+cycleOffset,loopTime);
    float dist = positionEquation(cubeTime);


    //beam particles position x&z
    float halfCycle = PI * mod(cube_id,2);
    float x_offset = sin(cube_id/cubeCount * 3 * PI + halfCycle) * u_SpiralRadius;
    float z_offset = cos(cube_id/cubeCount * 3 * PI + halfCycle) * u_SpiralRadius;
    float y_offset = maxDistance-dist;

    //scale
    float size = min((cubeTime/loopTime)/.8,1)*u_Scale;
    float tall = size*max(1,maxDistance-y_offset);
    float width = min(size/sqrt(tall),size);

    float y_trim = max(0,tall/2-y_offset);

    tall -= y_trim;
    y_offset += y_trim/2;

    mat4 scale = mat4 (
        width,0,0,0,
        0,tall,0,0,
        0,0,width,0,
        0,0,0,1
    );

    mat4 gravity = mat4(
            1,0,0,0,
            0,1,0,0,
            0,0,1,0,
            x_offset,y_offset,z_offset,1
    );

    gl_Position = gl_ModelViewProjectionMatrix * (gravity * (scale*vec4(pos,1)));
}
