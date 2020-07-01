package me.abdullah.eflow.file;

import javafx.scene.layout.AnchorPane;
import me.abdullah.eflow.Controller;
import me.abdullah.eflow.objects.Column;
import me.abdullah.eflow.objects.Point;
import me.abdullah.eflow.page.Page;
import me.abdullah.eflow.page.PageSwitcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EFlowParser {

    private File file;
    private Controller controller;
    public EFlowParser(File file, Controller controller){
        this.file = file;
        this.controller = controller;
    }

    public void load(){
        try {
            controller.clearPages();
            for (Page page : parsePages()) {
                controller.addPage(page);
            }
            controller.setCurrentPage(Controller.pages.get(0));
            PageSwitcher.loadPage(Controller.getCurrentPage(), controller.controlPane);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public Point parsePoint(String s, Column column){
        String[] valArray = s.split(Character.toString('"'));
        String[] spacedArray = s.split(" ");
        String vals = valArray[valArray.length - 1].substring(1);

        String text = betweenFinal(s, '"', '"');
        String connections = betweenFinal(vals, '[', ']');
        double x = Double.parseDouble(spacedArray[spacedArray.length-2]);
        double y = Double.parseDouble(spacedArray[spacedArray.length-1]);
        Point point = column.createPoint(text, x, y);
        for (String s1 : connections.split(";")) {
            if(s1.equals("") || s1.equals(null)) continue;
            int[] coords = new int[2];
            String[] coordsAsString = s1.split(" ");
            for(int i = 0; i < coordsAsString.length; i++){
                coords[i] = Integer.parseInt(coordsAsString[i]);
            }
            point.getConnection().queueConnection(coords);
        }
        return point;
    }

    public Column parseColumn(String s, Page page){
        Column column = page.addColumn(page.getColumns().size() * 200 + 200, controller.newColumn.getLayoutY());

        String rawPoints = betweenFinal(s, '{', '}');
        String[] points = rawPoints.split(",");
        for (String point : points) {
            column.getPoints().add(parsePoint(point, column));
        }
        return column;
    }

    public List<Page> parsePages() throws IOException {
        List<Page> pages = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String s;
        while((s = reader.readLine()) != null){
            AnchorPane pane = new AnchorPane();
            Page page = new Page(pane, s);
            String column;
            while(!(column = reader.readLine()).equals("-")){
                parseColumn(column, page);
            }
            reshapePage(page);
            pages.add(page);
        }
        return pages;
    }

    public void reshapePage(Page page){
        page.storeNodes();
        page.getNodes().addAll(new ArrayList<>(Controller.DEFAULT_NODES));
        page.setPane(controller.controlPane);
    }

    public String betweenFinal(String s, char c1, char c2){
        int i1 = 0;
        int i2 = 0;
        char[] chars = s.toCharArray();
        for(int i = 0; i < chars.length; i++){
            if(chars[i] == c1) i1 = Math.min(i1, i);
            if(chars[i] == c2) i2 = i;
        }
        return s.substring(i1 + 1, i2);
    }
}
