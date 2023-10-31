package com.example.fernandes_dos_santos_dev_mob.exceptions.modele;

public class ExceptionPieceInvalide extends Exception{
    public ExceptionPieceInvalide(String nomPiece) {
        super("La pièce "+nomPiece+" n'est pas valide");
    }
}
