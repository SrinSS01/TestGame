package me.srinjoy.engine;

import me.srinjoy.engine.renderer.Shader;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {
    private Shader defaultShader;
    private float[] vertexArray = {
            /*
                1   2
                3   0
             */
            // position            // colour
            0.5f, -0.5f, 0.0f,     1.0f, 0.0f, 0.0f, 1.0f, // Bottom right  0
           -0.5f,  0.5f, 0.0f,     0.0f, 1.0f, 0.0f, 1.0f, // Top left      1
            0.5f,  0.5f, 0.0f,     0.0f, 0.0f, 1.0f, 1.0f, // Top right     2
           -0.5f, -0.5f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f, // Bottom left   3
    };
    // Must be in counter-clockwise order
    private int[] elementArray = {
            2, 1, 0, // Top right triangle
            0, 1, 3, // Bottom left triangle
    };
    private int vaoID, vboID, eboID;

    @Override
    public void init() {
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        // Generate VAO, VBO, EBO buffer objects and send to GPU
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // create float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // create VBO upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // create indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // add vertex attribute pointers
        int positionSize = 3;
        int colourSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionSize + colourSize) * floatSizeBytes;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);
        glVertexAttribPointer(1, colourSize, GL_FLOAT, false, vertexSizeBytes, positionSize * vertexSizeBytes);
        glEnableVertexAttribArray(1);
    }

    public LevelEditorScene() {

    }

    @Override
    public void update(float dt) {
        defaultShader.use();
        // Bind VAO
        glBindVertexArray(vaoID);
        // enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        defaultShader.detach();

    }
}
