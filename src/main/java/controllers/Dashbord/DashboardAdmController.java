package controllers.Dashbord;

import controllers.Admin.ListUser;
import entities.User;
import entities.Role;
import services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.chart.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardAdmController implements Initializable {

    @FXML
    private StackPane contentPane;
    @FXML
    private ScrollPane contentScrollPane;
    @FXML
    private Label adminNameLabel;
    @FXML
    private Label currentDateLabel;
    @FXML
    private Label currentViewLabel;
    @FXML
    private Button logoutButton;
    @FXML
    private Button dashboardBtn;
    @FXML
    private Button usersBtn;
    @FXML
    private Button addAdminBtn;
    @FXML
    private Button statsBtn;
    @FXML
    private Button biBtn;
    @FXML
    private Button marketplaceBtn;
    @FXML
    private Button forumBtn;
    @FXML
    private Button productsBtn;
    @FXML
    private Button ordersBtn;
    @FXML
    private Button settingsBtn;

    private User loggedInAdmin;
    private UserService userService = new UserService();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("📊 Dashboard Admin initialisé");

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy");
        currentDateLabel.setText(today.format(formatter));

        loadView("/ListUser.fxml");
        updateCurrentView("Liste des utilisateurs");
    }

    public void setLoggedInUser(User user) {
        this.loggedInAdmin = user;
        if (user != null && adminNameLabel != null) {
            adminNameLabel.setText(user.getPrenom() + " " + user.getNom());
        }
    }

    @FXML
    private void handleDashboard(ActionEvent event) {
        loadView("/DashboardHome.fxml");
        updateCurrentView("Tableau de bord");
    }

    @FXML
    private void handleListUsers(ActionEvent event) {
        loadView("/ListUser.fxml");
        updateCurrentView("Liste des utilisateurs");
    }

    @FXML
    private void handleAddAdmin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddAdmin.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Ajouter un administrateur");
            stage.setScene(new Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
            refreshCurrentView();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le formulaire d'ajout");
        }
    }

    @FXML
    private void handleStats(ActionEvent event) {
        loadView("/Statistiques.fxml");
        updateCurrentView("Statistiques");
    }

    @FXML
    private void handleBI(ActionEvent event) {
        showBIView();
        updateCurrentView("Business Intelligence");
    }

    private void showBIView() {
        VBox biView = new VBox(20);
        biView.setPadding(new Insets(20));
        biView.setStyle("-fx-background-color: #f8fafc;");

        // Titre
        Label title = new Label("📊 BUSINESS INTELLIGENCE DASHBOARD");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        // KPIs
        HBox kpiBox = createKPIs();

        // Graphiques
        HBox chartsBox = createCharts();

        // Tableaux
        HBox tablesBox = createTables();

        // Boutons d'export
        HBox exportBox = createExportButtons();

        biView.getChildren().addAll(title, kpiBox, chartsBox, tablesBox, exportBox);

        // ScrollPane pour la vue BI
        ScrollPane scrollPane = new ScrollPane(biView);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        contentPane.getChildren().setAll(scrollPane);
    }

    private HBox createKPIs() {
        HBox box = new HBox(20);
        box.setPadding(new Insets(10, 0, 10, 0));

        List<User> users = userService.getEntities();
        long total = users.size();
        long admins = users.stream().filter(u -> u.getRole() == Role.ADMINISTRATEUR).count();
        long agriculteurs = users.stream().filter(u -> u.getRole() == Role.AGRICULTEUR).count();
        long clients = users.stream().filter(u -> u.getRole() == Role.CLIENT).count();
        long actifs = users.stream().filter(u -> u.getActif() != null && u.getActif()).count();

        box.getChildren().addAll(
                createKPICard("Total", String.valueOf(total), "#4caf50"),
                createKPICard("Admins", String.valueOf(admins), "#ef4444"),
                createKPICard("Agriculteurs", String.valueOf(agriculteurs), "#f59e0b"),
                createKPICard("Clients", String.valueOf(clients), "#3498db"),
                createKPICard("Actifs", String.valueOf(actifs), "#27ae60")
        );

        return box;
    }

    private VBox createKPICard(String label, String value, String color) {
        VBox card = new VBox(5);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15; -fx-min-width: 120; -fx-alignment: center; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 5, 0, 0, 0);");

        Label valueLabel = new Label(value);
        valueLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: " + color + ";");

        Label descLabel = new Label(label);
        descLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        card.getChildren().addAll(valueLabel, descLabel);
        return card;
    }

    private HBox createCharts() {
        HBox box = new HBox(20);

        List<User> users = userService.getEntities();
        long admins = users.stream().filter(u -> u.getRole() == Role.ADMINISTRATEUR).count();
        long agriculteurs = users.stream().filter(u -> u.getRole() == Role.AGRICULTEUR).count();
        long clients = users.stream().filter(u -> u.getRole() == Role.CLIENT).count();

        // PieChart
        VBox pieBox = new VBox(10);
        pieBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 0);");

        Label pieTitle = new Label("Répartition par rôle");
        pieTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        PieChart pieChart = new PieChart();
        pieChart.setPrefSize(300, 250);
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Administrateurs (" + admins + ")", Math.max(admins, 1)),
                new PieChart.Data("Agriculteurs (" + agriculteurs + ")", Math.max(agriculteurs, 1)),
                new PieChart.Data("Clients (" + clients + ")", Math.max(clients, 1))
        );
        pieChart.setData(pieData);
        pieBox.getChildren().addAll(pieTitle, pieChart);

        // BarChart
        VBox barBox = new VBox(10);
        barBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 0);");

        Label barTitle = new Label("Statut des comptes");
        barTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setPrefSize(300, 250);

        long actifs = users.stream().filter(u -> u.getActif() != null && u.getActif()).count();
        long inactifs = users.size() - actifs;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Actifs", actifs));
        series.getData().add(new XYChart.Data<>("Inactifs", inactifs));
        barChart.getData().add(series);
        barBox.getChildren().addAll(barTitle, barChart);

        box.getChildren().addAll(pieBox, barBox);
        return box;
    }

    private HBox createTables() {
        HBox box = new HBox(20);

        List<User> users = userService.getEntities();

        // Tableau des villes
        VBox cityBox = new VBox(10);
        cityBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 0);");

        Label cityTitle = new Label("Top Villes");
        cityTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TableView<Map.Entry<String, Long>> cityTable = new TableView<>();
        cityTable.setPrefHeight(200);
        cityTable.setPrefWidth(250);

        TableColumn<Map.Entry<String, Long>, String> cityCol = new TableColumn<>("Ville");
        cityCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKey()));
        cityCol.setPrefWidth(150);

        TableColumn<Map.Entry<String, Long>, Long> countCol = new TableColumn<>("Nombre");
        countCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleLongProperty(cellData.getValue().getValue()).asObject());
        countCol.setPrefWidth(100);

        cityTable.getColumns().addAll(cityCol, countCol);

        Map<String, Long> cityCount = users.stream()
                .filter(u -> u.getVille() != null && !u.getVille().isEmpty())
                .collect(Collectors.groupingBy(User::getVille, Collectors.counting()));

        ObservableList<Map.Entry<String, Long>> cityData =
                FXCollections.observableArrayList(cityCount.entrySet());
        cityTable.setItems(cityData);

        cityBox.getChildren().addAll(cityTitle, cityTable);

        box.getChildren().addAll(cityBox);
        return box;
    }

    private HBox createExportButtons() {
        HBox box = new HBox(15);
        box.setPadding(new Insets(10, 0, 0, 0));
        box.setAlignment(javafx.geometry.Pos.CENTER);

        Button exportPDF = new Button("📄 PDF");
        exportPDF.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        exportPDF.setOnAction(e -> showAlert("Export PDF", "Génération du PDF..."));

        Button exportExcel = new Button("📊 Excel");
        exportExcel.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        exportExcel.setOnAction(e -> showAlert("Export Excel", "Génération du fichier Excel..."));

        Button exportCSV = new Button("📝 CSV");
        exportCSV.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 10 20; -fx-background-radius: 5; -fx-cursor: hand; -fx-font-weight: bold;");
        exportCSV.setOnAction(e -> showAlert("Export CSV", "Génération du fichier CSV..."));

        box.getChildren().addAll(exportPDF, exportExcel, exportCSV);
        return box;
    }

    @FXML
    private void handleMarketplace(ActionEvent event) {
        showAlert("Marketplace", "Cette fonctionnalité sera bientôt disponible");
    }

    @FXML
    private void handleForum(ActionEvent event) {
        showAlert("Forum", "Cette fonctionnalité sera bientôt disponible");
    }

    @FXML
    private void handleProducts(ActionEvent event) {
        showAlert("Produits", "Cette fonctionnalité sera bientôt disponible");
    }

    @FXML
    private void handleOrders(ActionEvent event) {
        showAlert("Commandes", "Cette fonctionnalité sera bientôt disponible");
    }

    @FXML
    private void handleSettings(ActionEvent event) {
        showAlert("Paramètres", "Cette fonctionnalité sera bientôt disponible");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/LoginAdmin.fxml"));
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("FlahaSmart - Connexion Admin");
            stage.setMaximized(true);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadView(String fxmlFile) {
        try {
            URL fxmlUrl = getClass().getResource(fxmlFile);
            if (fxmlUrl == null) {
                showAlert("Erreur", "Fichier introuvable: " + fxmlFile);
                return;
            }

            FXMLLoader loader = new FXMLLoader(fxmlUrl);
            Parent view = loader.load();

            if (fxmlFile.equals("/ListUser.fxml") && loggedInAdmin != null) {
                Object controller = loader.getController();
                if (controller instanceof ListUser) {
                    ((ListUser) controller).setLoggedInUser(loggedInAdmin);
                }
            }

            contentPane.getChildren().setAll(view);
            if (contentScrollPane != null) {
                contentScrollPane.setVvalue(0);
            }
            System.out.println("✅ Vue chargée: " + fxmlFile);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de charger " + fxmlFile);
        }
    }

    private void refreshCurrentView() {
        if (currentViewLabel != null) {
            String currentView = currentViewLabel.getText();
            if ("Liste des utilisateurs".equals(currentView)) {
                loadView("/ListUser.fxml");
            }
        }
    }

    private void updateCurrentView(String viewName) {
        if (currentViewLabel != null) {
            currentViewLabel.setText(viewName);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}