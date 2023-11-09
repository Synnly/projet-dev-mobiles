package fernandes_dos_santos_dev_mob.donnees;

import android.graphics.Bitmap;
import android.graphics.Rect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import fernandes_dos_santos_dev_mob.exceptions.mur.ExceptionPortesSeSuperposent;

import java.util.ArrayList;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "idMur")
public class Mur {
    public static final int NORD = 0;
    public static final int EST = 1;
    public static final int SUD = 2;
    public static final int OUEST = 3;

    private int idMur;
    private int orientation;
    private Bitmap image;
    private Piece piece;
    private ArrayList<Porte> listePortes;

    /**
     * Constructeur d'un mur.
     * @param orientation L'orientation du mur. Les valeurs possibles sont : Mur.NORD, Mur.EST, Mur.SUD, Mur.OUEST. Si l'orientation n'est pas valide, l'orientation par défaut est Mur.NORD
     */
    public Mur(int orientation) {
        this.idMur = FabriqueIDs.getinstance().getIDMur();
        this.image = null;
        this.listePortes = new ArrayList<>();
        this.piece = null;

        if (orientation < 0 || orientation > 3) {
            this.orientation = Mur.NORD;
        } else {
            this.orientation = orientation;
        }
    }

    /**
     * Constructeur de copie profonde d'un mur
     * @param m Le mur à copier
     * @param p La pièce dans laquelle sera inséré le mur copié
     */
    public Mur(Mur m, Piece p){
        this.idMur = m.idMur;
        this.orientation = m.orientation;
        this.image = m.image;
        this.listePortes = new ArrayList<>();
        this.piece = p;
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
     * Modifie la pièce à laquelle appartient le mur
     * @param piece La pièce à laquelle appartient le mur
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
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

