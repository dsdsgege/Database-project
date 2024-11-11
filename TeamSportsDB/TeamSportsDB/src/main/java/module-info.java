module com.teamsportsdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.teamsportsdb to javafx.fxml;
    exports com.teamsportsdb;
}