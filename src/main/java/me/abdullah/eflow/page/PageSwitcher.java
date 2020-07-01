package me.abdullah.eflow.page;

import javafx.scene.layout.AnchorPane;

public class PageSwitcher {

    public static void switchPage(Page from, Page to, AnchorPane pane){
        from.storeNodes();
        pane.getChildren().clear();
        pane.getChildren().addAll(to.getNodes());
    }

    public static void loadPage(Page page, AnchorPane pane){
        pane.getChildren().clear();
        pane.getChildren().addAll(page.getNodes());
    }
}
