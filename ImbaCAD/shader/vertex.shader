#version 330

#extension GL_ARB_separate_shader_objects: enable

layout(location = 0) in vec4 inPosition;
layout(location = 1) in vec4 inColor;
//layout(location = 2) in vec3 inNormal;
layout(location = 3) in vec2 inTexCoords;

layout(location = 4) out vec4 outColor;
layout(location = 5) out vec2 outTexCoords;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main(void) {
	outColor = inColor;
	outTexCoords = inTexCoords;
	gl_Position = projection * view * model * inPosition;
}