package entities;
import java.time.LocalDateTime;
public class thread {
    private int id_thread;
    private String titre;
    private String contenu;
    private LocalDateTime date_creation;
    private LocalDateTime date_update;
    private int id_user;

    // 🔹 Constructeur vide
    public thread() {
    }

    // 🔹 Constructeur pour insertion (sans id_thread)
    public thread(String titre, String contenu,
                   LocalDateTime date_creation,
                   LocalDateTime date_update,
                   int id_user) {
        this.titre = titre;
        this.contenu = contenu;
        this.date_creation = date_creation;
        this.date_update = date_update;
        this.id_user = id_user;
    }

    // 🔹 Constructeur complet (pour récupération depuis la BD)
    public thread(int id_thread, String titre, String contenu,
                   LocalDateTime date_creation,
                   LocalDateTime date_update,
                   int id_user) {
        this.id_thread = id_thread;
        this.titre = titre;
        this.contenu = contenu;
        this.date_creation = date_creation;
        this.date_update = date_update;
        this.id_user = id_user;
    }

    // 🔹 Getters & Setters

    public int getId_thread() {
        return id_thread;
    }

    public void setId_thread(int id_thread) {
        this.id_thread = id_thread;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public LocalDateTime getDate_creation() {
        return date_creation;
    }

    public void setDate_creation(LocalDateTime date_creation) {
        this.date_creation = date_creation;
    }

    public LocalDateTime getDate_update() {
        return date_update;
    }

    public void setDate_update(LocalDateTime date_update) {
        this.date_update = date_update;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    @Override
    public String toString() {
        return "Threads{" +
                "id_thread=" + id_thread +
                ", titre='" + titre + '\'' +
                ", contenu='" + contenu + '\'' +
                ", date_creation=" + date_creation +
                ", date_update=" + date_update +
                ", id_user=" + id_user +
                '}';
    }
}
