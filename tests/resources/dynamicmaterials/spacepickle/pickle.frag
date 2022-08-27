varying vec2 v_texCoords;
varying vec2 v_position;

uniform sampler2D u_texture;
uniform sampler2D u_mainTexture;
uniform sampler2D u_maskTexture;

void main() {
	vec4 stencilFragment = texture2D(u_texture, v_texCoords);
	vec4 mainFragment = texture2D(u_mainTexture, v_texCoords);
	vec4 maskFragment = texture2D(u_maskTexture, vec2(v_texCoords.x * 4.0, v_texCoords.y * 4.0));
	if (stencilFragment.rgb == mainFragment.rgb) {
		gl_FragColor = maskFragment; //просто смена RGB не работает, почему - хуй знает
	} else discard; //отсечение перегороженных пикселей
	if (stencilFragment.a < 0.1) discard; //отсечение прозрачности
}
