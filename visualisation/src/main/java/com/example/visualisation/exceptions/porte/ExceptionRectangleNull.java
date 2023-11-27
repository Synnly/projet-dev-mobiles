package com.example.visualisation.exceptions.porte;

public class ExceptionRectangleNull extends Exception{
    public ExceptionRectangleNull(int idPorte){
        super("Le rectangle de la porte "+idPorte+" est null");
    }
}
