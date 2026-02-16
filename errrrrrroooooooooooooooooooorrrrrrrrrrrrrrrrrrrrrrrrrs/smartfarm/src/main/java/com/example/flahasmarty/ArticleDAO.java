package com.example.flahasmarty;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class ArticleDAO {

    public void insertArticle(Article article) {
        DBConnection.insertArticle(
                article.getNom(),
                article.getDescription(),
                article.getCategorie(),
                article.getPrix(),
                article.getStock(),
                article.getPoids(),
                article.getUnite(),
                article.getImageUrl(),
                article.getIdUser()
        );
    }

    public void updateArticle(Article article) {
        String sql = "UPDATE articles SET nom=?, description=?, categorie=?, prix=?, stock=?, poids=?, unite=?, image_url=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, article.getNom());
            ps.setString(2, article.getDescription());
            ps.setString(3, article.getCategorie());
            ps.setDouble(4, article.getPrix());
            ps.setInt(5, article.getStock());
            ps.setDouble(6, article.getPoids());
            ps.setString(7, article.getUnite());
            ps.setString(8, article.getImageUrl());
            ps.setInt(9, article.getId());

            ps.executeUpdate();
            System.out.println("✅ Article mise à jour avec succès");

        } catch (Exception e) {
            System.out.println("❌ Erreur mise à jour article");
            e.printStackTrace();
        }
    }

    public void deleteArticle(int id) {
        String sql = "DELETE FROM articles WHERE id_article=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("✅ Article supprimé avec succès. ID: " + id);
            } else {
                System.out.println("⚠ Aucun article trouvé avec ID: " + id);
            }

        } catch (Exception e) {
            System.out.println("❌ Erreur suppression article");
            e.printStackTrace();
        }
    }


    public List<Article> getAllArticles() {
        return DBConnection.getAllArticles();
    }
}
