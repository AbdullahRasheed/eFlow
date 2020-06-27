package me.abdullah.eflow.style;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import me.abdullah.eflow.objects.Point;

public class EditableText {

    private Text label;
    private Point point;
    public EditableText(double x, double y, String s, Point point){
        this.point = point;

        // Defining the label and its attributes
        Text label = new Text(s);
        this.label = label;
        label.setFont(new Font("Quicksand-Light", 18));
        label.setWrappingWidth(100);
        label.setBoundsType(TextBoundsType.VISUAL);
        label.setLayoutX(x);
        label.setLayoutY(y);

        // Adding the label
        point.getPane().getChildren().add(label);

        // Edit point event
        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                editPoint();
            }
        });
    }

    /**
     * Opens a textfield to edit the point
     */
    public void editPoint(){
        // Hides the label and connection
        label.setVisible(false);
        point.getConnection().setVisibile(false);

        // Creates the edit textfield and defines its attributes
        TextField labelEdit = new TextField();
        CSS.applyDefaultFieldCSS(labelEdit);
        labelEdit.setLayoutX(label.getLayoutX());
        labelEdit.setLayoutY(label.getLayoutY());
        labelEdit.setText(label.getText());

        // Adds the textfield
        point.getPane().getChildren().add(labelEdit);

        // Edit event
        labelEdit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Editing the label
                label.setText(labelEdit.getText());

                // Adjusting visibility and positioning of points and connections
                label.setVisible(true);
                point.getColumn().adjustPositions(point);
                point.getConnection().setVisibile(true);
                point.getConnection().updatePositions();

                // Removes the edit textfield
                point.getPane().getChildren().remove(labelEdit);
            }
        });
    }

    public Text getLabel(){
        return label;
    }
}
