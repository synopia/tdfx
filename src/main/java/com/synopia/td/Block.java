package com.synopia.td;

import com.synopia.engine.HasSprite;
import com.synopia.engine.Initializable;
import com.synopia.engine.Sprite;
import com.synopia.td.events.BlockEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * Created by synopia on 01.01.2015.
 */
public class Block implements Initializable, HasSprite {
    private boolean passable;
    private boolean towerPlaceable;
    private boolean spawn;
    private boolean goal;
    private Sprite sprite;

    private transient Tower tower;
    private transient Position position;

    private String normalFill;
    private String normalStroke;

    public Block() {
    }

    @Override
    public void initialize() {
        Node node = sprite.getShape();
        node.setOnMouseEntered(mouseEvent -> node.fireEvent(BlockEvent.entered(this)));
        node.setOnMouseExited(mouseEvent -> node.fireEvent(BlockEvent.exited(this)));
        node.setOnMouseClicked(mouseEvent -> node.fireEvent(BlockEvent.clicked(this)));
    }

    public boolean isPassable() {
        return passable && (tower == null || tower.isPassable());
    }

    public boolean isTowerPlaceable() {
        return towerPlaceable && tower == null;
    }

    public boolean isSpawn() {
        return spawn;
    }

    public boolean isGoal() {
        return goal;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
        sprite.setPosition(position.toVector2f());
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    public void setHovered(boolean entered) {
        if (entered) {
            normalFill = sprite.getFill();
            normalStroke = sprite.getStroke();

            sprite.setFill(Color.BLACK.toString());
            sprite.setStroke(Color.BLACK.toString());
        } else if (normalFill != null) {

            sprite.setFill(normalFill);
            sprite.setStroke(normalStroke);
        }
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }
}
