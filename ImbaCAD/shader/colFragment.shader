 #version 330

in vec4 fragmentColor;
out vec4 outColor;

void main (void) {
	//outColor = vec4(1.0f, 0.0f, 0.0f, 1.0f);
	outColor = fragmentColor;
}