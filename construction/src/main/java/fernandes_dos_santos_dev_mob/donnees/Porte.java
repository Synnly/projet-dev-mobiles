package fernandes_dos_santos_dev_mob.donnees;

import android.graphics.Rect;
import com.fasterxml.jackson.annotation.*;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionOrientationsIncoherentes;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionPiecesIdentiques;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionPorteSansRetour;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionRectangleNull;


@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "IDPorte", scope = Porte.class)
public class Porte {
    private int idPorte;
    private int left = -1;
    private int top = -1;
    private int right = -1;
    private int bottom = -1;
    private Mur murDepart;
    private int idPieceArrivee;
    @JsonIgnore
    private Piece pieceArrivee;

    /*
    Une entité divine doit surement me hair
    Ce parametre ne sert a rien mais pour une raison qui m'échappe si je la retire jackson inclus l'objet android.graphics.Rect
    dans le json et un attribut "empty" est ajouté alors qu'il n'existe pas dans la classe Rect et casse la deserialisation
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
        setPieceArrivee(piece);
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
        this.idPieceArrivee = p.idPieceArrivee;
        this.pieceArrivee = null;
    }

    /**
     * Constructeur par defaut d'une porte. Utilisé par Jackson pour la désérialisation. <br/><u>NE PAS UTILISER DANS LE CODE<u/>
     */
    public Porte(){
        this.idPorte = FabriqueIDs.getinstance().getIDPorte();
        this.murDepart = null;
        this.left = 0;
        this.top = 0;
        this.right = 0;
        this.bottom = 0;
        this.idPieceArrivee = -1;
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

    /**
     * Renvoie la piece d'arrivée de la porte
     */
    public Piece getPieceArrivee() {
        return pieceArrivee;
    }

    /**
     * Modifie la piece d'arrivée de la porte
     * @param pieceArrivee La piece d'arrivée de la porte
     */
    public void setPieceArrivee(Piece pieceArrivee) {
        this.pieceArrivee = pieceArrivee;
        this.idPieceArrivee = (pieceArrivee == null ? -1 : pieceArrivee.getIdPiece());
    }

    /**
     * Modifie l'identifiant de la piece d'arrivée de la porte
     * @param idPieceArrivee L'identifiant de la piece d'arrivée de la porte
     */
    public void setIdPieceArrivee(int idPieceArrivee) {
        this.idPieceArrivee = idPieceArrivee;
    }

    /**
     * Renvoie l'identifiant de la piece d'arrivée de la porte
     */
    public int getIdPieceArrivee(){
        return idPieceArrivee;
    }

    /**
     * Renvoie la coordonnée x du coin gauche du rectangle de la porte
     */
    public int getLeft() {
        return left;
    }

    /**
     * Modifie la coordonnée x du coin gauche du rectangle de la porte
     * @param left La coordonnée x du coin gauche du rectangle de la porte
     */
    public void setLeft(int left) {
        this.left = left;
    }

    /**
     * Renvoie la coordonnée y du coin supérieur du rectangle de la porte
     */
    public int getTop() {
        return top;
    }

    /**
     * Modifie la coordonnée y du coin supérieur du rectangle de la porte
     * @param top La coordonnée y du coin supérieur du rectangle de la porte
     */
    public void setTop(int top) {
        this.top = top;
    }

    /**
     * Renvoie la coordonnée x du coin droit du rectangle de la porte
     */
    public int getRight() {
        return right;
    }

    /**
     * Modifie la coordonnée x du coin droit du rectangle de la porte
     * @param right La coordonnée x du coin droit du rectangle de la porte
     */
    public void setRight(int right) {
        this.right = right;
    }

    /**
     * Renvoie la coordonnée y du coin inférieur du rectangle de la porte
     */
    public int getBottom() {
        return bottom;
    }

    /**
     * Modifie la coordonnée y du coin inférieur du rectangle de la porte
     * @param bottom La coordonnée y du coin inférieur du rectangle de la porte
     */
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

        // On parcourt les portes de la piece d'arrivée pour vérifier qu'il existe une porte sur le mur d'orientation opposée qui pointe vers la piece actuelle
        boolean aPorteRetour = false;
        for(Porte p : pieceArrivee.getMur((murDepart.getOrientation()+2)%4).getListePortes()){
            if (p.getPieceArrivee() == murDepart.getPiece()) {
                aPorteRetour = true;
                break;
            }
        }
        if(!aPorteRetour){
            throw new ExceptionPorteSansRetour(idPorte);
        }
    }
}
