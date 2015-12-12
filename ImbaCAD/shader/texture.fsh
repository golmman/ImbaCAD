 #version 330

const int MAX_LIGHTS = 32;

in vec3 fragPos;
in vec4 fragCol;
in vec3 fragNor;
in vec2 fragTex;

flat in vec3 fragNorFlat;
flat in vec4 fragColFlat;

out vec4 outCol;


uniform struct Light {
	int isDirectional;
	vec3 position;			// position
	vec3 color;				// colour of the light
	float attenuation;		// brightness depending on light distance ( = 0.0f for independence of distance)
	float ambient;			// minimum brightness (0.0f to 1.0f)
} light[MAX_LIGHTS];

uniform struct Material {
	sampler2D texture;
	float shininess;
	vec3 specularColor;
	bool flatNormals;
} material;

uniform int numLights;

uniform vec3 camPos;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

vec3 ApplyLight(Light, vec3, vec3, vec3, vec3);

void main (void) {
	
	vec3 normal;
	if (material.flatNormals) {
		normal = normalize(mat3(model) * fragNorFlat);
	} else {
		// remove translation/scaling of the model
		//vec3 normal = normalize(transpose(inverse(mat3(model))) * fragNor);
		normal = normalize(mat3(model) * fragNor);
	}
	vec3 surfacePos = vec3(model * vec4(fragPos, 1.0f));
	vec4 surfaceColor = texture(material.texture, fragTex);
	vec3 surfaceToCamera = normalize(camPos - surfacePos);
	
	//combine color from all the lights
	vec3 linearColor = vec3(0.0f);
	for(int i = 0; i < numLights; ++i){
		linearColor += ApplyLight(light[i], surfaceColor.rgb, normal, surfacePos, surfaceToCamera);
	}
	
	//final lighting color (after gamma correction)
	vec3 gamma = vec3(1.0f); //vec3(1.0f/2.2f);
	vec4 lighting = vec4(pow(linearColor, gamma), surfaceColor.a);
	
	// alpha blend with input color
	vec4 srcCol;
	vec3 outRGB;
	float outA = lighting.a + srcCol.a * (1.0f - lighting.a);
	if (material.flatNormals) {
		srcCol = fragColFlat;
	} else {
		srcCol = fragCol;
	}
	if (outA == 0.0f) {
		outRGB = vec3(0.0f);
	} else {
		outRGB = (srcCol.a * srcCol.rgb + lighting.a * (1.0f - srcCol.a) * lighting.rgb) / outA;
	}
	
	outCol = vec4(outRGB, outA);
}


vec3 ApplyLight(Light light, vec3 surfaceColor, vec3 normal, vec3 surfacePos, vec3 surfaceToCamera) {

    vec3 surfaceToLight;
    float attenuation = 1.0f;

	if(light.isDirectional == 1) {
		//directional light
		surfaceToLight = normalize(light.position.xyz);
		attenuation = 1.0f; //no attenuation for directional lights
	} else {
		//point light
		surfaceToLight = normalize(light.position.xyz - surfacePos);
		float distanceToLight = length(light.position.xyz - surfacePos);
		attenuation = 1.0f / (1.0f + light.attenuation * pow(distanceToLight, 2.0f));
	}

	//ambient
	vec3 ambient = light.ambient * surfaceColor.rgb * light.color;
	
	//diffuse
	float diffuseCoefficient = max(0.0f, dot(normal, surfaceToLight));
	vec3 diffuse = diffuseCoefficient * surfaceColor.rgb * light.color;
	
	//specular
	float specularCoefficient = 0.0f;
	if(diffuseCoefficient > 0.0f)
	    specularCoefficient = pow(max(0.0f, dot(surfaceToCamera, reflect(-surfaceToLight, normal))), material.shininess);
	vec3 specular = specularCoefficient * material.specularColor * light.color;
	
	//linear color (color before gamma correction)
	return ambient + attenuation * (diffuse + specular);
}









