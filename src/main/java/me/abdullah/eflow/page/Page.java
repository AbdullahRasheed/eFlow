package me.abdullah.eflow.page;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import me.abdullah.eflow.Controller;
import me.abdullah.eflow.objects.Column;

import java.util.ArrayList;
import java.util.List;

public class Page {

    private List<Node> nodes;

    public List<Column> columns = new ArrayList<>();

    private AnchorPane pane;
    private String name;
    public Page(AnchorPane pane, String name){
        this.pane = pane;
        this.name = name;
        nodes = new ArrayList<>(Controller.DEFAULT_NODES);
    }

    public void addColumn(double x, double y){
        columns.add(new Column(pane, x, y));
    }

    public List<Column> getColumns(){
        return columns;
    }

    public void storeNodes(){
        nodes = new ArrayList<>(pane.getChildren());
    }

    public List<Node> getNodes(){
        return nodes;
    }

    public String getName(){
        return name;
    }

}
