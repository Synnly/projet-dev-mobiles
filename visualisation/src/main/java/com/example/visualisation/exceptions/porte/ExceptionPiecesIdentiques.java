package com.example.visualisation.exceptions.porte;

public class ExceptionPiecesIdentiques extends Exception{
    public ExceptionPiecesIdentiques(int idPorte){
        super("Les deux pièces de la porte "+idPorte+" sont identiques");
    }
}
