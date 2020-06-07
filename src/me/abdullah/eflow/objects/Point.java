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

    /**
     * Initializes a new Point
     * @param text Text of the point
     * @param textField Textfield of the column
     * @param pane Pane to add to
     * @param column Point's column
     */
    public Point(String text, TextField textField, AnchorPane pane, Column column){
        this.textField = textField;
        this.pane = pane;
        this.column = column;

        // Defining the label and its attributes
        Text label = new Text(text);
        this.label = label;
        label.setFont(new Font("Quicksand-Light", 18));
        label.setWrappingWidth(100);
        label.setBoundsType(TextBoundsType.VISUAL);
        label.setLayoutX(textField.getLayoutX());
        label.setLayoutY(textField.getLayoutY());

        // Adding the label
        pane.getChildren().add(label);

        // Moving the column's textfield down
        textField.setLayoutY(label.getLayoutY() + getSize().getHeight() + 50);
        textField.setText("");

        // Edit point event
        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                editPoint();
            }
        });

        // Adds the point's connection
        this.connection = new Connection(this);
    }

    /**
     * @return The column's textfield
     */
    public TextField getTextField(){
        return textField;
    }

    /**
     * @return The point's label
     */
    public Text getLabel(){
        return label;
    }

    /**
     * @return The point's pane
     */
    public AnchorPane getPane(){
        return pane;
    }

    /**
     * @return The point's connection
     */
    public Connection getConnection(){
        return connection;
    }

    /**
     * @return The point's column
     */
    public Column getColumn(){
        return column;
    }

    /**
     * @return A list of all other points in the column
     */
    public List<Point> getOtherPoints(){
        List<Point> points = column.getPoints();
        points.remove(this);
        return points;
    }

    /**
     * @return Bounds of the label
     */
    public Bounds getSize(){
        return label.localToScene(label.getLayoutBounds());
    }

    /**
     * Opens a textfield to edit the point
     */
    public void editPoint(){
        // Hides the label and connection
        label.setVisible(false);
        connection.setVisibile(false);

        // Creates the edit textfield and defines its attributes
        TextField labelEdit = new TextField();
        labelEdit.setLayoutX(label.getLayoutX());
        labelEdit.setLayoutY(label.getLayoutY());
        labelEdit.setText(label.getText());

        // Adds the textfield
        pane.getChildren().add(labelEdit);

        // Edit event
        Point p = this;
        labelEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Editing the label
                label.setText(labelEdit.getText());

                // Adjusting visibility and positioning of points and connections
                label.setVisible(true);
                column.adjustPositions(p);
                connection.setVisibile(true);
                connection.updatePositions();

                // Removes the edit textfield
                pane.getChildren().remove(labelEdit);
            }
        });
    }
}
