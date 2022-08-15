module apachestorm.frontend {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires org.json;
    requires mongo.java.driver;
    requires org.controlsfx.controls;

    opens apachestorm.frontend to javafx.fxml;
    exports apachestorm.frontend;
}
