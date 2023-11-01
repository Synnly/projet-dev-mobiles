package fernandes_dos_santos_dev_mob.donnees;

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
}
