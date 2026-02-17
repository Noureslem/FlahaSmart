package com.example.flahasmarty;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;

public class ArticleUpdateDialogSimple extends Dialog<Article> {

    private TextField searchIdField;
    private TextField nomField;
    private TextField categorieField;
    private TextArea descriptionField;
    private TextField prixField;
    private TextField stockField;
    private TextField poidsField;
    private TextField uniteField;
    private TextField imageUrlField;
    private Label statusLabel;
    private Button searchButton;

    private ArticleDAO articleDAO;
    private Article currentArticle;
    private Button confirmButton;

    public ArticleUpdateDialogSimple(Window owner) {
        try {
            articleDAO = new ArticleDAO();

            // Create dialog
            initOwner(owner);
            initModality(Modality.APPLICATION_MODAL);
            setTitle("Mettre à jour un article");

            // Create content
            DialogPane dialogPane = new DialogPane();
            dialogPane.setContent(createContent());

            // Add buttons
            ButtonType confirmButtonType = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialogPane.getButtonTypes().addAll(confirmButtonType, cancelButtonType);

            setDialogPane(dialogPane);

            // Get confirm button
            confirmButton = (Button) dialogPane.lookupButton(confirmButtonType);
            if (confirmButton != null) {
                confirmButton.setDisable(true);
            }

            // Set result converter
            setResultConverter(buttonType -> {
                if (buttonType == confirmButtonType && validateForm()) {
                    updateArticleFromForm();
                    return currentArticle;
                }
                return null;
            });

        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Impossible de créer la fenêtre: " + e.getMessage());
        }
    }

    private Node createContent() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        // Search section
        Label searchLabel = new Label("🔍 RECHERCHER ARTICLE PAR ID");
        searchLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        Label idLabel = new Label("ID Article:");
        idLabel.setMinWidth(100);

        searchIdField = new TextField();
        searchIdField.setPromptText("Entrez l'ID de l'article");
        searchIdField.setPrefWidth(200);

        searchButton = new Button("Rechercher");
        searchButton.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        searchButton.setOnAction(e -> searchArticle());

        searchBox.getChildren().addAll(idLabel, searchIdField, searchButton);

        Separator separator = new Separator();

        // Form section
        Label formLabel = new Label("📝 MODIFIER LES INFORMATIONS");
        formLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2c3e50;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Row 0: Nom
        grid.add(new Label("Nom:"), 0, 0);
        nomField = new TextField();
        nomField.setPromptText("Nom de l'article");
        grid.add(nomField, 1, 0);

        // Row 1: Catégorie
        grid.add(new Label("Catégorie:"), 0, 1);
        categorieField = new TextField();
        categorieField.setPromptText("Catégorie");
        grid.add(categorieField, 1, 1);

        // Row 2: Description
        grid.add(new Label("Description:"), 0, 2);
        descriptionField = new TextArea();
        descriptionField.setPromptText("Description");
        descriptionField.setPrefRowCount(3);
        descriptionField.setWrapText(true);
        grid.add(descriptionField, 1, 2);

        // Row 3: Prix
        grid.add(new Label("Prix (€):"), 0, 3);
        prixField = new TextField();
        prixField.setPromptText("0.00");
        grid.add(prixField, 1, 3);

        // Row 4: Stock
        grid.add(new Label("Stock:"), 0, 4);
        stockField = new TextField();
        stockField.setPromptText("0");
        grid.add(stockField, 1, 4);

        // Row 5: Poids
        grid.add(new Label("Poids:"), 0, 5);
        HBox poidsBox = new HBox(5);
        poidsField = new TextField();
        poidsField.setPromptText("0.0");
        poidsField.setPrefWidth(150);
        uniteField = new TextField();
        uniteField.setPromptText("kg/g/L");
        uniteField.setPrefWidth(80);
        poidsBox.getChildren().addAll(poidsField, uniteField);
        grid.add(poidsBox, 1, 5);

        // Row 6: Image URL
        grid.add(new Label("Image URL:"), 0, 6);
        imageUrlField = new TextField();
        imageUrlField.setPromptText("URL de l'image");
        grid.add(imageUrlField, 1, 6);

        // Status label
        statusLabel = new Label();
        statusLabel.setWrapText(true);
        statusLabel.setStyle("-fx-text-fill: #e74c3c;");

        // Add number validation
        addNumberValidation();

        content.getChildren().addAll(
                searchLabel, searchBox, separator, formLabel,
                grid, statusLabel
        );

        return content;
    }

    private void searchArticle() {
        String idText = searchIdField.getText().trim();

        if (idText.isEmpty()) {
            showStatus("Veuillez entrer un ID d'article", true);
            return;
        }

        try {
            int id = Integer.parseInt(idText);

            System.out.println("[DEBUG] Searching for article ID: " + id);
            Article found = articleDAO.getArticleById(id);

            if (found != null) {
                currentArticle = found;
                populateForm(found);
                showStatus("Article trouvé ! Vous pouvez maintenant modifier les informations.", false);
                searchIdField.setDisable(true);
                searchButton.setDisable(true);
                confirmButton.setDisable(false);
                System.out.println("[DEBUG] Article found and form populated");
            } else {
                showStatus("Aucun article trouvé avec l'ID: " + id, true);
                clearForm();
            }

        } catch (NumberFormatException e) {
            showStatus("L'ID doit être un nombre entier", true);
        }
    }

    private void populateForm(Article article) {
        nomField.setText(article.getNom());
        categorieField.setText(article.getCategorie());
        descriptionField.setText(article.getDescription());
        prixField.setText(String.valueOf(article.getPrix()));
        stockField.setText(String.valueOf(article.getStock()));
        poidsField.setText(String.valueOf(article.getPoids()));
        uniteField.setText(article.getUnite());
        imageUrlField.setText(article.getImageUrl());
    }

    private void updateArticleFromForm() {
        currentArticle.setNom(nomField.getText().trim());
        currentArticle.setCategorie(categorieField.getText().trim());
        currentArticle.setDescription(descriptionField.getText().trim());
        currentArticle.setPrix(Double.parseDouble(prixField.getText().trim()));
        currentArticle.setStock(Integer.parseInt(stockField.getText().trim()));
        currentArticle.setPoids(Double.parseDouble(poidsField.getText().trim()));
        currentArticle.setUnite(uniteField.getText().trim());
        currentArticle.setImageUrl(imageUrlField.getText().trim());
    }

    private boolean validateForm() {
        if (currentArticle == null) {
            showStatus("Veuillez d'abord rechercher un article", true);
            return false;
        }

        if (nomField.getText().trim().isEmpty()) {
            showStatus("Le nom est obligatoire", true);
            return false;
        }

        if (categorieField.getText().trim().isEmpty()) {
            showStatus("La catégorie est obligatoire", true);
            return false;
        }

        try {
            Double.parseDouble(prixField.getText().trim());
        } catch (NumberFormatException e) {
            showStatus("Le prix doit être un nombre valide", true);
            return false;
        }

        try {
            Integer.parseInt(stockField.getText().trim());
        } catch (NumberFormatException e) {
            showStatus("Le stock doit être un nombre entier", true);
            return false;
        }

        try {
            Double.parseDouble(poidsField.getText().trim());
        } catch (NumberFormatException e) {
            showStatus("Le poids doit être un nombre valide", true);
            return false;
        }

        return true;
    }

    private void clearForm() {
        nomField.clear();
        categorieField.clear();
        descriptionField.clear();
        prixField.clear();
        stockField.clear();
        poidsField.clear();
        uniteField.clear();
        imageUrlField.clear();
    }

    private void showStatus(String message, boolean isError) {
        statusLabel.setText(message);
        statusLabel.setStyle(isError ? "-fx-text-fill: #e74c3c;" : "-fx-text-fill: #27ae60;");
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void addNumberValidation() {
        prixField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                prixField.setText(oldValue);
            }
        });

        stockField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                stockField.setText(oldValue);
            }
        });

        poidsField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                poidsField.setText(oldValue);
            }
        });
    }
}