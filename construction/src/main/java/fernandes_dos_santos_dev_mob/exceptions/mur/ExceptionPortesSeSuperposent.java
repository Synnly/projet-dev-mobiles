package fernandes_dos_santos_dev_mob.exceptions.mur;

public class ExceptionPortesSeSuperposent extends Exception{
    public ExceptionPortesSeSuperposent(int idPorte1, int idPorte2){
        super("Erreur : les portes "+idPorte1+" et "+idPorte2+" se superposent");
    }
}
