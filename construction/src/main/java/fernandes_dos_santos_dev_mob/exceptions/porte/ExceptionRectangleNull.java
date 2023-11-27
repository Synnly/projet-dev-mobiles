package fernandes_dos_santos_dev_mob.exceptions.porte;

public class ExceptionRectangleNull extends Exception{
    public ExceptionRectangleNull(int idPorte){
        super("Le rectangle de la porte "+idPorte+" est null");
    }
}
