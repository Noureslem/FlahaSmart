package controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Weather;
import services.WeatherService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WeatherWidgetController {

    private static final Logger LOGGER = Logger.getLogger(WeatherWidgetController.class.getName());

    @FXML
    private VBox weatherContainer;

    @FXML
    private TextField cityInput;

    @FXML
    private Label locationLabel;

    @FXML
    private Label temperatureLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label humidityLabel;

    @FXML
    private Label windLabel;

    @FXML
    private Label feelsLikeLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private ImageView weatherIcon;

    @FXML
    private Label loadingLabel;

    private WeatherService weatherService;

    @FXML
    public void initialize() {
        weatherService = new WeatherService();
        loadDefaultWeather();
    }

    private void loadDefaultWeather() {
        loadWeather("Tunis");
    }

    @FXML
    private void onSearchWeather() {
        String city = cityInput.getText();
        if (city != null && !city.trim().isEmpty()) {
            loadWeather(city.trim());
        }
    }

    private void loadWeather(String city) {
        showLoading(true);

        Task<Weather> task = new Task<Weather>() {
            @Override
            protected Weather call() throws Exception {
                return weatherService.getWeather(city);
            }
        };

        task.setOnSucceeded(event -> {
            Weather weather = task.getValue();
            if (weather != null) {
                updateWeatherDisplay(weather);
            } else {
                showError("Impossible de récupérer les données météo");
            }
            showLoading(false);
        });

        task.setOnFailed(event -> {
            showError("Erreur lors du chargement");
            showLoading(false);
            LOGGER.log(Level.SEVERE, "Erreur task météo", task.getException());
        });

        new Thread(task).start();
    }

    private void updateWeatherDisplay(Weather weather) {
        Platform.runLater(() -> {
            locationLabel.setText(weather.getLocation() + ", " + weather.getCountry());
            temperatureLabel.setText(weather.getTemperature() + "°C");
            descriptionLabel.setText(weather.getDescription());

            // Mise à jour des labels avec vérification null
            if (humidityLabel != null) {
                humidityLabel.setText("💧  " + weather.getHumidity() + "%");
            }
            if (windLabel != null) {
                windLabel.setText("💨  " + weather.getWindSpeed() + " km/h");
            }
            if (feelsLikeLabel != null) {
                feelsLikeLabel.setText("🌡  " + weather.getFeelsLike() + "°C");
            }
            if (timeLabel != null) {
                timeLabel.setText("🕐 " + weather.getLocalTime());
            }


        });
    }

    private void showLoading(boolean show) {
        Platform.runLater(() -> {
            if (loadingLabel != null) {
                loadingLabel.setVisible(show);
                loadingLabel.setManaged(show);
            }
        });
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            locationLabel.setText(message);
            temperatureLabel.setText("--");
            descriptionLabel.setText("");
        });
    }
}

