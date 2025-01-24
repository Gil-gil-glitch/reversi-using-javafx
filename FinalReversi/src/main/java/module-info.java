module course.finalreversi {
    requires javafx.controls;
    requires javafx.fxml;


    opens course.finalreversi to javafx.fxml;
    exports course.finalreversi;
}