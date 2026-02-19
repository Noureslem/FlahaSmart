package Controler;

import entities.thread;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import servise.ServiceThreads;

import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ThreadController {

    private final ServiceThreads service = new ServiceThreads();
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final int CURRENT_USER_ID = 5;

    private VBox threadsContainer;
    private Label totalLabel;
    private Label messageLabel;
    private TextField searchField;
    private List<thread> allThreads;
    private Stage primaryStage;

    public void afficher(Stage stage) {
        this.primaryStage = stage;

        // Construire threadsContainer en Java directement
        threadsContainer = new VBox(12);
        threadsContainer.setPadding(new Insets(24));

        searchField = new TextField();
        searchField.setPromptText("🔍  Rechercher un thread...");
        searchField.setPrefWidth(320);
        searchField.setStyle("-fx-background-color: #F9FAFB; -fx-border-color: #E5E7EB;" +
                "-fx-border-radius: 8; -fx-background-radius: 8; -fx-font-size: 13px; -fx-padding: 8 12 8 12;");

        messageLabel = new Label("");
        messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #059669;");

        totalLabel = new Label("0");
        totalLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #6C63FF;");

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #F0F0FA;");
        root.setTop(buildNavBar());
        root.setCenter(buildCenter());

        stage.setTitle("FlahaSmart — Forum");
        stage.setScene(new Scene(root, 1200, 759));
        stage.show();

        chargerThreads();
    }

    // =========================================================
    //  NAVBAR
    // =========================================================
    private HBox buildNavBar() {
        HBox nav = new HBox();
        nav.setAlignment(Pos.CENTER_LEFT);
        nav.setPrefHeight(64);
        nav.setPadding(new Insets(0, 24, 0, 24));
        nav.setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; -fx-border-width: 0 0 1 0;");

        Rectangle logo = new Rectangle(32, 32);
        logo.setArcWidth(8); logo.setArcHeight(8);
        logo.setFill(Color.web("#6C63FF"));

        Label appName = new Label("FlahaSmart");
        appName.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #111827;");
        Label appSub = new Label("");
        appSub.setStyle("-fx-font-size: 11px; -fx-text-fill: #6B7280;");
        VBox logoText = new VBox(2, appName, appSub);

        HBox logoBox = new HBox(8, logo, logoText);
        logoBox.setAlignment(Pos.CENTER_LEFT);
        logoBox.setPrefWidth(200);

        HBox links = new HBox(32,
                mkNavLabel("Grades", false),
                mkNavLabel("Schedule", false),
                mkNavLabel("Community", true),
                mkNavLabel("Payments", false));
        links.setAlignment(Pos.CENTER);

        Region sp1 = new Region(); HBox.setHgrow(sp1, Priority.ALWAYS);
        Region sp2 = new Region(); HBox.setHgrow(sp2, Priority.ALWAYS);

        Circle av1 = new Circle(16); av1.setFill(Color.web("#E0E7FF"));
        Circle av2 = new Circle(16); av2.setFill(Color.web("#E5E7EB"));
        HBox avatars = new HBox(16, av1, av2);
        avatars.setAlignment(Pos.CENTER);

        nav.getChildren().addAll(logoBox, sp1, links, sp2, avatars);
        return nav;
    }

    private Label mkNavLabel(String text, boolean active) {
        Label l = new Label(text);
        l.setStyle(active
                ? "-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #111827;"
                : "-fx-font-size: 13px; -fx-text-fill: #6B7280;");
        return l;
    }

    // =========================================================
    //  CENTER
    // =========================================================
    private HBox buildCenter() {
        HBox center = new HBox();
        center.setPrefHeight(695);
        center.getChildren().addAll(buildSidebarLeft(), buildMain(), buildSidebarRight());
        return center;
    }

    // =========================================================
    //  SIDEBAR GAUCHE
    // =========================================================
    private VBox buildSidebarLeft() {
        VBox sidebar = new VBox(4);
        sidebar.setPrefWidth(220);
        sidebar.setPrefHeight(695);
        sidebar.setPadding(new Insets(24, 16, 24, 16));
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; -fx-border-width: 0 1 0 0;");

        sidebar.getChildren().addAll(
                mkSidebarItem("🏠  Home", false),
                mkSidebarItem("💬  My Threads", true),
                mkSidebarItem("🔖  Saved", false)
        );

        Label courseTitle = new Label("CURRENT COURSE");
        courseTitle.setStyle("-fx-font-size: 11px; -fx-text-fill: #9CA3AF; -fx-font-weight: bold;");
        VBox.setMargin(courseTitle, new Insets(20, 0, 8, 0));

        Circle dot = new Circle(5); dot.setFill(Color.web("#6C63FF"));
        Label cName = new Label("Accounting");
        cName.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        Label cCode = new Label("Rz160015");
        cCode.setStyle("-fx-font-size: 10px; -fx-text-fill: #9CA3AF;");
        VBox cText = new VBox(2, cName, cCode);
        HBox courseItem = new HBox(8, dot, cText);
        courseItem.setAlignment(Pos.CENTER_LEFT);
        courseItem.setPadding(new Insets(8, 12, 8, 12));
        courseItem.setStyle("-fx-background-color: #F3F4F6; -fx-background-radius: 8;");

        Button btnNew = new Button("＋  New Thread");
        btnNew.setMaxWidth(Double.MAX_VALUE);
        btnNew.setStyle("-fx-background-color: #6C63FF; -fx-text-fill: white; -fx-font-size: 13px;" +
                "-fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 12; -fx-cursor: hand;");
        btnNew.setOnAction(e -> ouvrirAjouter());
        VBox.setMargin(btnNew, new Insets(24, 0, 0, 0));

        Label statsTxt = new Label("Total threads :");
        statsTxt.setStyle("-fx-font-size: 12px; -fx-text-fill: #6B7280;");
        HBox statsRow = new HBox(8, statsTxt, totalLabel);
        statsRow.setAlignment(Pos.CENTER_LEFT);
        Label statsTitle = new Label("STATS");
        statsTitle.setStyle("-fx-font-size: 11px; -fx-text-fill: #9CA3AF; -fx-font-weight: bold;");
        VBox statsBox = new VBox(8, statsTitle, statsRow);
        statsBox.setStyle("-fx-background-color: #F9FAFB; -fx-background-radius: 10; -fx-padding: 12;");
        VBox.setMargin(statsBox, new Insets(24, 0, 0, 0));

        sidebar.getChildren().addAll(courseTitle, courseItem, btnNew, statsBox);
        return sidebar;
    }

    private HBox mkSidebarItem(String text, boolean active) {
        Label l = new Label(text);
        l.setStyle(active
                ? "-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #6C63FF;"
                : "-fx-font-size: 13px; -fx-text-fill: #6B7280;");
        HBox box = new HBox(l);
        box.setPadding(new Insets(8, 12, 8, 12));
        if (active) box.setStyle("-fx-background-color: #EDE9FE; -fx-background-radius: 8;");
        return box;
    }

    // =========================================================
    //  ZONE PRINCIPALE
    // =========================================================
    private VBox buildMain() {
        VBox main = new VBox();
        HBox.setHgrow(main, Priority.ALWAYS);
        main.setPrefHeight(695);

        Button btnSearch = new Button("Rechercher");
        btnSearch.setStyle("-fx-background-color: #6C63FF; -fx-text-fill: white; -fx-font-size: 12px;" +
                "-fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        btnSearch.setOnAction(e -> rechercherThreads());

        Button btnRefresh = new Button("↺  Actualiser");
        btnRefresh.setStyle("-fx-background-color: #F3F4F6; -fx-text-fill: #374151; -fx-font-size: 12px;" +
                "-fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        btnRefresh.setOnAction(e -> rafraichir());

        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);

        HBox searchBar = new HBox(12, searchField, btnSearch, btnRefresh, sp, messageLabel);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchBar.setPadding(new Insets(16, 24, 16, 24));
        searchBar.setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; -fx-border-width: 0 0 1 0;");

        ScrollPane scroll = new ScrollPane(threadsContainer);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent; -fx-border-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);

        main.getChildren().addAll(searchBar, scroll);
        return main;
    }

    // =========================================================
    //  SIDEBAR DROITE
    // =========================================================
    private VBox buildSidebarRight() {
        VBox sidebar = new VBox(8);
        sidebar.setPrefWidth(240);
        sidebar.setPrefHeight(695);
        sidebar.setPadding(new Insets(24, 16, 24, 16));
        sidebar.setStyle("-fx-background-color: white; -fx-border-color: #E5E7EB; -fx-border-width: 0 0 0 1;");

        Label title = new Label("Accounting");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        Label group = new Label("Group: Rz160015");
        group.setStyle("-fx-font-size: 11px; -fx-text-fill: #9CA3AF;");
        Rectangle img = new Rectangle(200, 100);
        img.setArcWidth(12); img.setArcHeight(12);
        img.setFill(Color.web("#D1D5DB"));
        VBox.setMargin(img, new Insets(16, 0, 0, 0));
        Label prof = new Label("Dr Ronald Jackson");
        prof.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        VBox.setMargin(prof, new Insets(12, 0, 0, 0));
        Label badge = new Label("Main Lecturer");
        badge.setStyle("-fx-background-color: #ECFDF5; -fx-text-fill: #059669;" +
                "-fx-background-radius: 20; -fx-font-size: 11px; -fx-padding: 4 12 4 12;");

        sidebar.getChildren().addAll(title, group, img, prof, badge);
        return sidebar;
    }

    // =========================================================
    //  CHARGEMENT THREADS
    // =========================================================
    public void chargerThreads() {
        try {
            allThreads = service.recuperer();
            afficherThreads(allThreads);
            totalLabel.setText(String.valueOf(allThreads.size()));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger : " + e.getMessage());
        }
    }

    private void afficherThreads(List<thread> threads) {
        threadsContainer.getChildren().clear();
        if (threads == null || threads.isEmpty()) {
            VBox empty = new VBox(16);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(80));
            Label icon = new Label("💬");
            icon.setStyle("-fx-font-size: 48px;");
            Label msg1 = new Label("Aucun thread pour l'instant");
            msg1.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #374151;");
            Label msg2 = new Label("Cliquez sur '+ New Thread' pour créer votre premier thread");
            msg2.setStyle("-fx-font-size: 13px; -fx-text-fill: #9CA3AF;");
            Button btn = new Button("＋  Créer un thread");
            btn.setStyle("-fx-background-color: #6C63FF; -fx-text-fill: white; -fx-font-size: 13px;" +
                    "-fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 12 24 12 24; -fx-cursor: hand;");
            btn.setOnAction(e -> ouvrirAjouter());
            empty.getChildren().addAll(icon, msg1, msg2, btn);
            threadsContainer.getChildren().add(empty);
            return;
        }
        for (thread t : threads) {
            threadsContainer.getChildren().add(creerCarteThread(t));
        }
    }

    // =========================================================
    //  CARTE THREAD
    // =========================================================
    private VBox creerCarteThread(thread t) {
        VBox carte = new VBox(10);
        carte.setStyle("-fx-background-color: white; -fx-background-radius: 14;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.07), 10, 0, 0, 3); -fx-padding: 18 20 18 20;");

        Circle avatar = new Circle(20);
        avatar.setFill(Color.web("#EDE9FE"));
        avatar.setStroke(Color.web("#6C63FF"));
        avatar.setStrokeWidth(2);

        Label titreLabel = new Label(t.getTitre());
        titreLabel.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #111827;");
        titreLabel.setWrapText(true);

        Label dateLabel = new Label("Créé le " + t.getDate_creation().format(DATE_FMT)
                + "  •  Modifié le " + t.getDate_update().format(DATE_FMT));
        dateLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #9CA3AF;");

        VBox titreBox = new VBox(3, titreLabel, dateLabel);
        HBox.setHgrow(titreBox, Priority.ALWAYS);

        Label badgeUser = new Label("User #" + t.getId_user());
        badgeUser.setStyle("-fx-background-color: #E0E7FF; -fx-text-fill: #4338CA;" +
                "-fx-background-radius: 20; -fx-font-size: 10px; -fx-padding: 3 10 3 10;");

        HBox header = new HBox(12, avatar, titreBox, badgeUser);
        header.setAlignment(Pos.CENTER_LEFT);

        Label contenuLabel = new Label(t.getContenu());
        contenuLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #4B5563;");
        contenuLabel.setWrapText(true);
        contenuLabel.setMaxWidth(Double.MAX_VALUE);

        Region sep = new Region();
        sep.setStyle("-fx-background-color: #F3F4F6; -fx-min-height: 1; -fx-max-height: 1;");

        Label idLabel = new Label("Thread #" + t.getId_thread());
        idLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #D1D5DB;");

        Button btnModifier = new Button("✎  Modifier");
        btnModifier.setStyle("-fx-background-color: #FEF3C7; -fx-text-fill: #92400E;" +
                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 7 16 7 16; -fx-cursor: hand;");
        btnModifier.setOnAction(e -> ouvrirModifier(t));

        Button btnSupprimer = new Button("✕  Supprimer");
        btnSupprimer.setStyle("-fx-background-color: #FEE2E2; -fx-text-fill: #991B1B;" +
                "-fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 7 16 7 16; -fx-cursor: hand;");
        btnSupprimer.setOnAction(e -> supprimerThread(t));

        Region spacer = new Region(); HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox actions = new HBox(10, idLabel, spacer, btnModifier, btnSupprimer);
        actions.setAlignment(Pos.CENTER_RIGHT);

        carte.getChildren().addAll(header, contenuLabel, sep, actions);
        return carte;
    }

    // =========================================================
    //  RECHERCHE
    // =========================================================
    private void rechercherThreads() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) { afficherThreads(allThreads); return; }
        List<thread> filtres = allThreads.stream()
                .filter(t -> t.getTitre().toLowerCase().contains(query)
                        || t.getContenu().toLowerCase().contains(query))
                .collect(Collectors.toList());
        afficherThreads(filtres);
        showMessage(filtres.size() + " résultat(s) trouvé(s)", "#6C63FF");
    }

    private void rafraichir() {
        searchField.clear();
        messageLabel.setText("");
        chargerThreads();
    }

    // =========================================================
    //  SUPPRIMER
    // =========================================================
    private void supprimerThread(thread t) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirmation");
        confirm.setHeaderText("Supprimer le thread ?");
        confirm.setContentText("« " + t.getTitre() + " » sera définitivement supprimé.");
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                service.supprimer(t);
                showMessage("✓  Thread supprimé avec succès", "#059669");
                chargerThreads();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", e.getMessage());
            }
        }
    }

    // =========================================================
    //  OUVRIR FORMULAIRES
    // =========================================================
    public void ouvrirAjouter() {
        new AjouterThreadController().afficher(primaryStage, this, CURRENT_USER_ID);
    }

    private void ouvrirModifier(thread t) {
        new ModifierThreadController().afficher(primaryStage, this, t);
    }

    // =========================================================
    //  UTILITAIRES
    // =========================================================
    public void showMessage(String msg, String color) {
        messageLabel.setText(msg);
        messageLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: " + color + ";");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}