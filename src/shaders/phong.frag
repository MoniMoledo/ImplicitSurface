#version 330

smooth in vec4 theColor;
smooth in vec4 newNormal;
smooth in vec3 lightDir;
smooth in vec3 v;

uniform vec3 lightPos;
uniform vec3 ambientColor;
uniform vec3 diffuseColor;
uniform vec3 speclarColor;
uniform float kA, kD, kS, sN;


out vec4 outputColor;

void main()
{
    //diffuse
    float iD = max(0.0, dot(lightDir, newNormal.xyz));

    //specular
    vec3  h  =  normalize(lightDir + v);
    float iS =  pow(max(0.0, dot(newNormal.xyz, h)), sN);

    vec3 lightFactor = kA * ambientColor + kD * iD * diffuseColor + kS * iS * speclarColor;

    outputColor = vec4(theColor.rgb * lightFactor, theColor.a);

}