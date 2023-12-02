package fernandes_dos_santos_dev_mob;

import android.graphics.Rect;
import fernandes_dos_santos_dev_mob.donnees.Modele;
import fernandes_dos_santos_dev_mob.donnees.Mur;
import fernandes_dos_santos_dev_mob.donnees.Piece;
import fernandes_dos_santos_dev_mob.donnees.Porte;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionRectangleNull;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionOrientationsIncoherentes;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionPiecesIdentiques;
import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeEach;

public class PorteTest extends TestCase {

    private Porte porte1, porte2;
    private Mur murA, murB, murC, murD;
    private Rect rectangleA, rectangleB, rectangleC, rectangleD;
    private Piece pieceA, pieceB, pieceC, pieceD;
    private Modele modele;

    @BeforeEach
    public void setUp() {
        modele = new Modele();
        pieceA = new Piece(modele); pieceB = new Piece(modele);
        pieceC = new Piece(modele); pieceD = new Piece(modele);
        murA = new Mur(Mur.NORD); murB = new Mur(Mur.SUD);
        murC = new Mur(Mur.SUD); murD = new Mur(Mur.OUEST);
        pieceA.ajouterMur(murA); pieceB.ajouterMur(murB);
        pieceA.ajouterMur(murC); pieceD.ajouterMur(murD);
        rectangleA = new Rect(0, 0, 0, 0);
        rectangleB = new Rect(10, 10, 10, 10);
        rectangleC = new Rect(10, 10, 10, 10);
        rectangleD = new Rect(10, 10, 10, 10);
        porte1 = new Porte(murA, rectangleA, pieceA);
    }

    public void testSetMurDepart() {
        assert(murA.getListePortes().get(0) == porte1): "Erreur : la porte n'a pas été ajoutée à la liste des portes du mur lors de l'instanciation";
        assert(porte1.getMurDepart() == murA):"Erreur : le mur A n'a pas été ajouté à la porte";

        porte1.setMurDepart(murB, rectangleB);

        assert(murB.getListePortes().get(0) == porte1): "Erreur : la porte n'a pas été ajoutée à la liste des portes du mur lors du setMurDepart();()";
        assert(murA.getListePortes().size() == 0): "Erreur : la porte n'a pas été retirée de la liste des portes de l'ancien mur lors du setMurDepart();()";
        assert(porte1.getMurDepart() == murB):"Erreur : le mur A n'a pas été ajouté à la porte";

        porte1.setMurDepart(null, rectangleA);
        assert(porte1.getMurDepart() == null):"Erreur : le mur n'a pas été ajouté à la porte";
    }

    public void testValiderRectangle() {
        try{
            porte1.setRectangle(null);
            porte1.valider();
            fail("Erreur : valider() ne lance pas d'exception quand la porte n'est pas valide");
        }
        catch (ExceptionRectangleNull e) {
            assert(("Le rectangle de la porte "+porte1.getIdPorte()+" est null").equals(e.getMessage())):"Erreur : l'exception n'a pas le bon message d'erreur";
        }
        catch (Exception e){
            fail("Erreur : valider() ne lance pas l'exception ExceptionNombreRectangleInvalide quand le nombre de murs =/= 2");
        }
    }

    public void testValiderOrientation(){
        try{
            porte2 = new Porte(murB, rectangleB, pieceA);
            porte1.setPieceArrivee(pieceB);
            porte1.valider();
        }
        catch (Exception e){
            fail("Erreur : valider() lance une exception sur une porte valide");
        }
    }

    public void testValiderPiecesIdentiques(){
        try {
            porte2 = new Porte(murC, rectangleB, pieceA);
            porte1.valider();
        }
        catch (ExceptionPiecesIdentiques e){
            assert(("Les deux pièces de la porte " + porte1.getIdPorte() + " sont identiques").equals(e.getMessage())):"Erreur : l'exception n'a pas le bon message d'erreur";
        }
        catch (Exception e){
            fail("Erreur : valider() ne lance pas l'exception ExceptionPiecesIdentiques quand les pièces sont identiques");
        }
    }

}