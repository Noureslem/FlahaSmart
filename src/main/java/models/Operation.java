package models;

import java.sql.Date;

public class Operation {
    private int id_operation ;
    private int id_equipement;
    private String nomEquipement;
    private String type_operation;
    private Date date_debut ;
    private Date date_fin ;
    private String statut;


    public Operation(int id_operation, int id_equipement, String type_operation, Date date_debut, Date date_fin, String statut) {
        this.id_operation = id_operation;
        this.id_equipement = id_equipement;
        this.type_operation = type_operation;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.statut = statut;

    }
    public Operation(String type_operation, Date date_debut, Date date_fin, String statut) {
        this.id_equipement = id_equipement;
        this.type_operation = type_operation;
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.statut = statut;

    }

    public Operation() {

    }

    public int getId_operation() {
        return id_operation;
    }
    public void setId_operation(int id_operation) {
        this.id_operation = id_operation;
    }
    public String getType_operation() {
        return type_operation;
    }
    public void setType_operation(String type_operation) {
        this.type_operation = type_operation;
    }
    public Date getDate_debut() {
        return date_debut;
    }
    public void setDate_operation(Date date_operation) {
        this.date_debut = date_operation;
    }
    public String getStatut() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut = statut;
    }
    public void setDate_debut(Date date_debut) {
        this.date_debut = date_debut;
    }
    public Date getDate_fin() {
        return date_fin;
    }
    public void setDate_fin(Date date_fin) {
        this.date_fin = date_fin;
    }
    public int getId_equipement() {
        return id_equipement;
    }
    public void setId_equipement(int id_equipement) {
        this.id_equipement = id_equipement;
    }
    public String getNomEquipement() {
        return nomEquipement;
    }
    public void setNomEquipement(String nomEquipement) {
        this.nomEquipement = nomEquipement;
    }

    @Override
    public String toString() {
        return "Operation : " +
                " id_equipement = '" + id_equipement + '\'' +
                " type_operation = '" + type_operation + '\'' +
                " Date Debut = " + date_debut +
                " Date Fin = " + date_fin +
                " statut = '" + statut + "'\n"
                ;
    }

}
