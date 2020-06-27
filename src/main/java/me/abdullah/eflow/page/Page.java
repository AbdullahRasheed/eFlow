package me.abdullah.eflow.page;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import me.abdullah.eflow.objects.Column;

import java.util.ArrayList;
import java.util.List;

public class Page {

    private List<Node> nodes = new ArrayList<>();

    public List<Column> columns = new ArrayList<>();
    private int column = 0;

    private AnchorPane pane;
    public Page(AnchorPane pane){
        this.pane = pane;
    }

    public void addColumn(){
        columns.add(new Column(pane, column));
        column++;
    }

    public List<Column> getColumns(){
        return columns;
    }

    public void storeNodes(){
        nodes = pane.getChildren();
    }

    public List<Node> getNodes(){
        return nodes;
    }

}
