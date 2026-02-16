package com.example.flahasmarty;

import javafx.scene.control.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class HelloController {
    @FXML private TextField articleName, articleCategory, articleUnit, articleImageUrl;
    @FXML private TextArea articleDescription;
    @FXML private Spinner<Double> articlePrice, articleWeight;
    @FXML private Spinner<Integer> articleStock;
    @FXML private Button addArticleBtn, updateArticleBtn, deleteArticleBtn, clearArticleBtn;

    @FXML private TextField orderReference, orderStatus, orderPaymentMode, orderAddress;
    @FXML private Spinner<Double> orderTotal, orderFees;
    @FXML private DatePicker orderDate;
    @FXML private Button addOrderBtn, updateOrderBtn, deleteOrderBtn, clearOrderBtn;

    @FXML
    public void initialize() {
        initializeArticleSpinners();
        initializeOrderComponents();
        setupEventHandlers();
    }

    @FXML
    private void handleAddArticle() {

        try {
            if (!validateArticleForm()) {
                return;
            }

            String nom = articleName.getText();
            String categorie = articleCategory.getText();
            String description = articleDescription.getText();
            double prix = articlePrice.getValue();
            int stock = articleStock.getValue();
            double poids = articleWeight.getValue();
            String unite = articleUnit.getText();
            String imageUrl = articleImageUrl.getText();

            System.out.println("[v0] Adding article: " + nom);

            // Create Article object
            Article article = new Article(
                    nom,
                    description,
                    categorie,
                    prix,
                    stock
            );
            
            // Set weight, unit, image and default user ID
            article.setPoids(poids);
            article.setUnite(unite);
            article.setImageUrl(imageUrl);
            article.setIdUser(1);  // Default user ID - change as needed

            // Call DAO
            ArticleDAO dao = new ArticleDAO();
            dao.insertArticle(article);

            showAlert("Succès", "Article ajouté avec succès ✅");

            clearArticleFields();

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout ❌");
            System.out.println("[v0] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void clearArticleFields() {
        articleName.clear();
        articleCategory.clear();
        articleDescription.clear();
        articleUnit.clear();
        articleImageUrl.clear();

        articlePrice.getValueFactory().setValue(0.0);
        articleStock.getValueFactory().setValue(0);
        articleWeight.getValueFactory().setValue(0.0);
    }






    private void initializeArticleSpinners() {
        articlePrice.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999, 0, 0.01));
        articleStock.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999999, 0, 1));
        articleWeight.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999, 0, 0.1));
    }

    private void initializeOrderComponents() {
        orderTotal.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999, 0, 0.01));
        orderFees.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999, 0, 0.01));
        orderDate.setValue(java.time.LocalDate.now());
    }

    private void setupEventHandlers() {
        addArticleBtn.setOnAction(e -> handleAddArticle());
        updateArticleBtn.setOnAction(e -> updateArticle());
        deleteArticleBtn.setOnAction(e -> deleteArticle());
        clearArticleBtn.setOnAction(e -> clearArticleForm());

        addOrderBtn.setOnAction(e -> addOrder());
        updateOrderBtn.setOnAction(e -> updateOrder());
        deleteOrderBtn.setOnAction(e -> deleteOrder());
        clearOrderBtn.setOnAction(e -> clearOrderForm());
    }

    private void updateArticle() {
        if (validateArticleForm()) {
            showAlert("Succès", "Article mis à jour");
            clearArticleForm();
        }
    }

    private void deleteArticle() {
        showAlert("Confirmation", "Article supprimé");
        clearArticleForm();
    }

    private void clearArticleForm() {
        articleName.clear();
        articleDescription.clear();
        articleCategory.clear();
        articlePrice.getValueFactory().setValue(0.0);
        articleStock.getValueFactory().setValue(0);
        articleWeight.getValueFactory().setValue(0.0);
        articleUnit.clear();
        articleImageUrl.clear();
    }

    private void addOrder() {
        if (validateOrderForm()) {
            showAlert("Succès", "Commande ajoutée avec succès");
            clearOrderForm();
        }
    }

    private void updateOrder() {
        if (validateOrderForm()) {
            showAlert("Succès", "Commande mise à jour");
            clearOrderForm();
        }
    }

    private void deleteOrder() {
        showAlert("Confirmation", "Commande supprimée");
        clearOrderForm();
    }

    private void clearOrderForm() {
        orderReference.clear();
        orderDate.setValue(java.time.LocalDate.now());
        orderStatus.clear();
        orderPaymentMode.clear();
        orderAddress.clear();
        orderTotal.getValueFactory().setValue(0.0);
        orderFees.getValueFactory().setValue(0.0);
    }

    private boolean validateArticleForm() {
        if (articleName.getText().isEmpty() || articleCategory.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires");
            return false;
        }
        return true;
    }

    private boolean validateOrderForm() {
        if (orderReference.getText().isEmpty() || orderStatus.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires");
            return false;
        }
        return true;
    }


}
