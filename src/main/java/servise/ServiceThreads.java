package servise;

import entities.thread;
import utils.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class ServiceThreads implements IService<thread>{
    // bch njib les méthodes ly 3andi
    //9bal kol chy lezmni na3rfou est ce que connecta ll base wela lee
    private Connection connection; // ndeclari l objet mte3i connection 
    public ServiceThreads(){
        connection= MyDataBase.getInstance().getConnection();
    }


    @Override
    public void ajouter(thread thread) throws SQLException {

        validerThread(thread);  // 🔥 CONTROLE ICI

        String sql = "INSERT INTO threads (titre, contenu, date_creation, date_update, id_user) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setString(1, thread.getTitre());   // ✔ correct
        ps.setString(2, thread.getContenu());
        ps.setTimestamp(3, Timestamp.valueOf(thread.getDate_creation()));
        ps.setTimestamp(4, Timestamp.valueOf(thread.getDate_update()));
        ps.setInt(5, thread.getId_user());

        ps.executeUpdate();
    }


    @Override
    public void supprimer(thread threads) throws SQLException {
        String sql = "DELETE FROM threads WHERE id_thread = ?";

        PreparedStatement ps = connection.prepareStatement(sql);

        ps.setInt(1, threads.getId_thread());

        int rows = ps.executeUpdate();

        if (rows > 0) {
            System.out.println("Thread supprimé avec succès !");
        } else {
            System.out.println("Aucun thread trouvé avec cet id !");
        }

    }

    @Override

        public void modifier(thread t) throws SQLException {

        if (t.getId_thread() <= 0) {
            throw new IllegalArgumentException("id_thread invalide pour modification !");
        }

        validerThread(t);  // 🔥 CONTROLE ICI

            String sql = "UPDATE threads SET titre = ?, contenu = ?, date_update = ?, id_user = ? WHERE id_thread = ?";

            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setString(1, t.getTitre());
            ps.setString(2, t.getContenu());
            ps.setObject(3, t.getDate_update());
            ps.setInt(4, t.getId_user());
            ps.setInt(5, t.getId_thread());

            ps.executeUpdate();

            System.out.println("Thread modifié avec succès !");
        }



    @Override
    public List<thread> recuperer() throws SQLException {
        List<thread> threads = new ArrayList<>();

        String sql = "SELECT * FROM threads";

        PreparedStatement ps = connection.prepareStatement(sql);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            thread t = new thread(
                    rs.getInt("id_thread"),
                    rs.getString("titre"),
                    rs.getString("contenu"),
                    rs.getTimestamp("date_creation").toLocalDateTime(),
                    rs.getTimestamp("date_update").toLocalDateTime(),
                    rs.getInt("id_user")
            );

            threads.add(t);
        }

        return threads;
    }

    private void validerThread(thread t) {

        if (t.getTitre() == null || t.getTitre().trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne doit pas être vide !");
        }

        if (t.getContenu() == null || t.getContenu().trim().isEmpty()) {
            throw new IllegalArgumentException("Le contenu ne doit pas être vide !");
        }

        if (t.getId_user() <= 0) {
            throw new IllegalArgumentException("id_user invalide !");
        }

        if (t.getDate_creation() == null || t.getDate_update() == null) {
            throw new IllegalArgumentException("Les dates ne doivent pas être null !");
        }
    }



}
