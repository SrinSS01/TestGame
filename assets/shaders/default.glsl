#type vertex
#version 460
    layout (location=0) in vec3 aPos;
    layout (location=1) in vec4 aColour;

    out vec4 fColour;

    void main(){
        fColour = aColour;
        gl_Position = vec4(aPos, 1.0);
    }

#type fragment
#version 460
    in vec4 fColour;
    out vec4 colour;
    void main(){
        colour = fColour;
    }