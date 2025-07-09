module com.example.solidaire {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.solidaire to javafx.fxml;
    exports com.example.solidaire;
}