package me.abdullah.eflow;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import me.abdullah.eflow.page.Page;
import me.abdullah.eflow.page.PageSwitcher;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public AnchorPane mainPane;
    public ScrollPane scrollPane;
    public AnchorPane controlPane;
    public Label newColumn;
    public JFXButton addPageButton;
    public JFXListView pageView;


    // List of all columns
    private static List<Page> pages = new ArrayList<>();
    private static Page currentPage;

    public static String TEXTFIELD_STYLESHEET;

    public static List<Node> DEFAULT_NODES;

    /**
     * Initializes the application
     * @param location Location of application
     * @param resources The application's resources
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TEXTFIELD_STYLESHEET = getClass().getClassLoader().getResource("textfield_style.css").toExternalForm();
        DEFAULT_NODES = new ArrayList<>(controlPane.getChildren());

        currentPage = new Page(controlPane, "Page 1");
        addPage(currentPage);
        addPage(new Page(controlPane, "Page 2"));

        pageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(pageView.getSelectionModel().getSelectedIndices().size() < 1) return;
                int index = (int)pageView.getSelectionModel().getSelectedIndices().get(0);
                PageSwitcher.switchPage(currentPage, pages.get(index), controlPane);
                currentPage = pages.get(index);
            }
        });

        // Adds new column
        newColumn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                // Creates a new column and adds it to the list
                currentPage.addColumn(newColumn.getLayoutX(), newColumn.getLayoutY() + 100);
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

    public void addPage(Page page){
        pages.add(page);
        pageView.getItems().add(page.getName());
    }
}
