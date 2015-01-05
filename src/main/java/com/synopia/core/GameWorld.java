package com.synopia.core;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Created by synopia on 31.12.2014.
 */
public abstract class GameWorld {
    private Scene gameSurface;
    private Group sceneNodes;
    private static Timeline gameLoop;
    private final int framesPerSecond;
    private final String windowTitle;

    public GameWorld(int framesPerSecond, String windowTitle) {
        this.framesPerSecond = framesPerSecond;
        this.windowTitle = windowTitle;

        buildAndSetGameLoop();
    }

    protected final void buildAndSetGameLoop() {
        Duration oneFrameAmt = Duration.millis(1000 / framesPerSecond);
        KeyFrame oneFrame = new KeyFrame(oneFrameAmt, actionEvent -> {
            update(1.f / framesPerSecond);
        });

        gameLoop = new Timeline(oneFrame);
        gameLoop.setCycleCount(Animation.INDEFINITE);
    }

    public void beginGameLoop() {
        gameLoop.play();
    }

    protected abstract void update(float dt);

    public void initialize(Stage stage) {
        stage.setTitle(windowTitle);
        setSceneNodes(new Group());
        setGameSurface(new Scene(getSceneNodes(), 640, 580));
        stage.setScene(getGameSurface());
        stage.setOnCloseRequest(windowEvent -> System.exit(0));
    }

    public Scene getGameSurface() {
        return gameSurface;
    }

    public void setGameSurface(Scene gameSurface) {
        this.gameSurface = gameSurface;
    }

    public Group getSceneNodes() {
        return sceneNodes;
    }

    public void setSceneNodes(Group sceneNodes) {
        this.sceneNodes = sceneNodes;
    }

    public static Timeline getGameLoop() {
        return gameLoop;
    }
}
