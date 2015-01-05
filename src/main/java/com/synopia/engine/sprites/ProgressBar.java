package com.synopia.engine.sprites;

import com.synopia.engine.Sprite;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by synopia on 05.01.2015.
 */
public class ProgressBar extends Sprite {
    private transient Group group;
    private transient Rectangle bar;
    private transient Rectangle fill;
    private float progressValue;

    public ProgressBar() {
        group = new Group();

        bar = new Rectangle();
        bar.setStrokeWidth(0.1);
        bar.setStroke(Color.BLACK);
        bar.setFill(Color.WHITE);
        bar.setX(-0.5);
        bar.setY(-0.7);
        bar.setWidth(1);
        bar.setHeight(0.2);

        fill = new Rectangle();
        fill.setStrokeWidth(0);
        fill.setX(-0.5);
        fill.setY(-0.7);
        fill.setFill(Color.DARKGREEN);
        fill.setWidth(progressValue);
        fill.setHeight(0.2);

        group.getChildren().add(bar);
        group.getChildren().add(fill);
    }

    public void setProgressValue(float progressValue) {
        this.progressValue = progressValue;
        fill.setWidth(progressValue);
    }

    @Override
    public void setFill(String color) {
        super.setFill(color);
        fill.setFill(Color.web(color));
    }

    @Override
    public void setStroke(String stroke) {
        super.setStroke(stroke);
        bar.setFill(Color.web(stroke));
    }

    @Override
    public Node getShape() {
        return group;
    }
}
