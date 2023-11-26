package fernandes_dos_santos_dev_mob.donnees;

import android.graphics.Rect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionOrientationsIncoherentes;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionPiecesIdentiques;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionRectangleNull;


@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "IDPorte", scope = Porte.class)
public class Porte {
    private int idPorte;
    private int left = -1;
    private int top = -1;
    private int right = -1;
    private int bottom = -1;
    private Mur murDepart;
    private Piece pieceArrivee;

    /*
    Une entité divine doit surement me hair
    Ce parametre ne sert a rien mais pour une raison qui m'échappe si je la retire
    jackson inclus l'objet android.graphics.Rect dans le json et un attribut "empty" est ajouté
    alors qu'il n'existe pas dans la classe Rect
    :shrug:
     */
    @JsonIgnore
    private Rect rectangle;

    /**
     * Constructeur d'une porte. Une porte est un lien entre un mur de départ et une piece d'arrivée et est représentée par un rectangle sur le mur. Ajoute la porte dans la liste des portes du mur
     * @param mur Le mur de la porte
     * @param rectangle Le rectangle de la porte sur le premier mur
     * @param piece La piece d'arrivée de la porte
     */
    public Porte(Mur mur, Rect rectangle, Piece piece) {
        this.idPorte = FabriqueIDs.getinstance().getIDPorte();
        this.murDepart = mur;
        this.left = rectangle.left;
        this.top = rectangle.top;
        this.right = rectangle.right;
        this.bottom = rectangle.bottom;
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
        setRectangle(p.getRectangle());
        this.pieceArrivee = null;
    }

    public Porte(){
        this.idPorte = FabriqueIDs.getinstance().getIDPorte();
        this.murDepart = null;
        this.left = 0;
        this.top = 0;
        this.right = 0;
        this.bottom = 0;
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
        setRectangle(rectangle);
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
        return new Rect(left, top, right, bottom);
    }

    /**
     * Assigne le rectangle de la porte au mur
     * @param rectangle Le rectangle de la porte
     */
    public void setRectangle(Rect rectangle) {
        if(rectangle == null) {
            this.left = -1;
            this.top = -1;
            this.right = -1;
            this.bottom = -1;
        }
        else {
            this.left = rectangle.left;
            this.top = rectangle.top;
            this.right = rectangle.right;
            this.bottom = rectangle.bottom;
        }
    }

    public Piece getPieceArrivee() {
        return pieceArrivee;
    }

    public void setPieceArrivee(Piece pieceArrivee) {
        this.pieceArrivee = pieceArrivee;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
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
        if(left == -1 && top == -1 && right == -1 && bottom == -1) {
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
