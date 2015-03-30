#ifdef GL_ES
precision mediump float;
#endif


varying vec4 v_color;
varying vec2 v_texCoord0;

varying vec3 nard;

uniform vec2 u_resolution;
uniform vec2 u_center;
uniform vec4 u_color;
uniform sampler2D u_sampler2D;

const float outerRadius = .8, innerRadius = .1, intensity = .55;

void main() {
     
        vec4 color = texture2D(u_sampler2D, v_texCoord0) * v_color;

    //    vec2 relativePosition = gl_FragCoord.xy / u_resolution - .5;
                
 vec2 relativePosition = (gl_FragCoord.xy / u_resolution )- (u_center / u_resolution);                
                
        relativePosition.x *= u_resolution.x / u_resolution.y;
        
        float len = length(relativePosition);
        
        float vignette = smoothstep(outerRadius, innerRadius, len);
        
        color.rgb = mix(color.rgb, color.rgb * vignette, intensity);
   
        gl_FragColor = color;
        
    //  gl_FragColor = vec4( u_color.rgb, 1.0 );
}