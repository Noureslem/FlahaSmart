package controllers.Dashbord;

import entities.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardAgriculteurController {

    @FXML
    private Label agriculteurNameLabel;

    @FXML
    private Text welcomeText;

    @FXML
    private Text dateText;

    @FXML
    private Button logoutButton;

    private User loggedInUser;

    @FXML
    public void initialize() {
        // Afficher la date du jour
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        dateText.setText(today.format(formatter));
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
        if (user != null) {
            String prenom = user.getPrenom() != null ? user.getPrenom() : "";
            String nom = user.getNom() != null ? user.getNom() : "";
            agriculteurNameLabel.setText(prenom + " " + nom);
            welcomeText.setText("Bienvenue " + prenom + " dans votre espace agriculteur !");
        }
    }

    @FXML
    private void handleDashboard() {
        // Déjà sur le dashboard
        System.out.println("Tableau de bord");
    }

    @FXML
    private void handleCultures() {
        System.out.println("Navigation vers Cultures");
    }

    @FXML
    private void handlePlanification() {
        System.out.println("Navigation vers Planification");
    }

    @FXML
    private void handleRecoltes() {
        System.out.println("Navigation vers Récoltes");
    }

    @FXML
    private void handleRapports() {
        System.out.println("Navigation vers Rapports");
    }

    @FXML
    private void handleLogout() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion - FlahaSmart");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}