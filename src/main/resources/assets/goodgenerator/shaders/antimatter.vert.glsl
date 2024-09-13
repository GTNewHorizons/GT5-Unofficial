#version 120


uniform float u_SpikeMult;
uniform float u_Scale;
uniform float u_ScaleSnapshot;
uniform vec3 u_ColorCore;
uniform vec3 u_ColorSpike;
uniform float u_Time;
uniform float u_TimeSnapshot;
varying vec3 v_Color;

/*
float wave ( vec3 input){
    return (cos(input.x + u_Time*1.2)*cos(input.y + u_Time*1.3)*cos(input.z + u_Time*1.4));
}
*/
const float PI = 3.14159265358979323846;


float lazyHash(vec3 input){
    vec3 v = fract(input*1.23456 + 3.1456);
    v*=7;
    return fract(v.y + v.x*(v.z+1));
}


float wave ( vec3 input){
    float val = lazyHash(input);
    float pulse = cos(val*2*PI + u_Time)*cos(u_Time*(1+val)); // Slow save
    return pulse;
}

float trim (float val){
    return clamp(val - .1,0,1)/.9;
}

float amp(float x){
    float b = smoothstep(0.,1.,x);
    float c = pow(b,.3);
    return c;
}

float triangle(float x){
    return 1.0 - abs(2.0 * (x - 0.5));
}

void main() {
    //grab local position
    vec3 pos = gl_Vertex.xyz;

    //Grabs how far the vertex is for center
    //Antimatter.model has spikes that are 2 unit away, and the 'core' is 1 unit away
    float len = length(pos);

    //Grabs distance that remains as spike
    float spike = len-1;

    //Spike
    float extension = wave(pos);
    extension = abs(extension);
    float spikeSize = extension * u_SpikeMult;

    //Scale
    float timelerp = clamp(1,0,(u_Time-u_TimeSnapshot)/2.5);
    float scale = mix(u_ScaleSnapshot,u_Scale,timelerp)*(1 + 0.5*spike*(spikeSize - 1));

    vec3 currentCoreColor = mix(u_ColorCore,u_ColorSpike,triangle(mod((u_Time/4.0 + lazyHash(pos)/2),1.0)));
    vec3 currentSpikeColor = mix(u_ColorCore,u_ColorSpike,triangle(mod((u_Time/2.0 + lazyHash(pos)),1.0)));
    //v_Color = mix(u_ColorCore,u_ColorSpike,extension*spike);
    v_Color = mix(currentCoreColor,currentSpikeColor,extension*spike);
    mat4 mScale = mat4(
            scale,0,0,0,
            0,scale,0,0,
            0,0,scale,0,
            0,0,0,1);


    gl_Position = gl_ModelViewProjectionMatrix * mScale * gl_Vertex;
}
