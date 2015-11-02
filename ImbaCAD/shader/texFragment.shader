 #version 330

in vec4 fragmentColor;
in vec2 fragmentTexCoords;

out vec4 outColor;

uniform sampler2D texDiffuse;

void main (void) {
	//outColor = vec4(1.0f, 0.0f, 0.0f, 1.0f);
	//outColor = fragmentColor;
	
	outColor = vec4(texture(texDiffuse, fragmentTexCoords));
	
	//outColor = fragmentTexCoords;
}