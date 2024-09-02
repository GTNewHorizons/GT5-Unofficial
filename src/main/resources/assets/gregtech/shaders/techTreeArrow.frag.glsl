#version 120

varying vec2 v_p0;
varying vec2 v_p1;
varying vec2 v_p2;
uniform float u_dist;

vec2 quadraticBezier(float t) {
    return (1.0 - t) * (1.0 - t) * v_p0 +
    2.0 * (1.0 - t) * t * v_p1 +
    t * t * v_p2;
}
vec2 quadraticBezierDerivative(float t){
    return 2*(v_p1-v_p0)+2*(v_p0-2*v_p1+v_p2)*t;
}

vec2 quadraticBezierSecondDerivative(float t){
    return 2*(v_p0-2*v_p1+v_p2);
}

float distanceToBezier(vec2 p, float t) {
    vec2 bezierPoint = quadraticBezier(t);
    return length(bezierPoint - p);
}

float distanceToBezierSquared(float t,vec2 p){
    vec2 B = quadraticBezier(t);
    vec2 pB2 = B-p;
    return dot(pB2,pB2);
}

float distanceToBezierSquaredDerivative(float t,vec2 p){
    return 2*dot(quadraticBezierDerivative(t),(quadraticBezier(t) - p));
}

float distanceToBezierSquaredSecondDerivative(float t, vec2 p){
    vec2 d1t = quadraticBezierDerivative(t);
    vec2 d2t = quadraticBezierSecondDerivative(t);
    return 2*(dot(d2t,(quadraticBezier(t) - p)) + dot(d1t,d1t));
}

float findClosestT(vec2 p){
    float eps = .001;
    int iteration = 0;
    int maxIterations = 4; //Two looks presentable, 3 seems good, 4 for good measure
    float t = 0.5; //Initial guess

    // Newton-Raphson Method
    // Minimizes the distance squared insteadof the distance for numerical stability
    while (iteration < maxIterations) {
        float dt = distanceToBezierSquaredDerivative(t, p);
        float dtt = distanceToBezierSquaredSecondDerivative(t, p);

        if (abs(dt) < eps) {
            break;
        }

        float deltaT = dt / dtt;
        t -= deltaT;

        if (abs(deltaT) < eps) {
            break;
        }
        iteration++;
    }
    return t;
}

void main() {
    vec2 p = gl_FragCoord.xy;
    float t = findClosestT(p);
    float dist = distanceToBezier(p, t);

    float lazyAlias = smoothstep(0, 1, (u_dist-dist+1)/(u_dist+1));

    if (dist < u_dist)
        gl_FragColor = vec4(1,1,1,lazyAlias);
    else
        gl_FragColor = vec4(0,0,0,0);
}
