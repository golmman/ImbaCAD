#version 330

layout(location = 0) in vec3 inPosition;
layout(location = 1) in vec4 inColor;
layout(location = 2) in vec3 inNormal;
layout(location = 3) in vec2 inTexCoords;

out vec3 fragmentPosition;
out vec4 fragmentColor;
out vec3 fragmentNormal;
out vec2 fragmentTexCoords;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main(void) {

	fragmentPosition = inPosition;
	fragmentColor = inColor;
	fragmentNormal = inNormal;
	fragmentTexCoords = inTexCoords;
	
	gl_Position = projection * view * model * vec4(inPosition, 1.0f);
	
	gl_PointSize = 10.0f;
}