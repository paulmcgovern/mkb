#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoord0;

varying vec3 nard;

uniform vec2 u_resolution;
uniform vec2 u_center;
uniform float u_innerRadius;
uniform float u_outerRadius;

uniform vec4 u_color;
uniform sampler2D u_sampler2D;

const float outerRadius = .8, innerRadius = .1, intensity = .55;

void main() {
     

    vec4 color = texture2D(u_sampler2D, v_texCoord0) * v_color;

     // Center of screen
     //   vec2 relativePosition = (gl_FragCoord.xy / u_resolution.xy) - vec2(0.5);
                 
    vec2 relativePosition = (gl_FragCoord.xy / u_resolution.xy) - (u_center.xy / u_resolution.xy);

    //    vec2 relativePosition = (gl_FragCoord.xy / u_resolution )- (u_center / u_resolution);                
           
    // Normalize aspect     
    relativePosition.x *= u_resolution.x / u_resolution.y;
        
        float len = length(relativePosition);
        
   //     float vignette = smoothstep(outerRadius, innerRadius, len);
         float vignette = smoothstep( u_outerRadius, u_innerRadius, len);

    color.rgb = mix(color.rgb, color.rgb * vignette, intensity);
   if( gl_FragCoord.x == relativePosition.x ) {
gl_FragColor = vec4(1, 0, 0, 1);
} else {
    gl_FragColor = color;
        }
    //  gl_FragColor = vec4( u_color.rgb, 1.0 );
}