
// https://github.com/mattdesl/lwjgl-basics/wiki/ShaderLesson3

//texture 0
uniform sampler2D u_texture;


//"in" attributes from our vertex shader
varying vec4 vColor;
varying vec2 vTexCoord;

vec3 BLACK = vec3(0.3, 0.3, 0.8);
vec4 RED = vec4(1, 0, 0, 1);
vec4 BLUE = vec4(0,0,1,1);
vec4 CLEAR = vec4( 0, 0, 0, 0 );

vec4 SHADOW = vec4( 0.2, 0.2, 1, 0.5);

void main() {
   
    vec4 texColor = texture2D(u_texture, vTexCoord);

//	if(  texColor.rgb == BLACK ) {
	
//		texColor = CLEAR;
		
//	} else {
//		texColor = vec4( SHADOW.rgb, 0.75 );   
//	}
     
//	gl_FragColor = texColor;   
    gl_FragColor = vec4( BLACK.rgb, texColor[3] * 0.5);
}