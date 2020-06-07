package me.abdullah.eflow.objects;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.List;

public class Point {

    private Text label;
    private TextField textField;
    private AnchorPane pane;
    private Connection connection;
    private Column column;
    public Point(String text, TextField textField, AnchorPane pane, Column column){
        this.textField = textField;
        this.pane = pane;
        this.column = column;

        Text label = new Text(text);
        this.label = label;
        label.setFont(new Font("Quicksand-Light", 18));
        label.setWrappingWidth(100);
        label.setBoundsType(TextBoundsType.VISUAL);
        label.setLayoutX(textField.getLayoutX());
        label.setLayoutY(textField.getLayoutY());
        pane.getChildren().add(label);
        textField.setLayoutY(label.getLayoutY() + getSize().getHeight() + 50);
        textField.setText("");

        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                editPoint();
            }
        });

        this.connection = new Connection(this);
    }

    public TextField getTextField(){
        return textField;
    }

    public Text getLabel(){
        return label;
    }

    public AnchorPane getPane(){
        return pane;
    }

    public Connection getConnection(){
        return connection;
    }

    public Column getColumn(){
        return column;
    }

    public List<Point> getOtherPoints(){
        List<Point> points = column.getPoints();
        points.remove(this);
        return points;
    }

    public Bounds getSize(){
        return label.localToScene(label.getLayoutBounds());
    }

    public void editPoint(){
        label.setVisible(false);

        TextField labelEdit = new TextField();
        labelEdit.setLayoutX(label.getLayoutX());
        labelEdit.setLayoutY(label.getLayoutY());
        labelEdit.setText(label.getText());
        pane.getChildren().add(labelEdit);
        connection.setVisibile(false);
        Point p = this;
        labelEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                label.setText(labelEdit.getText());
                label.setVisible(true);
                pane.getChildren().remove(labelEdit);
                connection.updatePositions();
                connection.setVisibile(true);
                column.adjustPositions(p);
            }
        });
    }
}
