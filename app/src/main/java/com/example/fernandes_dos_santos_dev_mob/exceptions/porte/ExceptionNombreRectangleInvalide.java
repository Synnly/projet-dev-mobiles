package com.example.fernandes_dos_santos_dev_mob.exceptions.porte;

public class ExceptionNombreRectangleInvalide extends Exception{
    public ExceptionNombreRectangleInvalide(int idPorte){
        super("Le nombre de rectangles de la porte "+idPorte+" est invalide (<2)");
    }
}
