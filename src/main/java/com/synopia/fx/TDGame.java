package com.synopia.fx;

import com.synopia.core.GameWorld;
import com.synopia.engine.EntityManager;
import com.synopia.td.Tower;
import com.synopia.td.World;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by synopia on 31.12.2014.
 */
public class TDGame extends GameWorld {

    private World world;

    public TDGame(int framesPerSecond, String windowTitle) {
        super(framesPerSecond, windowTitle);
    }

    @Override
    public void initialize(Stage stage) {
        super.initialize(stage);

        load();

        VBox box = new VBox() {{
            setSpacing(5);
            setTranslateX(10);
            setTranslateY(10);
            getChildren().add(new HBox() {{
                setSpacing(5);
                getChildren().add(new Label("XY"));
                getChildren().add(new Button() {{
                    setText("Freeze/Resume");
                    setOnAction(actionEvent -> {
                        switch (getGameLoop().getStatus()) {
                            case RUNNING:
                                getGameLoop().stop();
                                break;
                            case STOPPED:
                                getGameLoop().play();
                                break;
                        }
                    });
                }});
            }});
        }};
//        getSceneNodes().getChildren().add(box);
    }

    @Override
    protected void update(float dt) {
        world.tick(dt);
    }

    private void load() {
        world = new World(getSceneNodes());
        world.getEntityManager().loadJson(EntityManager.class.getResourceAsStream("/damage.json"));
        world.getEntityManager().loadJson(EntityManager.class.getResourceAsStream("/towers.json"));
        world.getEntityManager().loadJson(EntityManager.class.getResourceAsStream("/units.json"));
        world.getEntityManager().loadJson(EntityManager.class.getResourceAsStream("/map.json"));
        world.getEntityManager().loadJson(EntityManager.class.getResourceAsStream("/waves.json"));

        world.findDamageTypes();
        world.loadGrid("map_element_td");
        world.loadWave("wave_1");


        Tower t1 = world.createTower("archer_tower_1");
        world.getGrid().setActiveTower(t1);


//        Unit u1 = world.createUnit("peon");
//        u1.setPosition(world.getGrid().getSpawnBlocks().get(0).getPosition().toVector2f());
//        for (int i = 0; i < 25; i++) {
//            world.tick(1);
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//
//            }
//            System.out.println(u1.getPosition());
//        }

    }
}
