#version 330

layout(location = 0) in vec4 inPosition;
layout(location = 1) in vec4 inColor;

out vec4 fragmentColor;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main(void) {
	fragmentColor = inColor;
	gl_Position = projection * view * model * inPosition;
	gl_PointSize = 10.0;
}