#version 330

layout(location = 0) in vec3 vertPos;
layout(location = 1) in vec4 vertCol;
layout(location = 2) in vec3 vertNor;
layout(location = 3) in vec2 vertTex;

out vec3 fragPos;
out vec4 fragCol;
out vec3 fragNor;
out vec2 fragTex;

flat out vec3 fragNorFlat;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main(void) {

	fragPos = vertPos;
	fragCol = vertCol;
	fragNor = vertNor;
	fragTex = vertTex;

	fragNorFlat = vertNor;
	
	gl_Position = projection * view * model * vec4(vertPos, 1.0f);
	
	gl_PointSize = 10.0f;
}