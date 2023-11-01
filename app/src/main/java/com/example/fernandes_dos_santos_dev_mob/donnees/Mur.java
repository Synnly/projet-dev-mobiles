package com.example.fernandes_dos_santos_dev_mob.donnees;

import android.graphics.Bitmap;
import android.graphics.Rect;
import com.example.fernandes_dos_santos_dev_mob.exceptions.mur.ExceptionPortesSeSuperposent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mur {
    public static final int NORD = 0;
    public static final int EST = 1;
    public static final int SUD = 2;
    public static final int OUEST = 3;

    private int idMur;
    private int orientation;
    private Bitmap image;
    private ArrayList<Porte> listePortes;

    private Piece piece;

    /**
     * Constructeur d'un mur.
     *
     * @param p           La pièce à laquelle appartient le mur
     * @param orientation L'orientation du mur. Les valeurs possibles sont : Mur.NORD, Mur.EST, Mur.SUD, Mur.OUEST. Si l'orientation n'est pas valide, l'orientation par défaut est Mur.NORD
     */
    public Mur(Piece p, int orientation) {
        this.idMur = FabriqueIDs.getinstance().getIDMur();
        this.image = null;
        this.listePortes = new ArrayList<>();
        this.piece = p;

        if (orientation < 0 || orientation > 3) {
            this.orientation = Mur.NORD;
        } else {
            this.orientation = orientation;
        }
    }

    /**
     * Renvoie l'identifiant du mur
     */
    public int getIdMur() {
        return idMur;
    }

    /**
     * Renvoie la pièce à laquelle appartient le mur
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * Assigne une image au mur
     *
     * @param image L'image à assigner
     */
    public void setImage(Bitmap image) {
        this.image = image;
    }

    /**
     * Renvoie l'image du mur
     */
    public Bitmap getImage() {
        return image;
    }

    /**
     * Renvoie l'orentation du mur. Les valeurs possibles sont : Mur.NORD, Mur.EST, Mur.SUD, Mur.OUEST
     */
    public int getOrientation() {
        return orientation;
    }

    /**
     * Ajoute une porte au mur
     * @param porte La porte à ajouter
     */
    public void ajouterPorte(Porte porte) {
        this.listePortes.add(porte);
    }

    /**
     * Renvoie la liste des portes du mur
     */
    public ArrayList<Porte> getListePortes() {
        return listePortes;
    }

    /**
     * Retire la porte du mur
     * @param porte La porte à retirer
     */
    public void retirerPorte(Porte porte) {
        this.listePortes.remove(porte);
    }

    /**
     * Verifie si le mur est valide. Un mur est valide si toutes ses portes sont valides. Voir Porte.valider() pour les exceptions lancées
     * @throws ExceptionPortesSeSuperposent Si deux portes se superposent
     */
    public void valider() throws Exception {
        for (Porte porte : this.listePortes) {
            porte.valider();

            for(Porte porte1 : this.listePortes){
                Rect a = porte.getRectangle(this);
                Rect b = porte1.getRectangle(this);
                if(a.left < b.right && b.left < a.right && a.top < b.bottom && b.top < a.bottom){
                    throw new ExceptionPortesSeSuperposent(porte.getIdPorte(), porte1.getIdPorte());
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mur mur = (Mur) o;
        return idMur == mur.idMur;
    }

}

