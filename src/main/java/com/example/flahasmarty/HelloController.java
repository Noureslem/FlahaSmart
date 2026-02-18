package com.example.flahasmarty;
import com.example.flahasmarty.ArticleUpdateDialogSimple;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
<<<<<<< Updated upstream
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.io.IOException;
=======
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
>>>>>>> Stashed changes

public class HelloController {
    @FXML private TextField articleName, articleCategory, articleUnit, articleImageUrl;
    @FXML private TextArea articleDescription;
    @FXML private Spinner<Double> articlePrice, articleWeight;
    @FXML private Spinner<Integer> articleStock;
    @FXML private Button addArticleBtn, updateArticleBtn, clearArticleBtn;

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
    @FXML private TableColumn<Article, LocalDateTime> dateAjoutColumn;
    @FXML private Button refreshArticlesBtn, deleteArticleTableBtn;
    @FXML private Label articleCountLabel;
    @FXML private TextField deleteArticleIdField;

<<<<<<< Updated upstream
=======
    // Articles Sorting and Search components
    @FXML private ComboBox<String> sortArticleCombo;
    @FXML private Button applyArticleSortBtn;
    @FXML private TextField searchArticleField;
    @FXML private Button searchArticleBtn;
    @FXML private Button clearArticleSearchBtn;

    // Orders Table components
    @FXML private TableView<Order> ordersTable;
    @FXML private TableColumn<Order, Integer> orderIdColumn;
    @FXML private TableColumn<Order, String> orderRefColumn, orderStatusColumn, orderPaymentColumn, orderAddressColumn;
    @FXML private TableColumn<Order, LocalDate> orderDateColumn;
    @FXML private TableColumn<Order, Double> orderTotalColumn, orderFeesColumn;
    @FXML private Button refreshOrdersBtn, deleteOrderTableBtn;
    @FXML private Label orderCountLabel;
    @FXML private TextField deleteOrderIdField;

    // NEW: Orders Sorting and Search components
    @FXML private ComboBox<String> sortOrderCombo;
    @FXML private Button applyOrderSortBtn;
    @FXML private TextField searchOrderField;
    @FXML private Button searchOrderBtn;
    @FXML private Button clearOrderSearchBtn;

    // Store lists for client-side operations
    private List<Article> allArticles;
    private List<Order> allOrders;
    private String currentArticleSortOption = "Trier par défaut (ID)";
    private String currentOrderSortOption = "Trier par défaut (ID)";
    private String currentArticleSearchTerm = "";
    private String currentOrderSearchTerm = "";

>>>>>>> Stashed changes
    @FXML
    public void initialize() {
        initializeArticleSpinners();
        initializeOrderComponents();
        setupEventHandlers();
        initializeArticlesTable();
<<<<<<< Updated upstream
        loadArticles();
=======
        initializeOrdersTable();
        loadArticles();
        loadOrders();
>>>>>>> Stashed changes
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
        clearArticleBtn.setOnAction(e -> clearArticleForm());

        addOrderBtn.setOnAction(e -> addOrder());
        updateOrderBtn.setOnAction(e -> updateOrder());
        deleteOrderBtn.setOnAction(e -> deleteOrder());
        clearOrderBtn.setOnAction(e -> clearOrderForm());

        // Add delete button handler
        deleteArticleTableBtn.setOnAction(e -> handleDeleteArticleFromTable());
    }

    // UPDATED: updateArticle method with popup dialog
    private void updateArticle() {
        try {
<<<<<<< Updated upstream
            System.out.println("[DEBUG] Opening update dialog...");

            // Use the CORRECT class name - ArticleUpdateDialogSimple
            ArticleUpdateDialogSimple dialog = new ArticleUpdateDialogSimple(
                    updateArticleBtn.getScene().getWindow()
            );

            dialog.showAndWait().ifPresent(updatedArticle -> {
                System.out.println("[DEBUG] Update confirmed for article ID: " + updatedArticle.getId());

                // Perform the update in database
                ArticleDAO dao = new ArticleDAO();
                dao.updateArticle(updatedArticle);

                // Show success message
                showAlert("Succès", "Article mis à jour avec succès ✅");

                // Refresh the table
                loadArticles();

                // Clear the form
                clearArticleForm();
            });

            System.out.println("[DEBUG] Dialog closed");

=======
            showAlert("Info", "Fonctionnalité de modification à implémenter");
>>>>>>> Stashed changes
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to update article:");
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la mise à jour: " + e.getMessage());
        }
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

    // ===== Articles Table Methods =====

    private void initializeArticlesTable() {
        // Setup table columns with cell value factories
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

        // Setup refresh button
        refreshArticlesBtn.setOnAction(e -> loadArticles());
    }

    private void loadArticles() {
        try {
            System.out.println("[v0] Loading articles from database...");

            ArticleDAO dao = new ArticleDAO();
            java.util.List<Article> articlesList = dao.getAllArticles();

            ObservableList<Article> articles = FXCollections.observableArrayList(articlesList);
            articlesTable.setItems(articles);

            // Update article count label
            articleCountLabel.setText("Total: " + articles.size() + " articles");

            System.out.println("[v0] ✅ Articles loaded successfully. Count: " + articles.size());

        } catch (Exception e) {
            System.out.println("[v0] ❌ Error loading articles: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du chargement des articles");
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

            int id;
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException e) {
                showAlert("Erreur", "L'ID doit être un nombre entier");
                return;
            }

            // Confirm deletion
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmation");
            confirmAlert.setHeaderText("Supprimer l'article");
            confirmAlert.setContentText("Êtes-vous sûr de vouloir supprimer cet article (ID: " + id + ")?");

            if (confirmAlert.showAndWait().get() != javafx.scene.control.ButtonType.OK) {
                return;
            }

            // Delete article
            ArticleDAO dao = new ArticleDAO();
            dao.deleteArticle(id);

            System.out.println("[v0] ✅ Article deleted successfully. ID: " + id);

            // Clear input field
            deleteArticleIdField.clear();

            // Reload the table
            loadArticles();

            showAlert("Succès", "Article supprimé avec succès");

        } catch (Exception e) {
            System.out.println("[v0] ❌ Error deleting article: " + e.getMessage());
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la suppression de l'article");
        }
    }
<<<<<<< Updated upstream
=======

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

        // Initialize date column
        dateAjoutColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDateAjout()));

        // Format date display
        dateAjoutColumn.setCellFactory(column -> new TableCell<Article, LocalDateTime>() {
            @Override
            protected void updateItem(LocalDateTime date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
                }
            }
        });

        refreshArticlesBtn.setOnAction(e -> loadArticles());

        // Initialize sort combo box for articles
        sortArticleCombo.getItems().addAll(
                "Trier par défaut (ID)",
                "Prix (croissant)",
                "Prix (décroissant)",
                "Date (plus récent)",
                "Date (plus ancien)",
                "Nom (A-Z)",
                "Nom (Z-A)"
        );
        sortArticleCombo.setValue("Trier par défaut (ID)");

        // Set up search field to trigger search on Enter key
        searchArticleField.setOnAction(e -> searchArticles());
    }

    // Article search methods
    @FXML
    private void searchArticles() {
        currentArticleSearchTerm = searchArticleField.getText().trim();

        if (currentArticleSearchTerm.isEmpty()) {
            loadArticles();
        } else {
            try {
                ArticleDAO dao = new ArticleDAO();
                List<Article> searchResults = dao.searchArticlesByName(currentArticleSearchTerm);
                allArticles = searchResults;
                applyArticleSorting(searchResults);
                articleCountLabel.setText("Résultats: " + searchResults.size() + " articles pour \"" + currentArticleSearchTerm + "\"");
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de la recherche");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void clearArticleSearch() {
        searchArticleField.clear();
        currentArticleSearchTerm = "";
        loadArticles();
    }

    private void loadArticles() {
        try {
            ArticleDAO dao = new ArticleDAO();
            allArticles = dao.getAllArticles();
            applyArticleSorting(allArticles);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement des articles");
            e.printStackTrace();
        }
    }

    @FXML
    private void sortArticles() {
        currentArticleSortOption = sortArticleCombo.getValue();
        if (allArticles != null) {
            applyArticleSorting(allArticles);
        }
    }

    private void applyArticleSorting(List<Article> articlesToSort) {
        List<Article> sortedList = articlesToSort;

        switch (currentArticleSortOption) {
            case "Prix (croissant)":
                sortedList.sort((a1, a2) -> Double.compare(a1.getPrix(), a2.getPrix()));
                break;
            case "Prix (décroissant)":
                sortedList.sort((a1, a2) -> Double.compare(a2.getPrix(), a1.getPrix()));
                break;
            case "Date (plus récent)":
                sortedList.sort((a1, a2) -> {
                    if (a1.getDateAjout() == null && a2.getDateAjout() == null) return 0;
                    if (a1.getDateAjout() == null) return 1;
                    if (a2.getDateAjout() == null) return -1;
                    return a2.getDateAjout().compareTo(a1.getDateAjout());
                });
                break;
            case "Date (plus ancien)":
                sortedList.sort((a1, a2) -> {
                    if (a1.getDateAjout() == null && a2.getDateAjout() == null) return 0;
                    if (a1.getDateAjout() == null) return 1;
                    if (a2.getDateAjout() == null) return -1;
                    return a1.getDateAjout().compareTo(a2.getDateAjout());
                });
                break;
            case "Nom (A-Z)":
                sortedList.sort((a1, a2) -> a1.getNom().compareToIgnoreCase(a2.getNom()));
                break;
            case "Nom (Z-A)":
                sortedList.sort((a1, a2) -> a2.getNom().compareToIgnoreCase(a1.getNom()));
                break;
            default:
                sortedList.sort((a1, a2) -> Integer.compare(a1.getId(), a2.getId()));
                break;
        }

        ObservableList<Article> articles = FXCollections.observableArrayList(sortedList);
        articlesTable.setItems(articles);

        if (!currentArticleSearchTerm.isEmpty()) {
            articleCountLabel.setText("Résultats: " + sortedList.size() + " articles pour \"" + currentArticleSearchTerm + "\"");
        } else {
            articleCountLabel.setText("Total: " + sortedList.size() + " articles");
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
            showAlert("Info", "Fonctionnalité de modification à implémenter");
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

    private void initializeOrdersTable() {
        orderIdColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        orderRefColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getReference()));
        orderDateColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDateCommande()));

        // Format date display
        orderDateColumn.setCellFactory(column -> new TableCell<Order, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                } else {
                    setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });

        orderStatusColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatut()));
        orderPaymentColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getModePaiement()));
        orderAddressColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAdresseLivraison()));
        orderTotalColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getMontantTotal()).asObject());

        // Format montant with 2 decimals
        orderTotalColumn.setCellFactory(column -> new TableCell<Order, Double>() {
            @Override
            protected void updateItem(Double montant, boolean empty) {
                super.updateItem(montant, empty);
                if (empty || montant == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", montant));
                }
            }
        });

        orderFeesColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getFraisLivraison()).asObject());

        // Format frais with 2 decimals
        orderFeesColumn.setCellFactory(column -> new TableCell<Order, Double>() {
            @Override
            protected void updateItem(Double frais, boolean empty) {
                super.updateItem(frais, empty);
                if (empty || frais == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f €", frais));
                }
            }
        });

        refreshOrdersBtn.setOnAction(e -> loadOrders());

        // Initialize sort combo box for orders
        sortOrderCombo.getItems().addAll(
                "Trier par défaut (ID)",
                "Date (plus récente)",
                "Date (plus ancienne)",
                "Montant (croissant)",
                "Montant (décroissant)",
                "Référence (A-Z)",
                "Référence (Z-A)"
        );
        sortOrderCombo.setValue("Trier par défaut (ID)");

        // Set up search field to trigger search on Enter key
        searchOrderField.setOnAction(e -> searchOrders());
    }

    // NEW: Order search methods
    @FXML
    private void searchOrders() {
        currentOrderSearchTerm = searchOrderField.getText().trim();

        if (currentOrderSearchTerm.isEmpty()) {
            loadOrders();
        } else {
            try {
                OrderDAO dao = new OrderDAO();
                List<Order> searchResults = dao.searchOrdersByReference(currentOrderSearchTerm);
                allOrders = searchResults;
                applyOrderSorting(searchResults);
                orderCountLabel.setText("Résultats: " + searchResults.size() + " commandes pour \"" + currentOrderSearchTerm + "\"");
            } catch (Exception e) {
                showAlert("Erreur", "Erreur lors de la recherche");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void clearOrderSearch() {
        searchOrderField.clear();
        currentOrderSearchTerm = "";
        loadOrders();
    }

    private void loadOrders() {
        try {
            OrderDAO dao = new OrderDAO();
            allOrders = dao.getAllOrders();
            applyOrderSorting(allOrders);
        } catch (Exception e) {
            showAlert("Erreur", "Erreur lors du chargement des commandes");
            e.printStackTrace();
        }
    }

    @FXML
    private void sortOrders() {
        currentOrderSortOption = sortOrderCombo.getValue();
        if (allOrders != null) {
            applyOrderSorting(allOrders);
        }
    }

    private void applyOrderSorting(List<Order> ordersToSort) {
        List<Order> sortedList = ordersToSort;

        switch (currentOrderSortOption) {
            case "Date (plus récente)":
                sortedList.sort((o1, o2) -> {
                    if (o1.getDateCommande() == null && o2.getDateCommande() == null) return 0;
                    if (o1.getDateCommande() == null) return 1;
                    if (o2.getDateCommande() == null) return -1;
                    return o2.getDateCommande().compareTo(o1.getDateCommande());
                });
                break;
            case "Date (plus ancienne)":
                sortedList.sort((o1, o2) -> {
                    if (o1.getDateCommande() == null && o2.getDateCommande() == null) return 0;
                    if (o1.getDateCommande() == null) return 1;
                    if (o2.getDateCommande() == null) return -1;
                    return o1.getDateCommande().compareTo(o2.getDateCommande());
                });
                break;
            case "Montant (croissant)":
                sortedList.sort((o1, o2) -> Double.compare(o1.getMontantTotal(), o2.getMontantTotal()));
                break;
            case "Montant (décroissant)":
                sortedList.sort((o1, o2) -> Double.compare(o2.getMontantTotal(), o1.getMontantTotal()));
                break;
            case "Référence (A-Z)":
                sortedList.sort((o1, o2) -> o1.getReference().compareToIgnoreCase(o2.getReference()));
                break;
            case "Référence (Z-A)":
                sortedList.sort((o1, o2) -> o2.getReference().compareToIgnoreCase(o1.getReference()));
                break;
            default:
                sortedList.sort((o1, o2) -> Integer.compare(o1.getId(), o2.getId()));
                break;
        }

        ObservableList<Order> orders = FXCollections.observableArrayList(sortedList);
        ordersTable.setItems(orders);

        if (!currentOrderSearchTerm.isEmpty()) {
            orderCountLabel.setText("Résultats: " + sortedList.size() + " commandes pour \"" + currentOrderSearchTerm + "\"");
        } else {
            orderCountLabel.setText("Total: " + sortedList.size() + " commandes");
        }
    }

    private void setupEventHandlers() {
        // Articles
        addArticleBtn.setOnAction(e -> handleAddArticle());
        updateArticleBtn.setOnAction(e -> updateArticle());
        clearArticleBtn.setOnAction(e -> clearArticleForm());
        deleteArticleTableBtn.setOnAction(e -> handleDeleteArticleFromTable());

        // Articles Search and Sort
        searchArticleBtn.setOnAction(e -> searchArticles());
        clearArticleSearchBtn.setOnAction(e -> clearArticleSearch());
        applyArticleSortBtn.setOnAction(e -> sortArticles());

        // Orders
        addOrderBtn.setOnAction(e -> handleAddOrder());
        updateOrderBtn.setOnAction(e -> updateOrder());
        deleteOrderBtn.setOnAction(e -> handleDeleteOrderFromTable());
        clearOrderBtn.setOnAction(e -> clearOrderForm());

        // Orders Search and Sort
        searchOrderBtn.setOnAction(e -> searchOrders());
        clearOrderSearchBtn.setOnAction(e -> clearOrderSearch());
        applyOrderSortBtn.setOnAction(e -> sortOrders());
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
>>>>>>> Stashed changes
}