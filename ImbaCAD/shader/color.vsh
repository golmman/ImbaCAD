#version 330

layout(location = 0) in vec4 vertPos;
layout(location = 1) in vec4 vertCol;

out vec4 fragCol;
flat out vec4 fragColFlat;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main(void) {
	fragCol = vertCol;
	fragColFlat = vertCol;

	gl_Position = projection * view * model * vertPos;
	gl_PointSize = 10.0;
}