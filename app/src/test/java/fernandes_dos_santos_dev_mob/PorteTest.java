package fernandes_dos_santos_dev_mob;

import android.graphics.Rect;
import fernandes_dos_santos_dev_mob.donnees.Modele;
import fernandes_dos_santos_dev_mob.donnees.Mur;
import fernandes_dos_santos_dev_mob.donnees.Piece;
import fernandes_dos_santos_dev_mob.donnees.Porte;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionMursIdentiques;
import fernandes_dos_santos_dev_mob.exceptions.porte.ExceptionNombreRectangleInvalide;
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
        murC = new Mur(Mur.EST); murD = new Mur(Mur.OUEST);
        pieceA.ajouterMur(murA); pieceB.ajouterMur(murB);
        pieceC.ajouterMur(murC); pieceD.ajouterMur(murD);
        rectangleA = new Rect(0, 0, 0, 0);
        rectangleB = new Rect(10, 10, 10, 10);
        rectangleC = new Rect(10, 10, 10, 10);
        rectangleD = new Rect(10, 10, 10, 10);
        porte1 = new Porte(murA, rectangleA);
    }

    public void testSetMurA() {
        assert(murA.getListePortes().get(0) == porte1): "Erreur : la porte n'a pas été ajoutée à la liste des portes du mur lors de l'instanciation";
        assert(porte1.getMurA() == murA):"Erreur : le mur A n'a pas été ajouté à la porte";

        porte1.setMurA(murB, rectangleB);

        assert(murB.getListePortes().get(0) == porte1): "Erreur : la porte n'a pas été ajoutée à la liste des portes du mur lors du setMurA()";
        assert(murA.getListePortes().size() == 0): "Erreur : la porte n'a pas été retirée de la liste des portes de l'ancien mur lors du setMurA()";
        assert(porte1.getMurA() == murB):"Erreur : le mur A n'a pas été ajouté à la porte";

        porte1.setMurA(null, rectangleA);
        assert(porte1.getMurA() == null):"Erreur : le mur n'a pas été ajouté à la porte";
    }

    public void testSetMurB() {
        porte1.setMurB(murB, rectangleB);
        assert(porte1.getMurB() == murB):"Erreur : le mur B n'a pas été ajouté à la porte";

        porte1.setMurB(murC, rectangleC);

        assert(murC.getListePortes().get(0) == porte1): "Erreur : la porte n'a pas été ajoutée à la liste des portes du mur lors du setMurB()";
        assert(murB.getListePortes().size() == 0): "Erreur : la porte n'a pas été retirée de la liste des portes de l'ancien mur lors du setMurB()";
        assert(porte1.getMurB() == murC):"Erreur : le mur B n'a pas été ajouté à la porte";

        porte1.setMurB(null, rectangleA);
        assert(porte1.getMurB() == null):"Erreur : le mur n'a pas été ajouté à la porte";
    }

    public void testGetRectangle() {
        porte2 = new Porte(murB, rectangleB);
        assert(rectangleA == (porte1.getRectangle(murA))):"Erreur : getRectangle() ne renvoie pas le bon rectangle";
        assert(rectangleB == (porte2.getRectangle(murB))):"Erreur : getRectangle() ne renvoie pas le bon rectangle";
        assert(porte1.getRectangle(murC) == null):"Erreur : getRectangle() ne renvoie pas null quand la porte n'est pas sur le mur en parametre";
    }

    public void testSetRectangle() {
        porte1 = new Porte(murA, rectangleA);
        porte1.setMurB(murB, null);
        porte1.setRectangle(murB, rectangleB);

        porte2 = new Porte(murC, rectangleC);
        porte2.setMurB(murD, null);
        porte2.setRectangle(murD, rectangleD);

        assert(rectangleA.top == (porte1.getRectangle(murA).top)):"Erreur : getRectangle() ne renvoie pas le bon rectangle";
        assert(rectangleB == (porte1.getRectangle(murB))):"Erreur : setRectangle() ne modifie pas le rectangle du mur correspondant";
        assert(rectangleC == (porte2.getRectangle(murC))):"Erreur : getRectangle() ne renvoie pas le bon rectangle";
        assert(rectangleD == (porte2.getRectangle(murD))):"Erreur : setRectangle() ne modifie pas le rectangle du mur correspondant";

        porte1.setRectangle(murC, rectangleC);
        assert(rectangleA == (porte1.getRectangle(murA))):"Erreur : setRectangle() modifie le rectangle du mur A alors que la porte n'est pas sur le mur entré en parametre";
        assert(rectangleB == (porte1.getRectangle(murB))):"Erreur : setRectangle() modifie le rectangle du mur A alors que la porte n'est pas sur le mur entré en parametre";
        assert(rectangleC != (porte1.getRectangle(murC))):"Erreur : setRectangle() modifie le rectangle du mur A alors que la porte n'est pas sur le mur entré en parametre";
    }

    public void testValiderRectangle() {

        try{
            porte1.valider();
            fail("Erreur : valider() ne lance pas d'exception quand la porte n'est pas valide");
        }
        catch (ExceptionNombreRectangleInvalide e) {
            assert(("Le nombre de rectangles de la porte " + porte1.getIdPorte() + " est invalide (<2)").equals(e.getMessage())):"Erreur : l'exception n'a pas le bon message d'erreur";
        }
        catch (Exception e){
            fail("Erreur : valider() ne lance pas l'exception ExceptionNombreRectangleInvalide quand le nombre de murs =/= 2");
        }
    }

    public void testValiderMurs(){
        porte1.setMurB(murA, rectangleB);

        try{
            porte1.valider();
            fail("Erreur : valider() ne lance pas d'exception quand la porte n'est pas valide");
        }
        catch (ExceptionMursIdentiques e) {
            assert(("Les murs de la porte "+porte1.getIdPorte()+" sont identiques").equals(e.getMessage())):"Erreur : l'exception n'a pas le bon message d'erreur";
        }
        catch (Exception e){
            fail("Erreur : valider() ne lance pas l'exception ExceptionMursIdentiques quand les murs sont identiques");
        }
    }

    public void testValiderOrientationCorrect(){
        try{
            porte1.setMurB(murB, rectangleB);
            System.out.println(porte1.getMurA().getIdMur());
            System.out.println(porte1.getMurB().getIdMur());
            System.out.println(porte1.getMurA().getPiece().getIdPiece());
            System.out.println(porte1.getMurB().getPiece().getIdPiece());
            porte1.valider();
        }
        catch (Exception e){
            fail("Erreur : valider() lance une exception sur une porte valide");
        }

        try{
            porte1.setMurA(murB, rectangleB);
            porte1.setMurB(murA, rectangleA);
            porte1.valider();
        }
        catch (Exception e){
            fail("Erreur : valider() lance une exception sur une porte valide");
        }

        try{
            porte1.setMurA(murC, rectangleC);
            porte1.setMurB(murD, rectangleD);
            porte1.valider();
        }
        catch (Exception e){
            fail("Erreur : valider() lance une exception sur une porte valide");
        }

        try{
            porte1.setMurA(murC, rectangleC);
            porte1.setMurB(murD, rectangleD);
            porte1.valider();
        }
        catch (Exception e){
            fail("Erreur : valider() lance une exception sur une porte valide");
        }
    }

    public void testValiderOrientationIncorrectNE(){
        try{
            porte1.setMurB(murC, rectangleC);
            porte1.valider();
        }
        catch (ExceptionOrientationsIncoherentes e){
            assert(("Les orientations des murs de la porte " + porte1.getIdPorte() + " sont incoherentes").equals(e.getMessage())):"Erreur : l'exception n'a pas le bon message d'erreur";
        }
        catch (Exception e){
            fail("Erreur : valider() ne lance pas l'exception ExceptionOrientationsIncoherentes quand les murs ne sont pas adjacents");
        }

        try{
            porte1 = new Porte(murC, rectangleC);
            porte1.setMurB(murA, rectangleA);
            porte1.valider();
        }
        catch (ExceptionOrientationsIncoherentes e){
            assert(("Les orientations des murs de la porte " + porte1.getIdPorte() + " sont incoherentes").equals(e.getMessage())):"Erreur : l'exception n'a pas le bon message d'erreur";
        }
        catch (Exception e){
            fail("Erreur : valider() ne lance pas l'exception ExceptionOrientationsIncoherentes quand les murs ne sont pas adjacents");
        }
    }

    public void testValiderOrientationIncorrectOS(){
        try{
            porte1 = new Porte(murB, rectangleB);
            porte1.setMurB(murD, rectangleD);
            porte1.valider();
        }
        catch (ExceptionOrientationsIncoherentes e){
            assert(("Les orientations des murs de la porte " + porte1.getIdPorte() + " sont incoherentes").equals(e.getMessage())):"Erreur : l'exception n'a pas le bon message d'erreur";
        }
        catch (Exception e){
            fail("Erreur : valider() ne lance pas l'exception ExceptionOrientationsIncoherentes quand les murs ne sont pas adjacents");
        }

        try{
            porte1 = new Porte(murD, rectangleD);
            porte1.setMurB(murB, rectangleB);
            porte1.valider();
        }
        catch (ExceptionOrientationsIncoherentes e){
            assert(("Les orientations des murs de la porte " + porte1.getIdPorte() + " sont incoherentes").equals(e.getMessage())):"Erreur : l'exception n'a pas le bon message d'erreur";
        }
        catch (Exception e){
            fail("Erreur : valider() ne lance pas l'exception ExceptionOrientationsIncoherentes quand les murs ne sont pas adjacents");
        }
    }

    public void testValiderOrientationIncorrectON(){
        try{
            porte1.setMurB(murD, rectangleD);
            porte1.valider();
        }
        catch (ExceptionOrientationsIncoherentes e){
            assert(("Les orientations des murs de la porte " + porte1.getIdPorte() + " sont incoherentes").equals(e.getMessage())):"Erreur : l'exception n'a pas le bon message d'erreur";
        }
        catch (Exception e){
            fail("Erreur : valider() ne lance pas l'exception ExceptionOrientationsIncoherentes quand les murs ne sont pas adjacents");
        }

        try{
            porte1 = new Porte(murD, rectangleD);
            porte1.setMurB(murA, rectangleA);
            porte1.valider();
        }
        catch (ExceptionOrientationsIncoherentes e){
            assert(("Les orientations des murs de la porte " + porte1.getIdPorte() + " sont incoherentes").equals(e.getMessage())):"Erreur : l'exception n'a pas le bon message d'erreur";
        }
        catch (Exception e){
            fail("Erreur : valider() ne lance pas l'exception ExceptionOrientationsIncoherentes quand les murs ne sont pas adjacents");
        }
    }

    public void testValiderOrientationIncorrectSE(){
        try{
            porte1 = new Porte(murB, rectangleB);
            porte1.setMurB(murC, rectangleC);
            porte1.valider();
        }
        catch (ExceptionOrientationsIncoherentes e){
            assert(("Les orientations des murs de la porte " + porte1.getIdPorte() + " sont incoherentes").equals(e.getMessage())):"Erreur : l'exception n'a pas le bon message d'erreur";
        }
        catch (Exception e){
            fail("Erreur : valider() ne lance pas l'exception ExceptionOrientationsIncoherentes quand les murs ne sont pas adjacents");
        }

        try{
            porte1 = new Porte(murC, rectangleC);
            porte1.setMurB(murB, rectangleB);
            porte1.valider();
        }
        catch (ExceptionOrientationsIncoherentes e){
            assert(("Les orientations des murs de la porte " + porte1.getIdPorte() + " sont incoherentes").equals(e.getMessage())):"Erreur : l'exception n'a pas le bon message d'erreur";
        }
        catch (Exception e){
            fail("Erreur : valider() ne lance pas l'exception ExceptionOrientationsIncoherentes quand les murs ne sont pas adjacents");
        }
    }

    public void testValiderPiecesIdentiques(){
        murB = new Mur(Mur.SUD);
        porte1.setMurB(murB, rectangleB);
        try {
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