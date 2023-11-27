package com.example.visualisation.exceptions.porte;

public class ExceptionPorteSansRetour extends Exception{
    public ExceptionPorteSansRetour(int idPorte){
        super("La porte "+idPorte+" amène à une pièce qui n'a pas de porte permettant de revenir à la pièce d'origine");
    }
}
