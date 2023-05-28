module com.example.astaesteiss {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.model.astaesteiss to javafx.fxml;
    exports com.model.astaesteiss;
}