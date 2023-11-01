package fernandes_dos_santos_dev_mob;

import android.graphics.Rect;
import fernandes_dos_santos_dev_mob.donnees.Modele;
import fernandes_dos_santos_dev_mob.donnees.Mur;
import fernandes_dos_santos_dev_mob.donnees.Piece;
import fernandes_dos_santos_dev_mob.donnees.Porte;
import fernandes_dos_santos_dev_mob.exceptions.modele.ExceptionAucunePiece;
import junit.framework.TestCase;

public class ModeleTest extends TestCase {
    private Modele modele;
    private Piece piece1, piece2;
    private Mur mur1, mur2, mur3, mur4, mur5, mur6, mur7, mur8;
    private Porte porte1;
    private Rect rect1, rect2;


    public void setUp() throws Exception {
        super.setUp();
        modele = new Modele();
        mur1 = new Mur(Mur.NORD); mur2 = new Mur(Mur.EST); mur3 = new Mur(Mur.SUD); mur4 = new Mur(Mur.OUEST);
        mur5 = new Mur(Mur.NORD); mur6 = new Mur(Mur.EST); mur7 = new Mur(Mur.SUD); mur8 = new Mur(Mur.OUEST);
        rect1 = new Rect(0, 0, 0, 0);
        porte1 = new Porte(mur1, rect1);
        porte1.setMurB(mur7, rect1);
    }

    public void testValider() {
        try {
            modele.valider();
        }
        catch (ExceptionAucunePiece e){
            assert(("Le modèle ne contient aucune pièce.").equals(e.getMessage())):"Erreur : le message de l'exception est incorrect";
        }
        catch (Exception e){
            fail("Erreur : valider() lance ne lance pas l'exception ExceptionAucunePiece quand le modèle ne contient aucune pièce");
        }

        piece1 = new Piece(modele); piece2 = new Piece(modele);
        piece1.ajouterMur(mur1); piece1.ajouterMur(mur2); piece1.ajouterMur(mur3); piece1.ajouterMur(mur4);
        piece2.ajouterMur(mur5); piece2.ajouterMur(mur6); piece2.ajouterMur(mur7); piece2.ajouterMur(mur8);
        modele.ajouterPiece(piece1);
        try{
            modele.valider();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            fail("Erreur : valider() lance une exception alors que le modèle est valide");
        }

        modele.ajouterPiece(piece2);
        try{
            modele.valider();
        }
        catch (Exception e){
            fail("Erreur : valider() lance une exception alors que le modèle est valide");
        }

    }
}