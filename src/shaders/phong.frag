#version 330

smooth in vec4 theColor;
smooth in vec4 newNormal;
smooth in vec4 positionWorld;
smooth in vec3 v;

smooth in vec3 lightPosF;
smooth in vec3 ambientColorF;
smooth in vec3 diffuseColorF;
smooth in vec3 speclarColorF;
smooth in float kAF, kDF, kSF, sNF;


out vec4 outputColor;

void main()
{
    //diffuse
    vec3 lightDir = normalize(lightPosF - positionWorld.xyz);
    float iD = max(0.0, dot(lightDir, newNormal.xyz));

    //specular
    vec3  h  =  normalize(lightDir + v);
    float iS =  pow(max(0.0, dot(newNormal.xyz, h)), sNF);

    vec3 lightFactor = kAF * ambientColorF + kDF * iD * diffuseColorF + kSF * iS * speclarColorF;

    outputColor = vec4(theColor.rgb * lightFactor, theColor.a);

}