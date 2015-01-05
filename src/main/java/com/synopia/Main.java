package com.synopia;

import com.synopia.core.GameWorld;
import com.synopia.fx.TDGame;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by synopia on 31.12.2014.
 */
public class Main extends Application {
    GameWorld gameWorld = new TDGame(60, "TD");

    @Override
    public void start(Stage stage) throws Exception {
        gameWorld.initialize(stage);
        gameWorld.beginGameLoop();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
