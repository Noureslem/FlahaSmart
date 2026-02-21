package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import models.ChatMessage;
import services.AgriChatbotService;

public class ChatbotController {

    @FXML
    private VBox chatContainer;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField userInput;

    @FXML
    private Label statusLabel;

    private AgriChatbotService chatbotService;

    @FXML
    public void initialize() {
        chatbotService = new AgriChatbotService();

        // Message de bienvenue
        addBotMessage("Bonjour ! 🌾 Je suis AgriBot, votre assistant agricole intelligent propulsé par l'IA Gemini.\n\n" +
                     "Je peux vous aider avec :\n" +
                     "🌱 Cultures et plantations\n" +
                     "💧 Irrigation\n" +
                     "🌤️ Impact météo\n" +
                     "🚜 Équipements\n" +
                     "🌿 Fertilisation\n" +
                     "Et bien plus !\n\n" +
                     "Posez-moi vos questions !");

        // Auto-scroll en bas
        scrollPane.vvalueProperty().bind(chatContainer.heightProperty());
    }

    @FXML
    private void onSendMessage() {
        String message = userInput.getText().trim();
        processMessage(message);
    }

    @FXML
    private void onSuggestionClick(ActionEvent event) {
        Button button = (Button) event.getSource();
        String suggestion = (String) button.getUserData();
        if (suggestion != null && !suggestion.isEmpty()) {
            processMessage(suggestion);
        }
    }

    private void processMessage(String message) {
        if (message != null && !message.isEmpty()) {
            // Afficher message utilisateur
            addUserMessage(message);

            // Effacer le champ
            userInput.clear();

            // Afficher indicateur de typing
            Label typingLabel = new Label("⏳ AgriBot réfléchit...");
            typingLabel.getStyleClass().add("typing-indicator");
            chatContainer.getChildren().add(typingLabel);

            // Obtenir réponse du bot dans un thread séparé
            new Thread(() -> {
                String botResponse = chatbotService.getResponse(message);
                javafx.application.Platform.runLater(() -> {
                    // Retirer l'indicateur de typing
                    chatContainer.getChildren().remove(typingLabel);
                    // Afficher la réponse
                    addBotMessage(botResponse);
                });
            }).start();
        }
    }

    private void addUserMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, false);
        VBox messageBox = createMessageBox(chatMessage);
        messageBox.setAlignment(Pos.CENTER_RIGHT);
        chatContainer.getChildren().add(messageBox);
    }

    private void addBotMessage(String message) {
        ChatMessage chatMessage = new ChatMessage(message, true);
        VBox messageBox = createMessageBox(chatMessage);
        messageBox.setAlignment(Pos.CENTER_LEFT);
        chatContainer.getChildren().add(messageBox);
    }

    private VBox createMessageBox(ChatMessage chatMessage) {
        VBox messageBox = new VBox(5);
        messageBox.setPadding(new Insets(5, 10, 5, 10));

        Label messageLabel = new Label(chatMessage.getMessage());
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(400);
        messageLabel.setPadding(new Insets(10));

        if (chatMessage.isBot()) {
            messageLabel.getStyleClass().add("bot-message");
        } else {
            messageLabel.getStyleClass().add("user-message");
        }

        Label timeLabel = new Label(chatMessage.getFormattedTime());
        timeLabel.getStyleClass().add("message-time");

        messageBox.getChildren().addAll(messageLabel, timeLabel);

        return messageBox;
    }

    @FXML
    private void onClearChat() {
        chatContainer.getChildren().clear();
        initialize(); // Réafficher message de bienvenue
    }
}

