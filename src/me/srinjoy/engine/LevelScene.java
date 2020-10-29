package me.srinjoy.engine;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;

public class LevelScene extends Scene{
    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;
    public LevelScene() {
        System.out.println("Inside level scene");
        Window.getInstance().r = 1;
        Window.getInstance().g = 1;
        Window.getInstance().b = 1;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void update(float dt) {
        if (!changingScene && KeyListener.isKeyPressed(GLFW_KEY_SPACE)) changingScene = true;
        if (changingScene && timeToChangeScene > 0){
            timeToChangeScene -= dt;
            Window.getInstance().r -= dt * 5.0f;
            Window.getInstance().g += dt * 5.0f;
            Window.getInstance().b -= dt * 5.0f;
        }
        else if (changingScene)  Window.changeScene(0);
    }
}
