package Controler;

import entities.thread;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import servise.ServiceThreads;

import java.sql.SQLException;
import java.time.LocalDateTime;

public class AjouterThreadController {

    private final ServiceThreads service = new ServiceThreads();

    public void afficher(Stage owner, ThreadController parent, int userId) {
        Stage stage = new Stage();
        stage.setTitle("Nouveau Thread");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(owner);
        stage.setResizable(false);

        // Header violet
        Rectangle logoRect = new Rectangle(28, 28);
        logoRect.setArcWidth(6); logoRect.setArcHeight(6);
        logoRect.setFill(Color.WHITE);
        Label headerTitle = new Label("Nouveau Thread");
        headerTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: white;");
        HBox header = new HBox(12, logoRect, headerTitle);
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefHeight(60);
        header.setPadding(new Insets(0, 24, 0, 24));
        header.setStyle("-fx-background-color: #6C63FF;");

        // Champs formulaire
        Label lblTitre = new Label("Titre *");
        lblTitre.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        TextField titreField = new TextField();
        titreField.setPromptText("Entrez le titre du thread...");
        titreField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #D1D5DB;" +
                "-fx-border-radius: 8; -fx-background-radius: 8; -fx-font-size: 13px; -fx-padding: 10 14 10 14;");
        VBox vTitre = new VBox(6, lblTitre, titreField);

        Label lblContenu = new Label("Contenu *");
        lblContenu.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        TextArea contenuField = new TextArea();
        contenuField.setPromptText("Décrivez votre question ou sujet...");
        contenuField.setPrefRowCount(6);
        contenuField.setWrapText(true);
        contenuField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #D1D5DB;" +
                "-fx-border-radius: 8; -fx-background-radius: 8; -fx-font-size: 13px;");
        VBox vContenu = new VBox(6, lblContenu, contenuField);

        Label messageLabel = new Label("");
        messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #EF4444;");
        messageLabel.setWrapText(true);

        // Boutons
        Button btnAnnuler = new Button("Annuler");
        btnAnnuler.setStyle("-fx-background-color: #F3F4F6; -fx-text-fill: #374151; -fx-font-size: 13px;" +
                "-fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 24 10 24; -fx-cursor: hand;");
        btnAnnuler.setOnAction(e -> stage.close());

        Button btnAjouter = new Button("＋  Publier");
        btnAjouter.setStyle("-fx-background-color: #6C63FF; -fx-text-fill: white; -fx-font-size: 13px;" +
                "-fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 24 10 24; -fx-cursor: hand;");
        btnAjouter.setOnAction(e -> {
            String titre   = titreField.getText().trim();
            String contenu = contenuField.getText().trim();
            if (titre.isEmpty()) {
                messageLabel.setText("⚠  Le titre est obligatoire !");
                return;
            }
            if (contenu.isEmpty()) {
                messageLabel.setText("⚠  Le contenu est obligatoire !");
                return;
            }
            LocalDateTime now = LocalDateTime.now();
            thread t = new thread(titre, contenu, now, now, userId);
            try {
                service.ajouter(t);
                parent.chargerThreads();
                parent.showMessage("✓  Thread publié avec succès !", "#059669");
                stage.close();
            } catch (IllegalArgumentException ex) {
                messageLabel.setText("⚠  " + ex.getMessage());
            } catch (SQLException ex) {
                messageLabel.setText("⚠  Erreur BD : " + ex.getMessage());
            }
        });

        HBox buttons = new HBox(12, btnAnnuler, btnAjouter);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        VBox content = new VBox(20, vTitre, vContenu, messageLabel, buttons);
        content.setPadding(new Insets(32));
        content.setStyle("-fx-background-color: white;");

        BorderPane root = new BorderPane();
        root.setTop(header);
        root.setCenter(content);

        stage.setScene(new Scene(root, 600, 500));
        stage.showAndWait();
    }
}