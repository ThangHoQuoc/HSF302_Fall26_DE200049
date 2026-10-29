module com.fu.ch08 {
    requires javafx.controls;


    // Application class phải được export để javafx.graphics khởi tạo được
    // b1–b3 viết UI bằng code (không FXML) → chỉ cần exports, không cần opens
    requires javafx.fxml;

    exports com.fu.ch08.b4;
    opens com.fu.ch08.b4 to javafx.fxml;
    exports com.fu.ch08.b1;
    exports com.fu.ch08.b2;
    exports com.fu.ch08.b3;
}
