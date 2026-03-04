package controllers.Dashbord;

import entities.User;
import entities.Role;
import services.UserService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardHomeController implements Initializable {

    @FXML
    private Label welcomeText;
    @FXML
    private Label dateText;
    @FXML
    private Label totalUsersText;
    @FXML
    private Label totalAdminsText;
    @FXML
    private Label totalAgriculteursText;
    @FXML
    private Label totalClientsText;
    @FXML
    private PieChart rolePieChart;
    @FXML
    private BarChart<String, Number> activityChart;
    @FXML
    private TableView<User> recentUsersTable;
    @FXML
    private TableColumn<User, Integer> colId;
    @FXML
    private TableColumn<User, String> colNom;
    @FXML
    private TableColumn<User, String> colPrenom;
    @FXML
    private TableColumn<User, String> colEmail;
    @FXML
    private TableColumn<User, Role> colRole;

    private UserService userService = new UserService();
    private User loggedInAdmin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Date du jour
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        dateText.setText("Nous sommes le " + today.format(formatter));

        // Configuration du tableau
        colId.setCellValueFactory(new PropertyValueFactory<>("id_user"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));

        // Charger les données
        loadStatistics();
        loadPieChart();
        loadRecentUsers();
        loadBarChart();
    }

    public void setLoggedInUser(User user) {
        this.loggedInAdmin = user;
        if (user != null && welcomeText != null) {
            welcomeText.setText("Bienvenue, " + user.getPrenom() + " " + user.getNom() + " !");
        }
    }

    private void loadStatistics() {
        List<User> users = userService.getEntities();

        long total = users.size();
        long admins = users.stream().filter(u -> u.getRole() == Role.ADMINISTRATEUR).count();
        long agriculteurs = users.stream().filter(u -> u.getRole() == Role.AGRICULTEUR).count();
        long clients = users.stream().filter(u -> u.getRole() == Role.CLIENT).count();

        totalUsersText.setText(String.valueOf(total));
        totalAdminsText.setText(String.valueOf(admins));
        totalAgriculteursText.setText(String.valueOf(agriculteurs));
        totalClientsText.setText(String.valueOf(clients));
    }

    private void loadPieChart() {
        List<User> users = userService.getEntities();

        long admins = users.stream().filter(u -> u.getRole() == Role.ADMINISTRATEUR).count();
        long agriculteurs = users.stream().filter(u -> u.getRole() == Role.AGRICULTEUR).count();
        long clients = users.stream().filter(u -> u.getRole() == Role.CLIENT).count();

        rolePieChart.getData().clear();
        if (admins > 0) rolePieChart.getData().add(new PieChart.Data("Administrateurs", admins));
        if (agriculteurs > 0) rolePieChart.getData().add(new PieChart.Data("Agriculteurs", agriculteurs));
        if (clients > 0) rolePieChart.getData().add(new PieChart.Data("Clients", clients));
    }

    private void loadBarChart() {
        List<User> users = userService.getEntities();

        long actifs = users.stream().filter(u -> u.getActif() != null && u.getActif()).count();
        long inactifs = users.size() - actifs;

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Utilisateurs");
        series.getData().add(new XYChart.Data<>("Actifs", actifs));
        series.getData().add(new XYChart.Data<>("Inactifs", inactifs));

        activityChart.getData().clear();
        activityChart.getData().add(series);
    }

    private void loadRecentUsers() {
        List<User> recentUsers = userService.getRecentUsers(5);
        recentUsersTable.getItems().setAll(recentUsers);
    }

    @FXML
    private void handleViewAllUsers() {
        // Cette méthode sera appelée depuis le contrôleur parent
        // Elle est gérée par DashboardAdmController
    }

    @FXML
    private void handleViewAdmins() {
        // À implémenter si nécessaire
    }

    @FXML
    private void handleViewAgriculteurs() {
        // À implémenter si nécessaire
    }

    @FXML
    private void handleViewClients() {
        // À implémenter si nécessaire
    }
}