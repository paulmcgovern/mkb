#ifdef GL_ES
precision mediump float;
#endif

uniform float time;
uniform vec2 mouse;
uniform vec2 resolution;

#define BLADES 3.
#define BIAS 0.2
#define SHARPNESS 1.0
#define COLOR 0.9, 0.9, 0.1
#define BG 0.9, 0.7, 0.1

void main( void ) {

    vec2 position = (( gl_FragCoord.xy / resolution.xy ) - vec2(0.5)) / vec2(resolution.y/resolution.x,1.0);
    vec3 color = vec3(0.);
    
    float blade = clamp(pow(sin(time+atan(position.y,position.x)*BLADES)+BIAS, SHARPNESS), 0.0, 1.0);
    
    color = mix(vec3(COLOR), vec3(BG), blade);
    
    gl_FragColor = vec4( color, 2.0 );

