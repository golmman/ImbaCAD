#version 330

layout(location = 0) in vec4 inPosition;
layout(location = 1) in vec4 inColor;
//layout(location = 2) in vec3 inNormal;
layout(location = 3) in vec2 inTexCoords;

out vec4 fragmentColor;
out vec2 fragmentTexCoords;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main(void) {
	fragmentColor = inColor;
	fragmentTexCoords = inTexCoords;
	gl_Position = projection * view * model * inPosition;
}