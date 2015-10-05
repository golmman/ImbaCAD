 #version 330


layout(location = 4) in vec4 inColor;
layout(location = 5) in vec2 inTexCoords;

out vec4 outColor;

uniform sampler2D texDiffuse;

void main (void) {
	//outColor = vec4(1.0f, 0.0f, 0.0f, 1.0f);
	//outColor = inColor;
	
	outColor = vec4(texture(texDiffuse, inTexCoords));
	
	//outColor = inTexCoords;
}