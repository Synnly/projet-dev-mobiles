package com.example.fernandes_dos_santos_dev_mob.exceptions.piece;

public class ExceptionNombreMursInvalide extends Exception{
    public ExceptionNombreMursInvalide(String nomPiece){
        super("Le nombre de murs de la piece "+nomPiece+" est invalide (<4)");
    }
}
