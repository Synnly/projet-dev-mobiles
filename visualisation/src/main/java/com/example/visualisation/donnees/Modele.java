package com.example.visualisation.donnees;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.visualisation.exceptions.modele.ExceptionAucunePiece;
import com.example.visualisation.exceptions.modele.ExceptionPiecesNonReliees;

import java.util.ArrayList;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "IDModele", scope = Modele.class)
public class Modele {
    private String nomModele;
    private int idModele;

    private ArrayList<Piece> listePieces;

    /**
     * Constructeur d'un modèle
     * @param nomModele Le nom du modèle
     */
    public Modele(String nomModele) {
        this.nomModele = nomModele;
        this.idModele = FabriqueIDs.getinstance().getIDModele();
        this.listePieces = new ArrayList<>();
    }

    /**
     * Constructeur d'un modèle. Le nom du modèle est généré automatiquement selon son identifiant
     */
    public Modele(){
        this.idModele = FabriqueIDs.getinstance().getIDModele();
        this.nomModele = "Modele "+this.idModele;
        this.listePieces = new ArrayList<>();
    }

    /**
     * Constructeur de copie profonde d'un modèle
     * @param m Le modèle à copier
     */
    public Modele(Modele m){
        this.nomModele = m.nomModele;
        this.idModele = m.idModele;
        this.listePieces = new ArrayList<>();
        for(Piece p : m.listePieces){
            this.listePieces.add(new Piece(p, this));
        }
    }

    /**
     * Renvoie le nom du modèle
     */
    public String getNomModele() {
        return nomModele;
    }

    /**
     * Modifie le nom du modèle
     * @param nomModele Le nouveau nom du modèle
     */
    public void setNomModele(String nomModele) {
        this.nomModele = nomModele;
    }

    /**
     * Ajoute une pièce au modèle
     * @param piece La pièce à ajouter
     */
    public void ajouterPiece(Piece piece) {
        if(!this.listePieces.contains(piece)){
            this.listePieces.add(piece);
        }
    }

    /**
     * Verfiie si le modèle est valide. Un modèle est valide si toutes ses pièces sont valides et que toutes les pièces soient reliées. Voir Piece.valider() pour les exceptions provenant d'une pièce invalide
     * @throws ExceptionAucunePiece Si le modèle ne contient aucune pièce
     * @throws ExceptionPiecesNonReliees Si le modèle contient des pièces non reliées
     */
    public void valider() throws Exception {
        if(this.listePieces.isEmpty()){
            throw new ExceptionAucunePiece();
        }

        for (Piece piece : this.listePieces){
            System.out.println(piece.getIdPiece());
        }

        // Si le modèle contient plus d'une pièce, on vérifie que chaque pièce est reliée à une autre pièce
        if(this.listePieces.size() > 1){
            for(Piece p : listePieces){
                int nbPortes = 0;
                for(Mur m : p.getListeMurs()){
                    nbPortes += m.getListePortes().size();
                }

                if(nbPortes == 0){
                    throw new ExceptionPiecesNonReliees(p.getNomPiece());
                }
            }
        }

        for(Piece piece : this.listePieces){
            piece.valider();
        }
    }

    public ArrayList<Piece> getListePieces() {
        return listePieces;
    }

    public String toJSON(){
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
        }
        catch (JsonProcessingException jpe){
            throw new RuntimeException(jpe);
        }
    }
}
