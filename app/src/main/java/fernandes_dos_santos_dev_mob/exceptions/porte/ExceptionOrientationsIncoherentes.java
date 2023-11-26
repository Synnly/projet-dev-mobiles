package fernandes_dos_santos_dev_mob.exceptions.porte;

public class ExceptionOrientationsIncoherentes extends Exception{
    public ExceptionOrientationsIncoherentes(int idPorte){
        super("Il n'y a pas de porte d'orientation opposee a la porte "+idPorte+" dans la piece d'arrivee");
    }
}
