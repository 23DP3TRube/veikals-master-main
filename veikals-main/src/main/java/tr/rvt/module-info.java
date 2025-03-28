module tr.rvt {
    requires javafx.controls;
    requires javafx.fxml;

    opens tr.rvt to javafx.fxml;
    exports tr.rvt;
}