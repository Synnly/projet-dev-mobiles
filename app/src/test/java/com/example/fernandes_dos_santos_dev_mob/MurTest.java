package com.example.fernandes_dos_santos_dev_mob;

import android.graphics.Rect;
import com.example.fernandes_dos_santos_dev_mob.donnees.*;
import com.example.fernandes_dos_santos_dev_mob.exceptions.mur.ExceptionPortesSeSuperposent;
import com.example.fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionNombreRectangleInvalide;
import com.example.fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionOrientationsIncoherentes;
import junit.framework.TestCase;

public class MurTest extends TestCase {
    private Modele modele;
    private Piece piece1, piece2;
    private Mur mur1, mur2;
    private Porte porte1, porte2, port3;
    private Rect rect1, rect2, rect3;

    public void setUp() throws Exception {
        super.setUp();
        modele = new Modele();
        piece1 = new Piece(modele);

        rect1 = new Rect(0, 0, 30, 40);
        rect2 = new Rect(20, 0, 50, 40);
        rect3 = new Rect(40, 0, 70, 40);
        mur1 = new Mur(piece1, Mur.NORD);
        mur2 = new Mur(piece2, Mur.SUD);
    }

    public void testMur() {
        mur1 = new Mur(piece1, Mur.NORD);
        assert(mur1.getOrientation() == Mur.NORD):"Erreur : le constructeur de Mur n'assigne pas une orientation valide";

        mur1 = new Mur(piece1, Mur.EST);
        assert(mur1.getOrientation() == Mur.EST):"Erreur : le constructeur de Mur n'assigne pas une orientation valide";

        mur1 = new Mur(piece1, Mur.SUD);
        assert(mur1.getOrientation() == Mur.SUD):"Erreur : le constructeur de Mur n'assigne pas une orientation valide";

        mur1 = new Mur(piece1, Mur.OUEST);
        assert(mur1.getOrientation() == Mur.OUEST):"Erreur : le constructeur de Mur n'assigne pas une orientation valide";

        mur1 = new Mur(piece1, -1);
        assert(mur1.getOrientation() == Mur.NORD):"Erreur : le constructeur de Mur n'assigne pas l'orientation Nord quand l'orientation est invalide";

        mur1 = new Mur(piece1, -1);
        assert(mur1.getOrientation() == Mur.NORD):"Erreur : le constructeur de Mur n'assigne pas l'orientation Nord quand l'orientation est invalide";

        mur1 = new Mur(piece1, 4);
        assert(mur1.getOrientation() == 0):"Erreur : le constructeur de Mur n'assigne pas l'orientation Nord quand l'orientation est invalide";

        mur1 = new Mur(piece1, 5);
        assert(mur1.getOrientation() == 0):"Erreur : le constructeur de Mur n'assigne pas l'orientation Nord quand l'orientation est invalide";
    }

    public void testValiderMurVide() {
        try {
            mur1 = new Mur(piece1, 0);
            mur1.valider();
        } catch (Exception e) {
            fail("Erreur : valider() renvoie une exception alors que le mur est valide");
        }

        try {
            mur1 = new Mur(piece1, 4);
            mur1.valider();
        } catch (Exception e) {
            fail("Erreur : valider() renvoie une exception alors que le mur est valide");
        }

        try{
            mur1 = new Mur(piece1, 0);
            mur2 = new Mur(piece1, 1);
            mur1.valider();
        }
        catch (Exception e){
            fail("Erreur : valider() renvoie une exception alors que le mur est valide");
        }
    }

    public void testValiderPortesSuperposent(){
        porte1 = new Porte(mur1, rect1);
        porte1.setMurB(mur2, rect1);
        try {
            porte2 = new Porte(mur1, rect2);
            porte2.setMurB(mur2, rect2);
            mur1.valider();
        }
        catch (ExceptionPortesSeSuperposent e){
            assert(("Erreur : les portes "+porte1.getIdPorte()+" et "+porte2.getIdPorte()+" se superposent").equals(e.getMessage())):"Erreur : le message de l'exception n'est pas correct";
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            fail("Erreur : valider() ne renvoie pas l'exception ExceptionPortesSeSuperposent alors que les portes se superposent");
        }

        try {
            porte2 = new Porte(mur1, rect3);
            porte2.setMurB(mur2, rect3);
            mur1.valider();
        }
        catch (Exception e){
            fail("Erreur : valider() renvoie une exception alors que le mur est valide");
        }
    }
}
