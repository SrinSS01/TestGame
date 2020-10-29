package me.srinjoy;

import me.srinjoy.engine.Window;

public class Main {
    public void start(){
        Thread game = new Thread(this::init, "game");
        game.start();
    }
    public void init(){
        Window window = Window.getInstance();
        window.create();
    }
    public static void main(String... args) {
        new Main().start();
    }
}
