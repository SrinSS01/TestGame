package me.srinjoy.engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

public class KeyListener {
    private static KeyListener instance;
    private final boolean[] keyPressed = new boolean[350];
    public static KeyListener getInstance(){
        return KeyListener.instance == null ? KeyListener.instance = new KeyListener() : KeyListener.instance;
    }
    public static void keyCallback(long window, int key, int scancode, int action, int mods){
        getInstance().keyPressed[key] = action == GLFW_PRESS;
        Window.fadeTo = key;
//        else if (action == GLFW_RELEASE) getInstance().keyPressed[key] = false;
    }

    public static boolean isKeyPressed(int keyCode) {
        return getInstance().keyPressed[keyCode];
    }
}
