package controllers.equipement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import models.Equipement;
import services.EquipementService;

import java.sql.SQLException;
import java.util.List;

public class ListeEqController {

    @FXML
    private VBox listContainer;

    EquipementService service = new EquipementService();

    @FXML
    public void initialize() {
        afficherEquipements();
    }

    public void afficherEquipements() {
        listContainer.getChildren().clear();

        try {
            List<Equipement> list = service.afficher();

            for (Equipement e : list) {
                VBox card = createCard(e);
                listContainer.getChildren().add(card);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private VBox createCard(Equipement e) {

        VBox card = new VBox(10);
        card.getStyleClass().add("card");

        // header
        HBox header = new HBox(10);

        Label nom = new Label(e.getType());
        nom.getStyleClass().add("card-title");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button btnEdit = new Button("Modifier");
        btnEdit.getStyleClass().add("edit-button");

        Button btnDelete = new Button("Supprimer");
        btnDelete.getStyleClass().add("danger-button");

        header.getChildren().addAll(nom, spacer, btnEdit, btnDelete);

        // infos
        Label type = new Label("Type : " + e.getNom());
        Label etat = new Label("État : " + e.getEtat());

        if (!e.getEtat().equalsIgnoreCase("libre")) {
            btnDelete.setDisable(true);
        }


        card.getChildren().addAll(header, type, etat);

        // actions
        btnDelete.setOnAction(ev -> {
            try {
                service.supprimer(e);
                afficherEquipements();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });

        btnEdit.setOnAction(ev -> ouvrirModification(e));


        return card;
    }

    private void ouvrirModification(Equipement e) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/views.equipement/ModifierEq.fxml")
            );

            Parent root = loader.load();

            ModifierEqController controller = loader.getController();
            controller.setEquipement(e);
            controller.setListeController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root, 400, 300));
            stage.setTitle("Modifier équipement");
            stage.show();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void trier(ActionEvent actionEvent) {
    }
}
