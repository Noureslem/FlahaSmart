package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public class MainLayoutController {

    @FXML
    private BorderPane root;

    private void loadPage(String path) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(path));
            root.setCenter(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        goToList();
    }

    @FXML
    public void goToAdd() {
        loadPage("/views/operation/AjouterOp.fxml");
    }

    @FXML
    public void goToList() {
        loadPage("/views/operation/ListeOp.fxml");
    }

    public void goToAddEq(ActionEvent actionEvent) {
        loadPage("/views.equipement/AjouterEq.fxml");
    }

    public void goToListEq(ActionEvent actionEvent) {
        loadPage("/views.equipement/ListeEq.fxml");
    }

     public void goToDashboard(ActionEvent actionEvent) {
        loadPage("/Dashboard.fxml");
    }
}
