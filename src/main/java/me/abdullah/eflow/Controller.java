package me.abdullah.eflow;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import me.abdullah.eflow.objects.Column;
import me.abdullah.eflow.page.Page;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public AnchorPane mainPane;
    public ScrollPane scrollPane;
    public AnchorPane controlPane;
    public Label newColumn;
    public JFXSlider pageSlider;
    public JFXButton addPageButton;

    // List of all columns
    private static List<Page> pages = new ArrayList<>();
    private static Page currentPage;

    public static String TEXTFIELD_STYLESHEET;

    /**
     * Initializes the application
     * @param location Location of application
     * @param resources The application's resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TEXTFIELD_STYLESHEET = getClass().getClassLoader().getResource("textfield_style.css").toExternalForm();

        currentPage = new Page(controlPane);
        pages.add(currentPage);

        // Adds new column
        newColumn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Creates a new column and adds it to the list
                currentPage.addColumn();
                newColumn.setLayoutX(newColumn.getLayoutX() + 200);
            }
        });
    }

    /**
     * @return Returns an ordered list of all columns
     */
    public static List<Page> getPages(){
        return pages;
    }

    public static Page getCurrentPage(){
        return currentPage;
    }

    public static void setCurrentPage(Page page){
        currentPage = page;
    }
}
