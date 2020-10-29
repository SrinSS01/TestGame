package me.srinjoy.engine.renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetProgramInfoLog;

public class Shader {
    private int shaderProgramID;
    private String vertexSrc;
    private String fragmentSrc;
    private final String filepath;
    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");

            // finding first pattern after #type 'pattern'
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n", index);
            String firstPattern = source.substring(index, eol).trim();

            // finding second pattern after #type 'pattern'
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex"))
                vertexSrc = splitString[1];
            else if (firstPattern.equals("fragment"))
                fragmentSrc = splitString[1];
            else throw new IOException("Unexpected token '" + firstPattern + "'");

            if (secondPattern.equals("vertex"))
                vertexSrc = splitString[2];
            else if (secondPattern.equals("fragment"))
                fragmentSrc = splitString[2];
            else throw new IOException("Unexpected token '" + firstPattern + "'");
        } catch (IOException e) {
            e.printStackTrace();
            assert false : "Error: Could not open file for shader: '" + filepath +"'";
        }
        System.out.println(vertexSrc);
        System.out.println(fragmentSrc);
    }
    public void compile(){
        int vertexID, fragmentID;
        //compile and link shaders
        //load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        //pass shader source to gpu
        glShaderSource(vertexID, vertexSrc);
        glCompileShader(vertexID);
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int length = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tVertexShader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, length));
            assert false : "";
        }
        //load and compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        //pass shader source to gpu
        glShaderSource(fragmentID, fragmentSrc);
        glCompileShader(fragmentID);
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE){
            int length = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tFragmentShader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, length));
            assert false : "";
        }
        //link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        //check for linking error
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE){
            int length = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: '" + filepath + "'\n\tLinking of shaders failed.");
            System.out.println(glGetProgramInfoLog(fragmentID, length));
            assert false : "";
        }
    }
    public void use(){
        // Bind shader program
        glUseProgram(shaderProgramID);
    }
    public void detach(){
        glUseProgram(0);
    }
}
