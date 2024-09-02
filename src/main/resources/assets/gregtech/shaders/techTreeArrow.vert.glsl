#version 120

uniform vec2 u_ScreenSize;

//Control pointers for the bezier curve
attribute vec2 a_p0;
attribute vec2 a_p1;
attribute vec2 a_p2;

uniform vec2 u_p0;

varying vec2 v_p0;
varying vec2 v_p1;
varying vec2 v_p2;


vec2 fragmentCoord(vec2 v){
    vec4 cs = gl_ModelViewProjectionMatrix * vec4(v, 0, 1);
    vec2 ncd = cs.xy/cs.w;
    vec2 ws = (ncd * 0.5 + 0.5) * u_ScreenSize;
    return ws;
}


void main() {
    v_p0 = fragmentCoord(a_p0);
    v_p1 = fragmentCoord(a_p1);
    v_p2 = fragmentCoord(a_p2);

    gl_Position = gl_ModelViewProjectionMatrix*gl_Vertex;
}
