package fernandes_dos_santos_dev_mob.donnees;

import android.graphics.Rect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionRectangleNull;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionOrientationsIncoherentes;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionPiecesIdentiques;


@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "idPorte")
public class Porte {
    private int idPorte;
    private Mur murDepart;
    private Rect rectangle;
    private Piece pieceArrivee;

    /**
     * Constructeur d'une porte. Une porte est un lien entre un mur de départ et une piece d'arrivée et est représentée par un rectangle sur le mur. Ajoute la porte dans la liste des portes du mur
     * @param mur Le mur de la porte
     * @param rectangle Le rectangle de la porte sur le premier mur
     * @param piece La piece d'arrivée de la porte
     */
    public Porte(Mur mur, Rect rectangle, Piece piece) {
        this.idPorte = FabriqueIDs.getinstance().getIDPorte();
        this.murDepart = mur;
        this.rectangle = rectangle;
        this.pieceArrivee = piece;
        mur.ajouterPorte(this);
    }

    /**
     * Constructeur de copie profonde d'une porte. À utiliser en conjonction avec setMurDepart et setPieceArrivee pour compléter la copie.
     * @param p La porte à copier
     */
    public Porte(Porte p){
        this.idPorte = p.idPorte;
        this.murDepart = null;
        this.rectangle = new Rect(p.rectangle);
        this.pieceArrivee = null;
    }

    /**
     * Renvoie l'identifiant de la porte
     */
    public int getIdPorte() {
        return idPorte;
    }

    /**
     * Modifie le mur de la porte ainsi que le rectangle de la porte. Retire cette porte de l'ancien mur puis l'ajoute dans la liste des portes du nouveau mur
     * @param mur Le mur de la porte
     * @param rectangle Le rectangle de la porte sur le premier mur
     */
    public void setMurDepart(Mur mur, Rect rectangle) {
        if (this.murDepart !=null) {
            this.murDepart.retirerPorte(this);
        }
        this.murDepart = mur;
        if (this.murDepart !=null) {
            this.murDepart.ajouterPorte(this);
        }
        this.rectangle = rectangle;
    }

    /**
     * Renvoie le mur de la porte
     */
    public Mur getMurDepart() {
        return murDepart;
    }

    /**
     * Renvoie le rectangle de la porte
     */
    public Rect getRectangle() {
        return rectangle;
    }

    /**
     * Assigne le rectangle de la porte au mur
     * @param rectangle Le rectangle de la porte
     */
    public void setRectangle(Rect rectangle) {
        this.rectangle = rectangle;
    }

    public Piece getPieceArrivee() {
        return pieceArrivee;
    }

    public void setPieceArrivee(Piece pieceArrivee) {
        this.pieceArrivee = pieceArrivee;
    }

    /**
     * Verifie si la porte est valide. Une porte est valide si : <br/>
     * - le mur n'est pas null <br/>
     * - les pieces de départ et d'arrive sont differents <br/>
     * - il existe une porte dans la piece d'arrivée d'orientation opposée <br/>
     * @throws ExceptionRectangleNull Si le rectangle est null
     * @throws ExceptionPiecesIdentiques Si les pieces sont identiques
     * @throws ExceptionOrientationsIncoherentes Si il n'existe pas de porte dans la piece d'arrivée d'orientation opposée
     */
    public void valider() throws Exception {
        // Verifie que le rectangle n'est pas null
        if(rectangle == null) {
            throw new ExceptionRectangleNull(idPorte);
        }

        // Verifie que les murs sont bien adjacents
        if(pieceArrivee == null || !pieceArrivee.porteOrientationExiste((murDepart.getOrientation()+2)%4)) {
            throw new ExceptionOrientationsIncoherentes(idPorte);
        }

        if(murDepart.getPiece() == pieceArrivee) {
            throw new ExceptionPiecesIdentiques(idPorte);
        }
    }
}
