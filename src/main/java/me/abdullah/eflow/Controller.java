package me.abdullah.eflow;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import me.abdullah.eflow.file.EFlowFile;
import me.abdullah.eflow.file.EFlowParser;
import me.abdullah.eflow.page.Page;
import me.abdullah.eflow.page.PageSwitcher;

import java.io.File;
import java.io.IOException;
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
    public JFXButton loadButton;
    public JFXButton downloadEflowButton;
    public JFXButton downloadPdfButton;

    public FileChooser fileChooser;

    // List of all columns
    public static List<Page> pages = new ArrayList<>();
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

        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("E-Flow Files", "*.eflow"));

        currentPage = new Page(controlPane, "Page 1");
        addPage(currentPage);
        addPage(new Page(controlPane, "Page 2"));

        pageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(pageView.getSelectionModel().getSelectedIndices().size() < 1) return;
                int index = (int)pageView.getSelectionModel().getSelectedIndices().get(0);
                PageSwitcher.switchPage(currentPage, pages.get(index), controlPane);
                setCurrentPage(pages.get(index));
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

        addPageButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String pageName = "Page " + (pages.size() + 1);
                addPage(new Page(controlPane, pageName));
            }
        });

        Controller controller = this;
        loadButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                File file = fileChooser.showOpenDialog(controlPane.getScene().getWindow());
                if(file == null) return;
                EFlowParser parser = new EFlowParser(file, controller);
                parser.load();
            }
        });

        downloadEflowButton.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    File file = fileChooser.showSaveDialog(controlPane.getScene().getWindow());
                    if(file == null) return;
                    file.createNewFile();
                    EFlowFile eFlowFile = new EFlowFile(file);
                    eFlowFile.write();
                }catch (IOException e){
                    e.printStackTrace();
                }
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

    public void setCurrentPage(Page page){
        currentPage = page;
        newColumn.setLayoutX(200 * (currentPage.getColumns().size() + 1));
    }

    public void clearPages(){
        pages.clear();
        pageView.getItems().clear();
    }

    public void addPage(Page page){
        pages.add(page);
        pageView.getItems().add(page.getName());
    }
}
