package controllers.Auth;

import controllers.Admin.ListUser;
import controllers.Dashbord.DashboardAdmController;
import controllers.Dashbord.ClientHomeController;
import entities.User;
import entities.Role;
import services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    private TextField username;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Text errorMessage;

    @FXML
    private Button registerButton;

    private UserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ Login controller initialisé");
        if (errorMessage != null) {
            errorMessage.setVisible(false);
        }
    }

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
        String email = username.getText().trim();
        String password = passwordField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs.");
            return;
        }

        try {
            User user = userService.getUserByEmail(email);

            if (user == null) {
                showError("Email ou mot de passe incorrect.");
                return;
            }

            System.out.println("Utilisateur trouvé: " + user.getEmail());
            System.out.println("Rôle: " + user.getRole());
            System.out.println("Mot de passe en base: " + user.getPassword());
            System.out.println("Mot de passe saisi: " + password);

            // VÉRIFICATION AVEC BCRYPT
            boolean passwordMatches;

            // Vérifier si le mot de passe en base est un hash BCrypt
            if (user.getPassword().startsWith("$2a$")) {
                // Comparaison avec BCrypt
                passwordMatches = BCrypt.checkpw(password, user.getPassword());
                System.out.println("Vérification BCrypt: " + passwordMatches);
            } else {
                // Comparaison simple (pour les anciens mots de passe en clair)
                passwordMatches = password.equals(user.getPassword());
                System.out.println("Vérification simple: " + passwordMatches);
            }

            if (!passwordMatches) {
                showError("Email ou mot de passe incorrect.");
                return;
            }

            if (user.getActif() == null || !user.getActif()) {
                showError("Votre compte est désactivé.");
                return;
            }

            errorMessage.setVisible(false);
            System.out.println("✅ Connexion réussie pour: " + email);

            redirectUser(user, event);

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur de connexion: " + e.getMessage());
        }
    }

    private void redirectUser(User user, ActionEvent event) {
        try {
            Role role = user.getRole();
            Stage stage = (Stage) loginButton.getScene().getWindow();

            if (role == Role.ADMINISTRATEUR) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardAdm.fxml"));
                Parent root = loader.load();

                DashboardAdmController controller = loader.getController();
                controller.setLoggedInUser(user);

                stage.setScene(new Scene(root));
                stage.setTitle("FlahaSmart - Administration");

            } else if (role == Role.CLIENT) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ClientHome.fxml"));
                Parent root = loader.load();

                ClientHomeController controller = loader.getController();
                controller.setLoggedInUser(user);

                stage.setScene(new Scene(root));
                stage.setTitle("FlahaSmart - Accueil");

            } else if (role == Role.AGRICULTEUR) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardAgriculteur.fxml"));
                Parent root = loader.load();

                stage.setScene(new Scene(root));
                stage.setTitle("FlahaSmart - Espace Agriculteur");

            } else {
                showError("Rôle non reconnu: " + role);
                return;
            }

            stage.setMaximized(true);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur de redirection: " + e.getMessage());
        }
    }

    @FXML
    protected void handleRegisterButtonAction(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Register.fxml"));
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("FlahaSmart - Inscription");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur lors du chargement de l'inscription.");
        }
    }

    private void showError(String message) {
        if (errorMessage != null) {
            errorMessage.setText(message);
            errorMessage.setVisible(true);
        } else {
            System.err.println("❌ Erreur: " + message);
        }
    }
}