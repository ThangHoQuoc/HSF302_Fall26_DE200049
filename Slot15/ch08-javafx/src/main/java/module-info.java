module com.fu.ch08 {
    requires javafx.controls;


    // Application class phải được export để javafx.graphics khởi tạo được
    // b1–b3 viết UI bằng code (không FXML) → chỉ cần exports, không cần opens
    exports com.fu.ch08.b1;

}
