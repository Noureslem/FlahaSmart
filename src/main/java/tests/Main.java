package tests;

import entities.thread;
import servise.ServiceThreads;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        ServiceThreads serviceThreads = new ServiceThreads();

        LocalDateTime now = LocalDateTime.now();
        //ajouter
        thread thread = new thread("awlthreads", "9amhjdid", now, now,5);

        try {
            serviceThreads.ajouter(thread);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        //modiffier
        thread thread1 = new thread(
                3,
                "Titre modifié",
                "trere",
                now,
                now,
                5
        );
        try{
        serviceThreads.modifier(thread1);}
        catch (SQLException e){
            System.out.println(e.getMessage());
        }
      //supprimer
        thread threadToDelete = new thread();
        threadToDelete.setId_thread(7);

        try {
            serviceThreads.supprimer(threadToDelete);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
      //récupérer
        try {
            System.out.println("Liste des threads :");
            for (thread t : serviceThreads.recuperer()) {
                System.out.println(t);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            serviceThreads.ajouter(thread);
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur de saisie : " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Erreur SQL : " + e.getMessage());
        }





    }
}

