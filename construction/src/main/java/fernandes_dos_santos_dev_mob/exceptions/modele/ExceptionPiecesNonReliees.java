package fernandes_dos_santos_dev_mob.exceptions.modele;

public class ExceptionPiecesNonReliees extends Exception{
    public ExceptionPiecesNonReliees(String nomPiece) {
        super("La pièce "+nomPiece+" n'est pas reliée à une autre pièce.");
    }
}
