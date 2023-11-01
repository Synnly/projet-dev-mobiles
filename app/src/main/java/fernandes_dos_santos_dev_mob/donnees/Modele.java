package fernandes_dos_santos_dev_mob.donnees;

import fernandes_dos_santos_dev_mob.exceptions.modele.ExceptionAucunePiece;

import java.util.ArrayList;

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
     * Ajoute une pièce au modèle
     * @param piece La pièce à ajouter
     */
    public void ajouterPiece(Piece piece) {
        this.listePieces.add(piece);
    }

    /**
     * Verfiie si le modèle est valide. Un modèle est valide si toutes ses pièces sont valides. Voir Piece.valider() pour les exceptions provenant d'une pièce invalide
     * @throws ExceptionAucunePiece Si le modèle ne contient aucune pièce
     */
    public void valider() throws Exception {
        if(this.listePieces.isEmpty()){
            throw new ExceptionAucunePiece();
        }

        for(Piece piece : this.listePieces){
            piece.valider();
        }
    }
}
