package me.srinjoy.engine;

import static org.lwjgl.glfw.GLFW.*;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private final boolean[] mouseButtonPressed = new boolean[3];
    private boolean isDragging;
    private MouseListener(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }
    public static MouseListener getInstance(){
        return MouseListener.instance == null ? MouseListener.instance = new MouseListener() : MouseListener.instance;
    }
    public static void mousePosCallback(long window, double xpos, double ypos){
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
        getInstance().xPos = xpos;
        getInstance().yPos = ypos;
        getInstance().isDragging = getInstance().mouseButtonPressed[0] || getInstance().mouseButtonPressed[1] || getInstance().mouseButtonPressed[2];
    }
    public static void mouseButtonCallback(long window, int button, int action, int mods){
        if (button < getInstance().mouseButtonPressed.length) {
            if (action == GLFW_PRESS) getInstance().mouseButtonPressed[button] = true;
            else if (action == GLFW_RELEASE) {
                getInstance().mouseButtonPressed[button] = false;
                getInstance().isDragging = false;
            }
        }
    }
    public static void mouseScrollCallback(long window, double xOffset, double yOffset){
        getInstance().scrollX = xOffset;
        getInstance().scrollY = yOffset;
    }
    public static void endFrame(){
        getInstance().scrollX = 0;
        getInstance().scrollY = 0;
        getInstance().lastX = getInstance().xPos;
        getInstance().lastY = getInstance().yPos;
    }

    public float getScrollX() {
        return (float) getInstance().scrollX;
    }

    public float getScrollY() {
        return (float) getInstance().scrollY;
    }

    public float getX() {
        return (float) getInstance().xPos;
    }

    public float getY() {
        return (float) getInstance().yPos;
    }

    public float getDX() {
        return (float) (getInstance().lastX - getInstance().xPos);
    }

    public float getDY() {
        return (float) (getInstance().lastY - getInstance().yPos);
    }

    public boolean getMouseButtonPressed(int button) {
        return button < getInstance().mouseButtonPressed.length && getInstance().mouseButtonPressed[button];
    }

    public boolean isDragging() {
        return getInstance().isDragging;
    }
}
