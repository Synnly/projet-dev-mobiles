package fernandes_dos_santos_dev_mob;

import android.graphics.Rect;
import fernandes_dos_santos_dev_mob.donnees.Modele;
import fernandes_dos_santos_dev_mob.donnees.Mur;
import fernandes_dos_santos_dev_mob.donnees.Piece;
import fernandes_dos_santos_dev_mob.donnees.Porte;
import fernandes_dos_santos_dev_mob.exceptions.piece.ExceptionNombreMursInvalide;
import fernandes_dos_santos_dev_mob.exceptions.piece.ExceptionPiecesReliesParPlusieursMurs;
import junit.framework.TestCase;

public class PieceTest extends TestCase {

    private Modele modele;
    private Piece piece1, piece2;
    private Mur mur1, mur2, mur3, mur4, mur5, mur6, mur7, mur8;
    private Porte porte1, porte2;
    private Rect rect1, rect2;

    public void setUp() throws Exception {
        super.setUp();
        modele = new Modele();
        piece1 = new Piece(modele);
        piece2 = new Piece(modele);
        mur1 = new Mur(Mur.NORD);
        mur2 = new Mur(Mur.EST);
        mur3 = new Mur(Mur.SUD);
        rect1 = new Rect(0, 0, 0, 0);
        rect2 = new Rect(0, 0, 0, 0);
    }

    public void testAjouterMur() {
        assert(piece1.getMur(Mur.NORD)==null):"Erreur : la liste de murs n'est pas vide";

        mur1 = new Mur(Mur.NORD);
        piece1.ajouterMur(mur1);
        assert(piece1.getMur(Mur.NORD)==mur1):"Erreur : le mur n'a pas été ajouté à la liste de murs";
        assert(piece1.getMur(Mur.EST)==null):"Erreur : le mur a été ajouté avec la mauvaise orientation";
        assert(piece1.getMur(Mur.SUD)==null):"Erreur : le mur a été ajouté avec la mauvaise orientation";
        assert(piece1.getMur(Mur.OUEST)==null):"Erreur : le mur a été ajouté avec la mauvaise orientation";

        mur2 = new Mur(Mur.EST);
        piece1.ajouterMur(mur2);
        assert(piece1.getMur(Mur.NORD)==mur1):"Erreur : le mur n'a pas été ajouté à la liste de murs";
        assert(piece1.getMur(Mur.EST)==mur2):"Erreur : le mur n'a pas été ajouté à la liste de murs";
        assert(piece1.getMur(Mur.SUD)==null):"Erreur : le mur a été ajouté avec la mauvaise orientation";
        assert(piece1.getMur(Mur.OUEST)==null):"Erreur : le mur a été ajouté avec la mauvaise orientation";

        mur3 = new Mur(Mur.NORD);
        piece1.ajouterMur(mur3);
        assert(piece1.getMur(Mur.NORD)==mur3):"Erreur : le mur n'a pas été ajouté à la liste de murs";
        assert(piece1.getMur(Mur.NORD)!=mur1):"Erreur : le mur n'a pas été remplacé quand un mur de la même orientation avait deja été ajouté";
        assert(piece1.getMur(Mur.SUD)==null):"Erreur : le mur a été ajouté avec la mauvaise orientation";
        assert(piece1.getMur(Mur.OUEST)==null):"Erreur : le mur a été ajouté avec la mauvaise orientation";

    }

    public void testGetMur() {
        mur1 = new Mur(Mur.NORD);
        piece1.ajouterMur(mur1);
        assert(piece1.getMur(Mur.NORD)==mur1):"Erreur : le mur n'a pas été ajouté à la liste de murs";
        assert(piece1.getMur(Mur.EST)==null):"Erreur : le mur a été ajouté avec la mauvaise orientation";
        assert(piece1.getMur(Mur.SUD)==null):"Erreur : le mur a été ajouté avec la mauvaise orientation";
        assert(piece1.getMur(Mur.OUEST)==null):"Erreur : le mur a été ajouté avec la mauvaise orientation";

        mur2 = new Mur(Mur.EST);
        piece1.ajouterMur(mur2);
        assert(piece1.getMur(Mur.NORD)==mur1):"Erreur : le mur n'a pas été ajouté à la liste de murs";
        assert(piece1.getMur(Mur.EST)==mur2):"Erreur : le mur n'a pas été ajouté à la liste de murs";
        assert(piece1.getMur(Mur.SUD)==null):"Erreur : le mur a été ajouté avec la mauvaise orientation";
        assert(piece1.getMur(Mur.OUEST)==null):"Erreur : le mur a été ajouté avec la mauvaise orientation";

        mur3 = new Mur(Mur.NORD);
        piece1.ajouterMur(mur3);
        assert(piece1.getMur(Mur.NORD)==mur3):"Erreur : le mur n'a pas été ajouté à la liste de murs";
        assert(piece1.getMur(Mur.NORD)!=mur1):"Erreur : le mur n'a pas été remplacé quand un mur de la même orientation avait deja été ajouté";
        assert(piece1.getMur(Mur.SUD)==null):"Erreur : le mur a été ajouté avec la mauvaise orientation";
        assert(piece1.getMur(Mur.OUEST)==null):"Erreur : le mur a été ajouté avec la mauvaise orientation";
    }

    public void testValiderNombreMursInvalide() {
        try{
            piece1.valider();
        }
        catch (ExceptionNombreMursInvalide e){
            assert(("Le nombre de murs de la piece "+piece1.getNomPiece()+" est invalide (=/=4)").equals(e.getMessage())):"Erreur : le message de l'exception n'est pas correct";
        }
        catch (Exception e){
            fail("Erreur : valider() ne renvoie pas la bonne exception quand la pièce n'a pas 4 murs");
        }

        for (Mur mur: new Mur[]{mur1, mur2, mur3}){
            try{
                piece1.ajouterMur(mur);
                piece1.valider();
            }
            catch (ExceptionNombreMursInvalide e){
                assert(("Le nombre de murs de la piece "+piece1.getNomPiece()+" est invalide (=/=4)").equals(e.getMessage())):"Erreur : le message de l'exception n'est pas correct";
            }
            catch (Exception e){
                fail("Erreur : valider() ne renvoie pas la bonne exception quand la pièce n'a pas 4 murs");
            }
        }
    }

    public void testValiderPiecesPlusieursMursEnCommun(){
        mur1 = new Mur(Mur.NORD); mur2 = new Mur(Mur.EST); mur3 = new Mur(Mur.SUD); mur4 = new Mur(Mur.OUEST);
        piece1.ajouterMur(mur1); piece1.ajouterMur(mur2); piece1.ajouterMur(mur3); piece1.ajouterMur(mur4);

        mur5 = new Mur(Mur.NORD); mur6 = new Mur(Mur.EST); mur7 = new Mur(Mur.SUD); mur8 = new Mur(Mur.OUEST);
        piece2.ajouterMur(mur5); piece2.ajouterMur(mur6); piece2.ajouterMur(mur7); piece2.ajouterMur(mur8);
        porte1 = new Porte(mur3, rect1, piece2);
        porte2 = new Porte(mur5, rect2, piece1);

        try{
            piece1.valider();
        }
        catch (ExceptionPiecesReliesParPlusieursMurs e){
            assert(("Les pièces "+piece1.getIdPiece()+" et "+piece2.getIdPiece()+" sont reliées par plusieurs murs").equals(e.getMessage())):"Erreur : le message de l'exception n'est pas correct";
        }
        catch (Exception e){
            fail("Erreur : valider() ne renvoie pas la bonne exception quand les pièces sont reliées par plusieurs murs");
        }
    }
}