package fernandes_dos_santos_dev_mob.exceptions.porte;

public class ExceptionOrientationsIncoherentes extends Exception{
    public ExceptionOrientationsIncoherentes(int idPorte){
        super("Les orientations des murs de la porte "+idPorte+" sont incoherentes");
    }
}
