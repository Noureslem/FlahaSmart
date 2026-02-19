package controllers.Auth;

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

import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class Register implements Initializable {

    @FXML
    private TextField nom;

    @FXML
    private TextField prenom;

    @FXML
    private TextField email;

    @FXML
    private TextField telephone;

    @FXML
    private TextField adresse;

    @FXML
    private TextField ville;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private ComboBox<String> roleComboBox;  // Changé en ComboBox<String>

    @FXML
    private CheckBox termsCheckBox;

    @FXML
    private Button registerButton;

    @FXML
    private Button loginButton;

    @FXML
    private Text errorMessage;

    @FXML
    private Text passwordError;

    private UserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("✅ Register controller initialisé");

        // Remplir la ComboBox avec les rôles disponibles (seulement Agriculteur et Client)
        if (roleComboBox != null) {
            roleComboBox.getItems().addAll("Agriculteur", "Client");
            roleComboBox.setValue("Client"); // Valeur par défaut
            System.out.println("✅ Rôles ajoutés à la ComboBox: Agriculteur, Client");
        } else {
            System.err.println("❌ roleComboBox est null - vérifiez le fx:id dans le FXML");
        }

        // Désactiver le bouton d'inscription tant que les conditions ne sont pas acceptées
        if (registerButton != null) {
            registerButton.setDisable(true);
        }

        if (termsCheckBox != null) {
            termsCheckBox.selectedProperty().addListener((obs, old, newValue) -> {
                if (registerButton != null) {
                    registerButton.setDisable(!newValue);
                }
            });
        }

        // Initialiser les messages d'erreur
        if (errorMessage != null) {
            errorMessage.setVisible(false);
        }
        if (passwordError != null) {
            passwordError.setVisible(false);
        }
    }

    @FXML
    private void handleRegister() {
        if (!validateInputs()) {
            return;
        }

        try {
            String emailText = email.getText().trim();

            // Vérifier si l'email existe déjà
            User existingUser = userService.getUserByEmail(emailText);
            if (existingUser != null) {
                showError("Cet email est déjà utilisé.");
                return;
            }

            // Déterminer le rôle sélectionné
            String selectedRoleText = roleComboBox.getValue();
            Role selectedRole;

            if ("Agriculteur".equals(selectedRoleText)) {
                selectedRole = Role.AGRICULTEUR;
            } else {
                selectedRole = Role.CLIENT; // Client par défaut
            }

            System.out.println("Rôle sélectionné: " + selectedRole);

            // Créer un nouvel utilisateur
            User user = new User();
            user.setNom(nom.getText().trim());
            user.setPrenom(prenom.getText().trim());
            user.setEmail(emailText);
            user.setPassword(passwordField.getText()); // Sera haché dans UserService
            user.setTelephone(telephone.getText().trim().isEmpty() ? null : telephone.getText().trim());
            user.setAdresse(adresse.getText().trim().isEmpty() ? null : adresse.getText().trim());
            user.setVille(ville.getText().trim().isEmpty() ? null : ville.getText().trim());
            user.setRole(selectedRole);
            user.setActif(true);
            user.setDate_creation(Timestamp.from(Instant.now()));
            user.setPhoto_profil(null);

            // Ajouter l'utilisateur à la base de données
            System.out.println("Ajout de l'utilisateur à la BD...");
            userService.addEntity(user);

            System.out.println("✅ Utilisateur ajouté avec succès!");

            showSuccess("Inscription réussie ! Vous pouvez maintenant vous connecter.");

            // Rediriger vers la page de connexion après 2 secondes
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                    javafx.application.Platform.runLater(() -> navigateToLogin());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de l'inscription : " + e.getMessage());
        }
    }

    private boolean validateInputs() {
        // Réinitialiser les messages d'erreur
        if (errorMessage != null) {
            errorMessage.setVisible(false);
        }
        if (passwordError != null) {
            passwordError.setVisible(false);
        }

        // Validation du nom
        if (nom.getText() == null || nom.getText().trim().isEmpty()) {
            showError("Veuillez saisir votre nom.");
            return false;
        }

        // Validation du prénom
        if (prenom.getText() == null || prenom.getText().trim().isEmpty()) {
            showError("Veuillez saisir votre prénom.");
            return false;
        }

        // Validation de l'email
        String emailText = email.getText();
        if (emailText == null || emailText.trim().isEmpty()) {
            showError("Veuillez saisir votre email.");
            return false;
        }
        if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", emailText)) {
            showError("Format d'email invalide.");
            return false;
        }

        // Validation du mot de passe
        String password = passwordField.getText();
        if (password == null || password.isEmpty()) {
            showError("Veuillez saisir un mot de passe.");
            return false;
        }
        if (password.length() < 6) {
            showError("Le mot de passe doit contenir au moins 6 caractères.");
            return false;
        }

        // Validation de la confirmation du mot de passe
        if (!password.equals(confirmPasswordField.getText())) {
            if (passwordError != null) {
                passwordError.setText("Les mots de passe ne correspondent pas.");
                passwordError.setVisible(true);
            }
            return false;
        }

        // Validation du téléphone (optionnel mais doit être valide si fourni)
        String phone = telephone.getText();
        if (phone != null && !phone.trim().isEmpty()) {
            if (!phone.matches("\\d{8}")) {
                showError("Le téléphone doit contenir exactement 8 chiffres.");
                return false;
            }
        }

        // Validation de la ville
        if (ville.getText() == null || ville.getText().trim().isEmpty()) {
            showError("Veuillez saisir votre ville.");
            return false;
        }

        // Validation du rôle
        if (roleComboBox.getValue() == null) {
            showError("Veuillez sélectionner un rôle.");
            return false;
        }

        // Validation des conditions d'utilisation
        if (!termsCheckBox.isSelected()) {
            showError("Vous devez accepter les conditions d'utilisation.");
            return false;
        }

        return true;
    }

    private void navigateToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/Login.fxml"));
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Connexion");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void handleLoginButtonAction(ActionEvent event) {
        navigateToLogin();
    }

    private void showError(String message) {
        if (errorMessage != null) {
            errorMessage.setText(message);
            errorMessage.setStyle("-fx-fill: #ef4444;");
            errorMessage.setVisible(true);
        } else {
            System.err.println("❌ Erreur: " + message);
        }
    }

    private void showSuccess(String message) {
        if (errorMessage != null) {
            errorMessage.setText(message);
            errorMessage.setStyle("-fx-fill: #4caf50; -fx-font-weight: bold;");
            errorMessage.setVisible(true);
        }
    }
}