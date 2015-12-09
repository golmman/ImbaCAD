 #version 330

in vec4 fragCol;
flat in vec4 fragColFlat;

out vec4 outCol;

uniform bool flatColors;

void main (void) {
	//outColor = vec4(1.0f, 0.0f, 0.0f, 1.0f);

	if (flatColors) {
		outCol = fragColFlat;
	} else {
		outCol = fragCol;
	}
}