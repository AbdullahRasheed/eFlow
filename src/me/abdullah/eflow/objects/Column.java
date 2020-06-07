package me.abdullah.eflow.objects;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;
import java.util.List;

public class Column {

    private List<Point> points;

    private TextField textField;
    public Column(AnchorPane pane, int column){
        points = new ArrayList<>();

        textField = new TextField();
        textField.setLayoutX(50 + column*200);
        textField.setLayoutY(150);
        pane.getChildren().add(textField);

        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                points.add(createPoint(textField.getText(), textField, pane));
            }
        });
    }

    public List<Point> getPoints(){
        return points;
    }

    public Point createPoint(String text, TextField field, AnchorPane pane){
        return new Point(text, field, pane, this);
    }

    public void adjustPositions(Point p){
        for (int i = points.indexOf(p); i < points.size(); i++) {
            if(i == 0) continue;

            Point point = points.get(i);
            Point previousPoint = points.get(i-1);
            point.getLabel().setLayoutY(previousPoint.getLabel().getLayoutY() + previousPoint.getSize().getHeight() + 50);
            point.getConnection().updatePositions();
            textField.setLayoutY(point.getLabel().getLayoutY() + point.getSize().getHeight() + 50);
        }
    }
}
