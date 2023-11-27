package com.example.visualisation.donnees;

public class FabriqueIDs {
    private static FabriqueIDs instance = new FabriqueIDs();
    public static FabriqueIDs getinstance() {return instance;}

    private int idModele = 0;
    private int idPiece = 0;
    private int idMur = 0;
    private int idPorte = 0;

    /**
     * Renvoie un id pour un modele
     */
    public int getIDModele() {return idModele++;}

    /**
     * Renvoie un id pour une piece
     */
    public int getIDPiece() {return idPiece++;}

    /**
     * Renvoie un id pour un mur
     */
    public int getIDMur() {return idMur++;}

    /**
     * Renvoie un id pour une porte
     */
    public int getIDPorte() {return idPorte++;}

    /**
     * Initialise l'id de départ pour les modeles
     * @param id L'id de départ
     */
    public void initIDModele(int id) {idModele = id;}

    /**
     * Initialise l'id de départ pour les pieces
     * @param id L'id de départ
     */
    public void initIDPiece(int id) {idPiece = id;}

    /**
     * Initialise l'id de départ pour les murs
     * @param id L'id de départ
     */
    public void initIDMur(int id) {idMur = id;}

    /**
     * Initialise l'id de départ pour les portes
     * @param id L'id de départ
     */
    public void initIDPorte(int id) {idPorte = id;}
}
