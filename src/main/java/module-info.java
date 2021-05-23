module hellofx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;

    opens org.openjfx to javafx.fxml;
    exports org.openjfx;
}