module com.fu.ch08 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;      // T5
    requires javafx.media;    // T5

    // FXMLLoader cần reflection vào controller (@FXML private field/method)
    opens com.fu.ch08.b4 to javafx.fxml;
    opens com.fu.ch08.b5 to javafx.fxml;
    opens com.fu.ch08.b6 to javafx.fxml;
    opens com.fu.ch08.t1 to javafx.fxml, javafx.base;   // javafx.base: PropertyValueFactory (TableView)
    opens com.fu.ch08.t4 to javafx.fxml, javafx.base;

    // Application class phải được export để javafx.graphics khởi tạo được
    // b1–b3 viết UI bằng code (không FXML) → chỉ cần exports, không cần opens
    exports com.fu.ch08.b1;

}
