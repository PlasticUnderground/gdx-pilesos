varying vec2 v_texCoords;
varying vec2 v_position;

uniform sampler2D u_texture;
uniform sampler2D u_mainTexture;
uniform sampler2D u_maskTexture;

vec4 blur13(sampler2D image, vec2 uv, vec2 resolution, vec2 direction) {
	vec4 color = vec4(0.0);
	vec2 off1 = vec2(1.411764705882353) * direction;
	vec2 off2 = vec2(3.2941176470588234) * direction;
	vec2 off3 = vec2(5.176470588235294) * direction;
	color += texture2D(image, uv) * 0.1964825501511404;
	color += texture2D(image, uv + (off1 / resolution)) * 0.2969069646728344;
	color += texture2D(image, uv - (off1 / resolution)) * 0.2969069646728344;
	color += texture2D(image, uv + (off2 / resolution)) * 0.09447039785044732;
	color += texture2D(image, uv - (off2 / resolution)) * 0.09447039785044732;
	color += texture2D(image, uv + (off3 / resolution)) * 0.010381362401148057;
	color += texture2D(image, uv - (off3 / resolution)) * 0.010381362401148057;
	return color;
}

void main() {
	vec4 stencilFragment = texture2D(u_texture, v_texCoords);
	vec4 mainFragment = texture2D(u_mainTexture, v_texCoords);
	vec4 maskFragment = texture2D(u_maskTexture, vec2(v_texCoords.x * 4.0, v_texCoords.y * 4.0));
	if (stencilFragment.rgb == mainFragment.rgb) {
		gl_FragColor = maskFragment; //просто смена RGB не работает, почему - хуй знает
	} else discard; //отсечение перегороженных пикселей
	if (stencilFragment.a < 0.1) discard; //отсечение прозрачности
}
