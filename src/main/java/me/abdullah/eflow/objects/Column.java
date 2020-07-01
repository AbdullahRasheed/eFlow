package me.abdullah.eflow.objects;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import me.abdullah.eflow.page.Page;
import me.abdullah.eflow.style.CSS;

import java.util.ArrayList;
import java.util.List;

public class Column {

    /**
     * Comprehensive list of all points inside the given column
     */
    private List<Point> points;

    private Page page;

    private TextField textField;
    private AnchorPane pane;

    /**
     * Initializes the column within the given AnchorPane
     * @param x The x value of the newColumn
     * @param y The y value: newColumn + 150
     */
    public Column(double x, double y, Page page){
        this.pane = page.getPane();
        this.page = page;

        // Defining the points list
        points = new ArrayList<>();

        // Creating the points field
        textField = new TextField();
        CSS.applyDefaultFieldCSS(textField);
        textField.setLayoutX(x);
        textField.setLayoutY(y);
        pane.getChildren().add(textField);

        // Localizing its actions (to create a point)
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                points.add(createPoint(textField.getText()));
            }
        });
    }

    /**
     * Returns the points within the column
     * @return List<Point> inside the column
     */
    public List<Point> getPoints(){
        return points;
    }

    /**
     * Creates a point inside the column
     * @param text Point text
     * @return The point
     */
    public Point createPoint(String text){
        return new Point(text, textField.getLayoutX(), textField.getLayoutY(), this);
    }

    public Point createPoint(String text, double x, double y){
        return new Point(text, x, y, this);
    }

    /**
     * Adjusts the position of all points in the column after the given point
     * @param p Pivot point
     */
    public void adjustPositions(Point p){
        // Loops through all points after the pivot point
        for (int i = points.indexOf(p); i < points.size(); i++) {
            // If there is no previous point to base off of, no adjustment is needed
            if(i == 0) continue;

            // Defining the current and previous points
            Point point = points.get(i);
            Point previousPoint = points.get(i-1);

            // Adjusting the point's position based on the previous point's Y and height
            point.getLabel().setLayoutY(previousPoint.getLabel().getLayoutY() + previousPoint.getSize().getHeight() + 50);
            point.getConnection().updatePositions();
            textField.setLayoutY(point.getLabel().getLayoutY() + point.getSize().getHeight() + 50);
        }
    }

    public Page getPage() {
        return page;
    }

    public TextField getTextField(){
        return textField;
    }

    public AnchorPane getPane(){
        return pane;
    }
}
