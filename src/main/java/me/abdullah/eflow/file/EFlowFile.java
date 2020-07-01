package me.abdullah.eflow.file;

import me.abdullah.eflow.Controller;
import me.abdullah.eflow.objects.Column;
import me.abdullah.eflow.objects.Connection;
import me.abdullah.eflow.objects.Point;
import me.abdullah.eflow.page.Page;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class EFlowFile {

    /**
     * EXAMPLE:
     * Page 1
     * Column{"Point 1" [1 1;1 2] x y,"Point 2" [] x y}
     * Column{"Other Point 1" [0 1] x y,"Other Point 2" [] x y}
     * -
     */

    private File file;
    public EFlowFile(File file){
        this.file = file;
    }

    public void write() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write("");
        for (Page page : Controller.getPages()) {
            writer.append(page.getName() + "\n");
            for (Column column : page.getColumns()) {
                writer.append(columnAsString(column));
                writer.append("\n");
            }
            writer.append("-" + "\n");
        }
        writer.close();
    }

    public static String pointAsString(Point point){
        String connections = "";
        for (Connection connection : point.getConnection().getConnections().keySet()) {

            Point p = connection.getPoint();
            Column column = p.getColumn();
            Page page = column.getPage();

            connections += page.getColumns().indexOf(column) + " " + column.getPoints().indexOf(p) + ";";
        }
        return '"' + point.getLabel().getText() + '"' + " [" + connections.substring(0, connections.length() - 1) + "] " + point.getLabel().getLayoutX() + " " + point.getLabel().getLayoutY();
    }

    public static String columnAsString(Column column){
        String c = "Column{";
        for (Point point : column.getPoints()) {
            c += pointAsString(point) + ",";
        }
        return c.substring(0, c.length() - 1) + "}";
    }
}
