 #version 330

struct Light {
   vec3 position;
   vec3 intensities;
} light;

in vec3 fragmentPosition;
in vec4 fragmentColor;
in vec3 fragmentNormal;
in vec2 fragmentTexCoords;

out vec4 outColor;

uniform sampler2D texDiffuse;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main (void) {
	//outColor = vec4(1.0f, 0.0f, 0.0f, 1.0f);
	//outColor = fragmentColor;
	
	
	light.position = vec3(0.0f, 0.0f, 5.0f);
	light.intensities = vec3(1.0f, 1.0f, 1.0f);
	
	
	//calculate normal in world coordinates
	mat3 normalMatrix = transpose(inverse(mat3(model)));
	vec3 normal = normalize(normalMatrix * fragmentNormal);
	
	//calculate the location of this fragment (pixel) in world coordinates
    vec3 fragPosition = vec3(model * vec4(fragmentPosition, 1));
	
	//calculate the vector from this pixels surface to the light source
	vec3 surfaceToLight = light.position - fragPosition;
	
	//calculate the cosine of the angle of incidence
	float brightness = dot(normal, surfaceToLight) / (length(surfaceToLight) * length(normal));
	brightness = clamp(brightness, 0.0f, 1.0f);
	
	
	vec4 surfaceColor = texture(texDiffuse, fragmentTexCoords);
	
	outColor = vec4(brightness * light.intensities * surfaceColor.rgb, surfaceColor.a);
	
	//outColor = fragmentTexCoords;
}