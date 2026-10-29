module edu.fpt.swt301.demologin {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.fpt.swt301.demologin to javafx.fxml;
    exports edu.fpt.swt301.demologin;
}