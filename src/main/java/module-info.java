module org.jetbrains.finguard {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.jetbrains.finguard to javafx.fxml;
    exports org.jetbrains.finguard;
    exports application;
    opens application to javafx.fxml;
}
