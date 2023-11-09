package fernandes_dos_santos_dev_mob.donnees;

import android.graphics.Rect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionMursIdentiques;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionNombreRectangleInvalide;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionOrientationsIncoherentes;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionPiecesIdentiques;


@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "idPorte")
public class Porte {
    private int idPorte;
    private Mur murA, murB;
    private Rect rectangleA, rectangleB;

    /**
     * Constructeur d'une porte. Une porte est un lien entre deux murs A et B et est représentée par un rectangle sur chaque mur. Ajoute la porte dans la liste des portes du mur
     * @param mur Le premier mur de la porte
     * @param rectangle Le rectangle de la porte sur le premier mur
     */
    public Porte(Mur mur, Rect rectangle) {
        this.idPorte = FabriqueIDs.getinstance().getIDPorte();
        this.murA = mur;
        this.rectangleA = rectangle;
        this.murB = null;
        this.rectangleB = null;
        mur.ajouterPorte(this);
    }

    /**
     * Constructeur de copie profonde d'une porte. À utiliser en conjonction avec setMurA et setMurB pour compléter la copie.
     * @param p La porte à copier
     */
    public Porte(Porte p){
        this.idPorte = p.idPorte;
        this.murA = null;
        this.rectangleA = null;
        this.murB = null;
        this.rectangleB = null;
    }

    /**
     * Renvoie l'identifiant de la porte
     */
    public int getIdPorte() {
        return idPorte;
    }

    /**
     * Modifie le mur A de la porte ainsi que le rectangle de la porte. Retire cette porte de l'ancien mur A puis l'ajoute dans la liste des portes du nouveau mur A
     * @param murA Le premier mur de la porte
     * @param rectangle Le rectangle de la porte sur le premier mur
     */
    public void setMurA(Mur murA, Rect rectangle) {
        if (this.murA!=null) {
            this.murA.retirerPorte(this);
        }
        this.murA = murA;
        if (this.murA!=null) {
            this.murA.ajouterPorte(this);
        }
        this.rectangleA = rectangle;
    }

    /**
     * Modifie le mur B de la porte ainsi que le rectangle de la porte. Retire cette porte de l'ancien mur B puis l'ajoute dans la liste des portes du nouveau mur B
     * @param murB Le premier mur de la porte
     * @param rectangle Le rectangle de la porte sur le premier mur
     */
    public void setMurB(Mur murB, Rect rectangle) {
        if (this.murB!=null) {
            this.murB.retirerPorte(this);
        }
        this.murB = murB;
        if (this.murB!=null) {
            this.murB.ajouterPorte(this);
        }
        this.rectangleB = rectangle;
    }

    /**
     * Renvoie le premier mur de la porte
     */
    public Mur getMurA() {
        return murA;
    }

    /**
     * Renvoie le deuxième mur de la porte
     */
    public Mur getMurB() {
        return murB;
    }

    /**
     * Renvoie le rectangle de la porte sur le mur correspondant
     * @param mur Le mur sur lequel se trouve le rectangle de la porte
     */
    public Rect getRectangle(Mur mur) {
        if(mur == murA) {
            return rectangleA;
        }
        else if(mur == murB) {
            return rectangleB;
        }
        else {
            return null;
        }
    }
    /**
     * Assigne le rectangle de la porte sur le mur correspondant. Si le mur en paramètre ne correspond à aucun des deux murs de la porte, ne fait rien
     * @param mur Le mur sur lequel se trouve le rectangle de la porte
     * @param rectangle Le rectangle de la porte sur le mur
     */
    public void setRectangle(Mur mur, Rect rectangle) {
        if(mur.equals(murA)) {
            this.rectangleA = rectangle;
        }
        if (mur.equals(murB)) {
            this.rectangleB = rectangle;
        }
    }

    /**
     * Verifie si la porte est valide. Une porte est valide si : <br/>
     * - ni les murs ni les rectangles ne sont null <br/>
     * - les murs sont differents <br/>
     * - les rectangles ne se superposent pas <br/>
     * - les murs sont bien adjacents <br/>
     * @throws ExceptionNombreRectangleInvalide Si les rectangles sont null
     * @throws ExceptionMursIdentiques Si les murs sont identiques
     * @throws ExceptionOrientationsIncoherentes Si les murs ne sont pas adjacents
     */
    public void valider() throws Exception {
        // Verifie que les rectangles ne sont pas null (et par conséquent que les murs ne sont pas null)
        if(rectangleA == null || rectangleB == null) {
            throw new ExceptionNombreRectangleInvalide(idPorte);
        }

        // Verifie que les murs sont differents
        if(murA.equals(murB)) {
            throw new ExceptionMursIdentiques(idPorte);
        }

        // Verifie que les murs sont bien adjacents
        if(murA.getOrientation() != (murB.getOrientation()+2)%4) {
            throw new ExceptionOrientationsIncoherentes(idPorte);
        }

        if(murA.getPiece() == murB.getPiece()) {
            throw new ExceptionPiecesIdentiques(idPorte);
        }
    }

    public Rect getRectangleA() {
        return rectangleA;
    }

    public Rect getRectangleB() {
        return rectangleB;
    }
}
