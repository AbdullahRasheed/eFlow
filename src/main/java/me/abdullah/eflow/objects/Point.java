package me.abdullah.eflow.objects;

import javafx.geometry.Bounds;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import me.abdullah.eflow.style.EditableText;

import java.util.List;

public class Point {

    private EditableText editableText;
    private Connection connection;
    private Column column;

    /**
     * Initializes a new Point
     * @param text Text of the point
     * @param column Point's column
     */
    public Point(String text, double x, double y, Column column){
        this.column = column;

        this.editableText = new EditableText(x, y, text, this);

        editableText.setOnEditStart(new Runnable() {
            @Override
            public void run() {
                connection.setVisibile(false);
            }
        });

        Point p = this;
        editableText.setOnEditComplete(new Runnable() {
            @Override
            public void run() {
                column.adjustPositions(p);
                connection.setVisibile(true);
                connection.updatePositions();
            }
        });

        // Moving the column's textfield down
        column.getTextField().setLayoutY(editableText.getLabel().getLayoutY() + getSize().getHeight() + 50);
        column.getTextField().setText("");

        // Adds the point's connection
        this.connection = new Connection(this);
    }

    /**
     * @return The point's label
     */
    public EditableText getEditableText(){
        return editableText;
    }

    public Text getLabel(){
        return editableText.getLabel();
    }

    /**
     * @return The point's pane
     */
    public AnchorPane getPane(){
        return column.getPane();
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
        return editableText.getLabel().localToScene(editableText.getLabel().getLayoutBounds());
    }
}
