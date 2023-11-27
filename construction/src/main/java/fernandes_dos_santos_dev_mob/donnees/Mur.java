package fernandes_dos_santos_dev_mob.donnees;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import fernandes_dos_santos_dev_mob.exceptions.mur.ExceptionPortesSeSuperposent;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "IDMur")
public class Mur {
    public static final int NORD = 0;
    public static final int EST = 1;
    public static final int SUD = 2;
    public static final int OUEST = 3;
    private int idMur;
    private int orientation;
    private byte[] binaireImage;
    private Piece piece;
    private ArrayList<Porte> listePortes;

    /**
     * Constructeur d'un mur.
     * @param orientation L'orientation du mur. Les valeurs possibles sont : Mur.NORD, Mur.EST, Mur.SUD, Mur.OUEST. Si l'orientation n'est pas valide, l'orientation par défaut est Mur.NORD
     */
    public Mur(int orientation) {
        this.idMur = FabriqueIDs.getinstance().getIDMur();
        this.binaireImage = new byte[0];
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
        this.binaireImage = m.binaireImage;
        this.listePortes = new ArrayList<>();
        this.piece = p;
    }

    public Mur(@JsonProperty("idMur") int id, @JsonProperty("orientation") int orientation, @JsonProperty("binaireImage") byte[] binaireImage, @JsonProperty("piece") Piece piece, @JsonProperty("listePortes") ArrayList<Porte> listePortes) {
        this.idMur = id;
        this.orientation = orientation;
        this.binaireImage = binaireImage;
        this.piece = piece;
        this.listePortes = listePortes;
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
     * Assigne une image au mur en utilisant le binaire du bitmap
     * @param binaireImage Le binaire du bitmap à assigner
     */
    public void setBinaireImage(byte[] binaireImage) {
        this.binaireImage = binaireImage;
    }

    /**
     * Assigne une bitmap au mur en utilisant
     * @param image Le bitmap à assigner
     */
    public void setBitmap(Bitmap image) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        this.binaireImage = stream.toByteArray();
    }

    /**
     * Renvoie le binaire du bitmap du mur
     */
    public byte[] getBinaireImage() {
        return binaireImage;
    }

    @JsonIgnore
    /**
     * Renvoie le bitmap du mur
     */
    public Bitmap getImageBitmap(){
        return BitmapFactory.decodeByteArray(binaireImage, 0, binaireImage.length);
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
                Rect a = porte.getRectangle();
                Rect b = porte1.getRectangle();
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

