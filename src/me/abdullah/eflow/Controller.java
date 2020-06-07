package me.abdullah.eflow;

import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import me.abdullah.eflow.objects.Column;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public AnchorPane mainPane;
    public ScrollPane scrollPane;
    public AnchorPane controlPane;
    public Label newColumn;

    public int column = 0;

    private static List<Column> columns = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newColumn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                columns.add(new Column(controlPane, column));
                column++;
                newColumn.setLayoutX(newColumn.getLayoutX() + 200);
            }
        });
    }

    public static List<Column> getColumns(){
        return columns;
    }
}
