# 🚜 CRUD Operation - Gestion d'Opérations Agricoles

Application JavaFX moderne de gestion d'opérations et d'équipements agricoles avec intégration météo et chatbot intelligent.

---

## 📋 Table des matières

- [Présentation](#-présentation)
- [Fonctionnalités](#-fonctionnalités)
- [Technologies](#-technologies)
- [Installation](#-installation)
- [Utilisation](#-utilisation)
- [Documentation](#-documentation)
- [Captures d'écran](#-captures-décran)
- [Auteur](#-auteur)

---

## 🎯 Présentation

**CRUD Operation** est une application de gestion complète pour les opérations agricoles. Elle permet de :
- Gérer les équipements agricoles (CRUD complet)
- Planifier et suivre les opérations de maintenance
- Consulter la météo en temps réel
- Obtenir des conseils agricoles via un chatbot intelligent

### Points forts :
✅ Interface moderne et intuitive  
✅ Intégration API météo WeatherStack  
✅ Chatbot agricole avec 12 domaines de connaissances  
✅ Architecture MVC propre  
✅ Base de données MySQL  
✅ Design responsive avec CSS moderne  

---

## 🌟 Fonctionnalités

### 1. Gestion des Équipements 🚜

- ✅ **Ajouter** un équipement (nom, type, statut)
- ✅ **Modifier** les informations
- ✅ **Supprimer** un équipement
- ✅ **Afficher** la liste complète
- ✅ **Rechercher** par nom (API Stream)
- ✅ **Trier** alphabétiquement
- ✅ Statuts : **Libre** / **Réservé**

### 2. Gestion des Opérations 🔧

- ✅ **Créer** une opération (type, dates, équipement)
- ✅ **Modifier** une opération existante
- ✅ **Supprimer** une opération
- ✅ **Afficher** toutes les opérations
- ✅ **Rechercher** et **Trier** (API Stream)
- ✅ **Terminer** une opération (bouton dédié)
- ✅ Statuts : **En cours** / **Terminé**
- ✅ Types : LocalDate pour dates début/fin
- ✅ Relation avec modèle Equipement

### 3. Dashboard Moderne 📊

#### Stats Opérations (Cards) :
- 📈 **Total des opérations**
- 🔄 **Opérations en cours** (avec pourcentage)
- ✅ **Opérations terminées** (avec pourcentage)
- 🎯 **Navigation intelligente** : clic sur "En cours" → liste filtrée

#### Stats Équipements :
- 📊 **Répartition par type** avec graphiques
- 🔢 **Nombre total, libres, réservés**
- 🎨 **Barres de progression colorées**

### 4. Widget Météo ☀️

- 🌍 **Météo en temps réel** via API WeatherStack
- 🔍 **Recherche par ville** (monde entier)
- 📊 Affichage :
  - 🌡️ Température actuelle et ressentie
  - 💧 Humidité
  - 💨 Vitesse du vent
  - ☁️ Description météo
  - 🖼️ Icône météo dynamique
  - 🕐 Heure locale
- 🎨 **Design glassmorphism** moderne

### 5. AgriBot - Chatbot Agricole 🤖

#### 12 Domaines de connaissances :
1. 🌾 **Cultures et plantations**
2. 💧 **Irrigation et arrosage**
3. 🌤️ **Météo et impact agricole**
4. 🚜 **Équipements agricoles**
5. 🌿 **Fertilisation et engrais**
6. 🐛 **Maladies et parasites**
7. 🌍 **Sol et compost**
8. 🏠 **Serres et culture protégée**
9. ♻️ **Agriculture biologique**
10. 📱 **Technologies modernes**
11. 🌾 **Récolte et conservation**
12. 💦 **Économie d'eau**

#### Fonctionnalités :
- ✅ Détection intelligente par mots-clés (regex)
- ✅ Réponses variées et pertinentes
- ✅ Interface de chat moderne
- ✅ Historique des conversations
- ✅ Emojis pour convivialité

---

## 💻 Technologies

### Backend :
- ☕ **Java 17**
- 🗄️ **MySQL** (base de données)
- 🔗 **JDBC** (MySQL Connector)
- 📦 **Maven** (gestion dépendances)

### Frontend :
- 🖼️ **JavaFX 21** (interface graphique)
- 🎨 **CSS3** (styles modernes)
- 📄 **FXML** (déclaration interfaces)

### APIs & Services :
- 🌤️ **WeatherStack API** (météo temps réel)
- 🔧 **Gson** (parsing JSON)

### Architecture :
- 🏗️ **MVC** (Model-View-Controller)
- 🔄 **DAO Pattern**
- 🧵 **Multithreading** (Task JavaFX)
- 🌊 **Java Stream API** (recherche/tri)

---

## 📦 Installation

### Prérequis :

- ✅ Java 17 ou supérieur
- ✅ Maven 3.6+
- ✅ MySQL 8.0+
- ✅ IDE (IntelliJ IDEA recommandé)

### Étapes :

1. **Cloner le projet**
   ```bash
   git clone <votre-repo>
   cd CRUD_Operation
   ```

2. **Configurer la base de données**
   ```sql
   CREATE DATABASE crud_operation;
   USE crud_operation;
   
   -- Les tables seront créées automatiquement
   -- ou exécutez le script SQL fourni
   ```

3. **Configurer la connexion**
   Éditer `src/main/java/utilies/MyDataBase.java` :
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/crud_operation";
   private static final String USER = "votre_user";
   private static final String PASSWORD = "votre_password";
   ```

4. **Installer les dépendances**
   ```bash
   mvn clean install
   ```

5. **Compiler le projet**
   ```bash
   mvn compile
   ```

6. **Lancer l'application**
   ```bash
   mvn javafx:run
   ```

---

## 🎮 Utilisation

### Navigation :

L'application utilise une **sidebar** avec les sections suivantes :

```
📊 Dashboard          → Tableau de bord principal
🚜 Équipements        → Gestion des équipements
   ├─ Liste          → Voir tous les équipements
   └─ Ajouter        → Nouvel équipement
🔧 Opérations         → Gestion des opérations
   ├─ Liste          → Voir toutes les opérations
   └─ Ajouter        → Nouvelle opération
```

### Workflow typique :

1. **Ajouter un équipement**
   - Aller dans "Équipements" → "Ajouter"
   - Remplir : nom, type
   - Statut par défaut : "Libre"

2. **Créer une opération**
   - Aller dans "Opérations" → "Ajouter"
   - Sélectionner un équipement (ComboBox)
   - Choisir dates début/fin
   - Type d'opération
   - Statut par défaut : "En cours"

3. **Suivre dans le Dashboard**
   - Voir les statistiques en temps réel
   - Cliquer sur card "En cours" pour voir détails
   - Consulter la météo

4. **Terminer une opération**
   - Liste des opérations → Bouton "Terminer"
   - Statut change automatiquement

5. **Consulter AgriBot**
   - Poser une question agricole
   - Recevoir conseils instantanés

---

## 📚 Documentation

Documentation détaillée disponible :

- 📖 [**CHATBOT_DOCUMENTATION.md**](CHATBOT_DOCUMENTATION.md) - Guide complet du chatbot
- 🚀 [**CHATBOT_QUICKSTART.md**](CHATBOT_QUICKSTART.md) - Démarrage rapide chatbot
- 🌤️ [**WEATHER_INTEGRATION.md**](WEATHER_INTEGRATION.md) - Documentation API météo
- 📘 [**WEATHER_USER_GUIDE.md**](WEATHER_USER_GUIDE.md) - Guide utilisateur météo

---

## 📸 Captures d'écran

### Dashboard
```
┌─────────────────────────────────────────────────────────────┐
│  Tableau de bord                                            │
├───────────────────────────────────┬─────────────────────────┤
│  [Card: Total]  [Card: En cours]  │   ☀️ Widget Météo       │
│  [Card: Terminé]                   │                         │
│                                    │   🤖 AgriBot Chatbot   │
│  📊 Équipements par type           │                         │
│  ████████████ 45%  Tracteur        │                         │
│  ██████ 30%  Irrigation            │                         │
└───────────────────────────────────┴─────────────────────────┘
```

---

## 🏗️ Structure du projet

```
CRUD_Operation/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controllers/        # Contrôleurs JavaFX
│   │   │   │   ├── DashboardController.java
│   │   │   │   ├── ChatbotController.java
│   │   │   │   ├── WeatherWidgetController.java
│   │   │   │   ├── equipement/     # Contrôleurs équipements
│   │   │   │   └── operation/      # Contrôleurs opérations
│   │   │   ├── models/             # Modèles de données
│   │   │   │   ├── Equipement.java
│   │   │   │   ├── Operation.java
│   │   │   │   ├── Weather.java
│   │   │   │   └── ChatMessage.java
│   │   │   ├── services/           # Logique métier
│   │   │   │   ├── EquipementService.java
│   │   │   │   ├── OperationService.java
│   │   │   │   ├── WeatherService.java
│   │   │   │   └── AgriChatbotService.java
│   │   │   ├── utilies/            # Utilitaires
│   │   │   │   └── MyDataBase.java
│   │   │   └── testes/
│   │   │       └── MainApp.java    # Point d'entrée
│   │   └── resources/
│   │       ├── Dashboard.fxml
│   │       ├── MainLayout.fxml
│   │       ├── styles/
│   │       │   └── style.css       # Styles globaux
│   │       └── views/
│   │           ├── equipement/     # Vues équipements
│   │           ├── operation/      # Vues opérations
│   │           ├── WeatherWidget.fxml
│   │           └── Chatbot.fxml
│   └── test/
│       └── java/services/          # Tests unitaires
├── pom.xml                         # Configuration Maven
├── README.md                       # Ce fichier
├── CHATBOT_DOCUMENTATION.md
├── CHATBOT_QUICKSTART.md
├── WEATHER_INTEGRATION.md
└── WEATHER_USER_GUIDE.md
```

---

## 🎓 Concepts utilisés

### Programmation Java :
- ✅ **POO** : Encapsulation, héritage, polymorphisme
- ✅ **Collections** : ArrayList, HashMap
- ✅ **Stream API** : filter, sorted, map, collect
- ✅ **Generics** : List<T>, Task<T>
- ✅ **Lambda expressions**
- ✅ **Pattern Matching** : Regex pour chatbot
- ✅ **Exception handling** : try-catch-finally
- ✅ **JDBC** : PreparedStatement, ResultSet

### JavaFX :
- ✅ **FXML** : Déclaration interfaces
- ✅ **Controllers** : @FXML annotations
- ✅ **Layouts** : VBox, HBox, BorderPane
- ✅ **Controls** : TableView, ComboBox, DatePicker
- ✅ **CSS Styling** : Classes, pseudo-classes
- ✅ **Task & Concurrency** : Background threads
- ✅ **Property Binding** : ScrollPane auto-scroll

### Design Patterns :
- ✅ **MVC** : Séparation responsabilités
- ✅ **DAO** : Data Access Object
- ✅ **Singleton** : Database connection
- ✅ **Service Layer** : Logique métier isolée

---

## 🔒 Sécurité & Bonnes pratiques

- ✅ **PreparedStatement** : Protection contre SQL Injection
- ✅ **Validation** : Champs obligatoires vérifiés
- ✅ **Gestion erreurs** : Try-catch partout
- ✅ **Logs** : Logger pour debugging
- ✅ **Fermeture ressources** : Finally blocks
- ✅ **Séparation concerns** : MVC strict

---

## 🚀 Fonctionnalités avancées

### 1. Recherche et Tri (Stream API)
```java
// Recherche par nom
list.stream()
    .filter(e -> e.getNom().toLowerCase().contains(search))
    .collect(Collectors.toList());

// Tri alphabétique
list.stream()
    .sorted(Comparator.comparing(Equipement::getNom))
    .collect(Collectors.toList());
```

### 2. Navigation intelligente
- Clic sur card "En cours" → Liste opérations filtrées
- Highlight moderne des opérations en cours
- Animation smooth au chargement

### 3. Intégration modèles
- Operation utilise `Equipement equipement` (pas id_equipement)
- Jointure correcte en Java (best practice)
- ComboBox affiche objets Equipement

### 4. API météo asynchrone
```java
Task<Weather> task = new Task<>() {
    protected Weather call() {
        return weatherService.getWeather(city);
    }
};
```

---

## 📊 Base de données

### Tables :

**equipement**
```sql
CREATE TABLE equipement (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nom VARCHAR(100) NOT NULL,
    type VARCHAR(50),
    statut ENUM('Libre', 'Réservé') DEFAULT 'Libre'
);
```

**operation**
```sql
CREATE TABLE operation (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type VARCHAR(100) NOT NULL,
    date_debut DATE NOT NULL,
    date_fin DATE NOT NULL,
    statut ENUM('En cours', 'Terminé') DEFAULT 'En cours',
    id_equipement INT,
    FOREIGN KEY (id_equipement) REFERENCES equipement(id)
);
```

---

## 🐛 Résolution de problèmes

### Erreur de connexion BD :
```
Solution : Vérifier URL, user, password dans MyDataBase.java
```

### JavaFX ne s'affiche pas :
```
Solution : mvn clean compile puis mvn javafx:run
```

### Styles CSS non appliqués :
```
Solution : Vérifier chemin dans FXML : stylesheets="@/styles/style.css"
```

### API météo ne répond pas :
```
Solution : 
1. Vérifier connexion Internet
2. Limite API gratuite atteinte (100 req/mois)
3. Tester avec autre ville
```

---

## 📈 Améliorations futures

### Court terme :
- [ ] Export PDF des opérations
- [ ] Calendrier visuel des opérations
- [ ] Notifications système

### Moyen terme :
- [ ] Multi-utilisateurs avec login
- [ ] Historique complet des modifications
- [ ] Graphiques statistiques avancés

### Long terme :
- [ ] Application mobile (JavaFX Mobile)
- [ ] Synchronisation cloud
- [ ] IA prédictive pour planification

---

## 👨‍💻 Auteur

**Votre Nom**  
🎓 Étudiant en [Votre Formation]  
📧 Email : votre.email@example.com  
🔗 GitHub : [Votre profil]

---

## 📄 Licence

Ce projet est réalisé dans un cadre académique.

---

## 🙏 Remerciements

- **WeatherStack** pour l'API météo
- **OpenJFX** pour JavaFX
- **MySQL** pour la base de données
- **Maven** pour la gestion de projet

---

## 📞 Support

Pour toute question :
1. Consultez la documentation dans `/docs`
2. Vérifiez les logs de l'application
3. Contactez l'auteur

---

*Développé avec ❤️ et ☕ - Février 2026*

**Version** : 1.0  
**Status** : ✅ Stable

