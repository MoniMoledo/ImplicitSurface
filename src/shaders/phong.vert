#version 330

layout (location = 0) in vec4 position;
layout (location = 1) in vec4 normal;
layout (location = 2) in vec4 color;

//matrix
uniform mat4 modelmatrix;
uniform mat4 viewmatrix;
uniform mat4 projection;

//light parameters
uniform vec3 lightPos;
uniform vec3 ambientColor;
uniform vec3 diffuseColor;
uniform vec3 speclarColor;
uniform float kA, kD, kS, sN;

//ilumination variables dor frag shader
smooth out vec4 theColor;
smooth out vec4 newNormal;
smooth out vec4 positionWorld;
smooth out vec3 v;


//light parameters for frag shader
smooth out vec3 lightPosF;
smooth out vec3 ambientColorF; 
smooth out vec3 diffuseColorF;
smooth out vec3 speclarColorF;
smooth out float kAF, kDF, kSF, sNF;

void main()
{
    lightPosF = lightPos;
    ambientColorF = ambientColor; 
    diffuseColorF = diffuseColor;
    speclarColorF = speclarColor;   
    kAF = kA;
    kDF = kD;
    kSF = kS;
    sNF = sN;

    mat4 modelView = viewmatrix * modelmatrix;
    mat4 normalMatrix = transpose(inverse(modelView));

    // final vertex position
    gl_Position = projection * modelView * position;

    positionWorld = modelmatrix * position;
    newNormal = normalize(normalMatrix * normal);

    //specular
    v  = -normalize((modelView * position).xyz);
    theColor = color;
}