package com.example.fernandes_dos_santos_dev_mob.exceptions.porte;

public class ExceptionPiecesIdentiques extends Exception{
    public ExceptionPiecesIdentiques(int idPorte){
        super("Les deux pièces de la porte "+idPorte+" sont identiques");
    }
}
