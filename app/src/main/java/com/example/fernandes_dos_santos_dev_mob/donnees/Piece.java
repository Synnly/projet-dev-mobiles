package com.example.fernandes_dos_santos_dev_mob.donnees;

import com.example.fernandes_dos_santos_dev_mob.exceptions.piece.ExceptionNombreMursInvalide;

import java.util.ArrayList;
import java.util.List;

public class Piece {
    private String nomPiece;
    private int idPiece;
    private Modele modele;
    private ArrayList<Mur> listeMurs;

    /**
     * Constructeur d'une pièce
     * @param nomPiece Le nom de la pièce
     * @param modele Le modèle de la pièce
     */
    public Piece(String nomPiece, Modele modele) {
        this.nomPiece = nomPiece;
        this.modele = modele;
        this.idPiece = FabriqueIDs.getinstance().getIDPiece();
        this.listeMurs = new ArrayList<>(4);
    }

    /**
     * Constructeur d'une pièce. Le nom de la pièce est généré automatiquement selon son identifiant
     * @param modele Le modèle de la pièce
     */
    public Piece(Modele modele) {
        this.modele = modele;
        this.idPiece = FabriqueIDs.getinstance().getIDPiece();
        this.nomPiece = "Piece " + this.idPiece;
        this.listeMurs = new ArrayList<>(4);
    }

    /**
     * Renvoie l'identifiant de la pièce
     */
    public int getIdPiece() {
        return idPiece;
    }

    /**
     * Renvoie le modèle de la pièce
     */
    public String getNomPiece() {
        return nomPiece;
    }

    /**
     * Modifie le nom de la pièce
     * @param nomPiece Le nom de la pièce
     */
    public void setNomPiece(String nomPiece) {
        this.nomPiece = nomPiece;
    }

    /**
     * Ajoute le mur à la pièce s'il n'y a pas déjà un mur avec la même orientation, sinon remplace le mur existant
     * @param mur Le mur à ajouter
     */
    public void ajouterMur(Mur mur) {
        // On vérifie s'il y a déjà un mur avec la même orientation
        for(int i = 0; i < this.listeMurs.size(); i++) {
            if (listeMurs.get(i).getOrientation() == mur.getOrientation()) {
                this.listeMurs.set(i, mur);
                break;
            }
        }
        // S'il n'y a pas de mur avec la même orientation, on ajoute le mur
        this.listeMurs.add(mur);
    }

    /**
     * Verifie si la pièce est valide. Une pièce est valide si elle possède 4 murs d'orientation différents et valides. Voir Mur.valider() pour les exceptions prevenant d'un mur invalide
     * @throws ExceptionNombreMursInvalide Si la pièce ne possède pas 4 murs valides
     */
    public void valider() throws Exception {
        // Verification de la validité de chaque mur
        for (Mur mur : this.listeMurs) {
            mur.valider();
        }
        // Chaque mur est valide. On vérifie qu'il y a 4 murs valides
        if(listeMurs.size() != 4){
            throw new ExceptionNombreMursInvalide(nomPiece);
        }
    }
}
