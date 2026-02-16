package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import models.Equipement;
import services.EquipementService;
import services.OperationService;
import models.Operation;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DashboardController {

    private static final Logger LOGGER = Logger.getLogger(DashboardController.class.getName());

    @FXML
    private VBox statsContainer;

    @FXML
    private HBox cardsContainer;

    EquipementService eqService = new EquipementService();
    OperationService opService = new OperationService();

    // 🎨 palette couleurs
    private final String[] colors = {
            "#93441A",
            "#00353F",
            "#7AA95C",
            "#CA3C66",
            "#6A645A"
    };

    @FXML
    public void initialize() {
        chargerStatsTypes();
        chargerCardsOperations();
    }

    private void chargerStatsTypes() {

        try {
            statsContainer.getChildren().clear();

            var list = eqService.afficher();

            if (list.isEmpty()) return;

            Map<String, Long> map = list.stream()
                    .collect(Collectors.groupingBy(
                            Equipement::getNom,
                            Collectors.counting()
                    ));

            int total = list.size();

            int index = 0;

            for (String type : map.keySet()) {

                double percent = (map.get(type) * 100.0) / total;

                HBox row = createStatRow(type, percent, index);
                statsContainer.getChildren().add(row);

                index++;
            }

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement des stats types", e);
        }
    }

    private HBox createStatRow(String type, double percent, int index) {

        Label name = new Label(type);
        name.setPrefWidth(120);

        Label value = new Label(String.format("%.0f%%", percent));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        ProgressBar bar = new ProgressBar(percent / 100);
        bar.setPrefWidth(200);

        // couleur automatique
        String color = colors[index % colors.length];
        bar.setStyle("-fx-accent: " + color + ";");
        value.setStyle("-fx-text-fill: " + color + "; -fx-font-weight: bold;");

        VBox content = new VBox(5, new HBox(name, spacer, value), bar);

        return new HBox(content);
    }

    // ===============================
    // CARDS operations
    // ===============================
    private void chargerCardsOperations() {
        try {
            cardsContainer.getChildren().clear();

            List<Operation> ops = opService.afficher();
            int total = ops.size();
            long enCours = ops.stream().filter(o -> o.getStatut() != null && o.getStatut().equalsIgnoreCase("en cours")).count();
            long termines = ops.stream().filter(o -> o.getStatut() != null && (o.getStatut().equalsIgnoreCase("terminer") || o.getStatut().equalsIgnoreCase("terminé"))).count();

            double pctEnCours = total == 0 ? 0 : (enCours * 100.0) / total;
            double pctTermines = total == 0 ? 0 : (termines * 100.0) / total;

            VBox cardTotal = createCard("Les Opérations", String.valueOf(total), "#3b82f6", "");
            VBox cardEnCours = createCard("En cours", String.valueOf(enCours), "red", "");
            VBox cardTermine = createCard("Terminé", String.valueOf(termines), "#22c55e", "");

            cardsContainer.getChildren().addAll(cardTotal, cardEnCours, cardTermine);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors du chargement des cards operations", e);
        }
    }

    private VBox createCard(String title, String mainValue, String color, String subtitle) {
        Label titleLabel = new Label(title);
        // rendu : titre visible en haut de la card
        titleLabel.getStyleClass().clear();
        titleLabel.getStyleClass().add("card-title");
        // appliquer la couleur au titre pour rendre chaque card distincte
        titleLabel.setStyle("-fx-text-fill: " + color + "; -fx-font-size: 14px; -fx-font-weight: 600;");

        Label valueLabel = new Label(mainValue);
        valueLabel.getStyleClass().add("card-value");

        Label subLabel = new Label(subtitle);
        subLabel.getStyleClass().add("card-subtitle");

        // suppression de la barre de pourcentage : on n'ajoute plus le ProgressBar
        VBox box = new VBox(8, titleLabel, valueLabel, subLabel);
        box.getStyleClass().add("stat-card");
        box.setPrefWidth(200);

        return box;
    }

}
