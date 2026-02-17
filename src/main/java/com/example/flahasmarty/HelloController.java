package com.example.flahasmarty;

import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import java.time.LocalDate;

public class HelloController {
    // Articles components
    @FXML private TextField articleName, articleCategory, articleUnit, articleImageUrl;
    @FXML private TextArea articleDescription;
    @FXML private Spinner<Double> articlePrice, articleWeight;
    @FXML private Spinner<Integer> articleStock;
    @FXML private Button addArticleBtn, updateArticleBtn, clearArticleBtn;

    // Orders components
    @FXML private TextField orderReference, orderStatus, orderPaymentMode, orderAddress;
    @FXML private Spinner<Double> orderTotal, orderFees;
    @FXML private DatePicker orderDate;
    @FXML private Button addOrderBtn, updateOrderBtn, deleteOrderBtn, clearOrderBtn;

    // Articles Table components
    @FXML private TableView<Article> articlesTable;
    @FXML private TableColumn<Article, Integer> idColumn;
    @FXML private TableColumn<Article, String> nomColumn, categorieColumn, descriptionColumn, uniteColumn, imageUrlColumn;
    @FXML private TableColumn<Article, Double> prixColumn, poidsColumn;
    @FXML private TableColumn<Article, Integer> stockColumn;
    @FXML private Button refreshArticlesBtn, deleteArticleTableBtn;
    @FXML private Label articleCountLabel;
    @FXML private TextField deleteArticleIdField;

    // Orders Table components (NEW)
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> orderIdColumn;
    @FXML private TableColumn<Order, String> orderRefColumn, orderStatusColumn, orderPaymentColumn, orderAddressColumn;
    @FXML private TableColumn<Order, LocalDate> orderDateColumn;
    @FXML private TableColumn<Order, Double> orderTotalColumn, orderFeesColumn;
    @FXML private Button refreshOrdersBtn, deleteOrderTableBtn;
    @FXML private Label orderCountLabel;
    @FXML private TextField deleteOrderIdField;

    @FXML
    public void initialize() {
        initializeArticleSpinners();
        initializeOrderComponents();
        setupEventHandlers();
        initializeArticlesTable();
        initializeOrdersTable(); // NEW
        loadArticles();
        loadOrders(); // NEW
    }

    // ===== ARTICLES METHODS =====

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

            Article article = new Article(nom, description, categorie, prix, stock);
            article.setPoids(poids);
            article.setUnite(unite);
            article.setImageUrl(imageUrl);
            article.setIdUser(1);

            ArticleDAO dao = new ArticleDAO();
            dao.insertArticle(article);

            showAlert("Succès", "Article ajouté avec succès ✅");
            clearArticleFields();
            loadArticles();

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout ❌");
            e.printStackTrace();
        }
    }

    private void updateArticle() {
        try {
            ArticleUpdateDialogSimple dialog = new ArticleUpdateDialogSimple(
                    updateArticleBtn.getScene().getWindow()
            );

            dialog.showAndWait().ifPresent(updatedArticle -> {
                ArticleDAO dao = new ArticleDAO();
                dao.updateArticle(updatedArticle);
                showAlert("Succès", "Article mis à jour avec succès ✅");
                loadArticles();
                clearArticleForm();
            });

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la mise à jour ❌");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteArticleFromTable() {
        try {
            String idText = deleteArticleIdField.getText().trim();

            if (idText.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer un ID d'article à supprimer");
                return;
            }

            int id = Integer.parseInt(idText);

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Supprimer l'article");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet article (ID: " + id + ")?");

            if (confirmAlert.showAndWait().get() != ButtonType.OK) {
                return;
            }

            ArticleDAO dao = new ArticleDAO();
            dao.deleteArticle(id);

            deleteArticleIdField.clear();
            loadArticles();
            showAlert("Succès", "Article supprimé avec succès");

        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID doit être un nombre entier");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la suppression");
            e.printStackTrace();
        }
    }

    private void initializeArticleSpinners() {
        articlePrice.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999, 0, 0.01));
        articleStock.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 999999, 0, 1));
        articleWeight.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999, 0, 0.1));
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

    private void clearArticleForm() {
        clearArticleFields();
    }

    private boolean validateArticleForm() {
        if (articleName.getText().isEmpty() || articleCategory.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires");
            return false;
        }
        return true;
    }

    private void initializeArticlesTable() {
        idColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        nomColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom()));
        categorieColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategorie()));
        descriptionColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDescription()));
        prixColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPrix()).asObject());
        stockColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getStock()).asObject());
        poidsColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getPoids()).asObject());
        uniteColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUnite()));
        imageUrlColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getImageUrl()));

        refreshArticlesBtn.setOnAction(e -> loadArticles());
    }

    private void loadArticles() {
        try {
            ArticleDAO dao = new ArticleDAO();
            java.util.List<Article> articlesList = dao.getAllArticles();
            ObservableList<Article> articles = FXCollections.observableArrayList(articlesList);
            articlesTable.setItems(articles);
            articleCountLabel.setText("Total: " + articles.size() + " articles");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement des articles");
            e.printStackTrace();
        }
    }

    // ===== ORDERS METHODS =====

    @FXML
    private void handleAddOrder() {
        try {
            if (!validateOrderForm()) {
                return;
            }

            String reference = orderReference.getText();
            LocalDate date = orderDate.getValue();
            String statut = orderStatus.getText();
            String modePaiement = orderPaymentMode.getText();
            String adresse = orderAddress.getText();
            double montant = orderTotal.getValue();
            double frais = orderFees.getValue();

            Order order = new Order(reference, date, statut, modePaiement, adresse, montant, frais, 1);

            OrderDAO dao = new OrderDAO();
            dao.insertOrder(order);

            showAlert("Succès", "Commande ajoutée avec succès ✅");
            clearOrderForm();
            loadOrders();

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de l'ajout ❌");
            e.printStackTrace();
        }
    }

    private void updateOrder() {
        try {
            OrderUpdateDialog dialog = new OrderUpdateDialog(
                    updateOrderBtn.getScene().getWindow()
            );

            dialog.showAndWait().ifPresent(updatedOrder -> {
                OrderDAO dao = new OrderDAO();
                dao.updateOrder(updatedOrder);
                showAlert("Succès", "Commande mise à jour avec succès ✅");
                loadOrders();
                clearOrderForm();
            });

        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la mise à jour ❌");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteOrderFromTable() {
        try {
            String idText = deleteOrderIdField.getText().trim();

            if (idText.isEmpty()) {
                showAlert("Erreur", "Veuillez entrer un ID de commande à supprimer");
                return;
            }

            int id = Integer.parseInt(idText);

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Supprimer la commande");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cette commande (ID: " + id + ")?");

            if (confirmAlert.showAndWait().get() != ButtonType.OK) {
                return;
            }

            OrderDAO dao = new OrderDAO();
            dao.deleteOrder(id);

            deleteOrderIdField.clear();
            loadOrders();
            showAlert("Succès", "Commande supprimée avec succès");

        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID doit être un nombre entier");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors de la suppression");
            e.printStackTrace();
        }
    }

    private void initializeOrderComponents() {
        orderTotal.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999, 0, 0.01));
        orderFees.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 999999, 0, 0.01));
        orderDate.setValue(LocalDate.now());
    }

    private void clearOrderForm() {
        orderReference.clear();
        orderDate.setValue(LocalDate.now());
        orderStatus.clear();
        orderPaymentMode.clear();
        orderAddress.clear();
        orderTotal.getValueFactory().setValue(0.0);
        orderFees.getValueFactory().setValue(0.0);
    }

    private boolean validateOrderForm() {
        if (orderReference.getText().isEmpty() || orderStatus.getText().isEmpty()) {
            showAlert("Erreur", "Veuillez remplir tous les champs obligatoires");
            return false;
        }
        return true;
    }

    // NEW: Initialize Orders Table
    private void initializeOrdersTable() {
        orderIdColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        orderRefColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getReference()));
        orderDateColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDateCommande()));
        orderStatusColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatut()));
        orderPaymentColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getModePaiement()));
        orderAddressColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAdresseLivraison()));
        orderTotalColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getMontantTotal()).asObject());
        orderFeesColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getFraisLivraison()).asObject());

        refreshOrdersBtn.setOnAction(e -> loadOrders());
    }

    // NEW: Load Orders into Table
    private void loadOrders() {
        try {
            OrderDAO dao = new OrderDAO();
            java.util.List<Order> ordersList = dao.getAllOrders();
            ObservableList<Order> orders = FXCollections.observableArrayList(ordersList);
            ordersTable.setItems(orders);
            orderCountLabel.setText("Total: " + orders.size() + " commandes");
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement des commandes");
            e.printStackTrace();
        }
    }

    private void setupEventHandlers() {
        // Articles
        addArticleBtn.setOnAction(e -> handleAddArticle());
        updateArticleBtn.setOnAction(e -> updateArticle());
        clearArticleBtn.setOnAction(e -> clearArticleForm());
        deleteArticleTableBtn.setOnAction(e -> handleDeleteArticleFromTable());

        // Orders
        addOrderBtn.setOnAction(e -> handleAddOrder());
        updateOrderBtn.setOnAction(e -> updateOrder());
        deleteOrderBtn.setOnAction(e -> handleDeleteOrderFromTable());
        clearOrderBtn.setOnAction(e -> clearOrderForm());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Keep these for backward compatibility
    private void addOrder() { handleAddOrder(); }
    private void deleteOrder() { handleDeleteOrderFromTable(); }
}