module com.teamsportsdb {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires jdk.compiler;
    requires mysql.connector.j;


    opens com.teamsportsdb to javafx.fxml;
    exports com.teamsportsdb;
    exports com.teamsportsdb.utils;
    opens com.teamsportsdb.utils to javafx.fxml;
    exports com.teamsportsdb.ui;
    opens com.teamsportsdb.ui to javafx.fxml;
}