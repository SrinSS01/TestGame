package me.srinjoy.engine;

import me.srinjoy.engine.util.Time;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import java.util.Objects;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private final int width;
    private final int height;
    private final String title;
    private static Window window = null;
    private long glfwWindow;
    public float r, g, b;
    private static Scene currentScene;
    public static int fadeTo;

    private Window() {
        this.width = 1366;
        this.height = 768;
        this.title = "Minecraft";
        r = 1;
        g = 1;
        b = 1;
    }

    public static void changeScene(int newScene){
        switch (newScene) {
            case 0 -> {
                currentScene = new LevelEditorScene();
                currentScene.init();
            }
            case 1 -> {
                currentScene = new LevelScene();
                currentScene.init();
            }
            default -> {
                assert false : "Unknown Scene '" + newScene + "'";
            }
        }
    }

    public static Window getInstance(){
        return window == null ? window = new Window() : window;
    }

    public void create() {
        System.out.println("LWJGL " + Version.getVersion() + "!");
        init();
        loop();
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }

    private void loop() {
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;

        while (!glfwWindowShouldClose(glfwWindow)){
            glfwPollEvents();
            glClearColor(r, g, b, 0);
            glClear(GL_COLOR_BUFFER_BIT);
            if (dt >= 0)
                currentScene.update(dt);


            glfwSwapBuffers(glfwWindow);
            switch (fadeTo) {
                case GLFW_KEY_R -> {
                    r = Math.min(r + 0.01f, 2);
                    g = Math.max(g - 0.01f, 0);
                    b = Math.max(b - 0.01f, 0);
                }
                case GLFW_KEY_G -> {
                    r = Math.max(r - 0.01f, 0);
                    g = Math.min(g + 0.01f, 2);
                    b = Math.max(b - 0.01f, 0);
                }
                case GLFW_KEY_B -> {
                    r = Math.max(r - 0.01f, 0);
                    g = Math.max(g - 0.01f, 0);
                    b = Math.min(b + 0.01f, 2);
                }
            }
            glfwSetWindowTitle(glfwWindow, title+"\t [FPS : " + (int)(1 / dt) + "] \t" + (char) (Math.random() * 1000));

            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW.");
        // window properties
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        // creating the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if (glfwWindow == NULL) throw new IllegalStateException("Failed to create the GLFW window!");

        // setting callbacks
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        glfwShowWindow(glfwWindow);

        GL.createCapabilities();
        Window.changeScene(0);
    }
}
