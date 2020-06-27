package me.abdullah.eflow.style;

import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import me.abdullah.eflow.Controller;

public class CSS {

    public static void applyDefaultFieldCSS(TextField textField){
        textField.getStylesheets().add(Controller.TEXTFIELD_STYLESHEET);
        textField.setFont(new Font("Quicksand-Light", 12));
        textField.setMinHeight(32);
    }
}
