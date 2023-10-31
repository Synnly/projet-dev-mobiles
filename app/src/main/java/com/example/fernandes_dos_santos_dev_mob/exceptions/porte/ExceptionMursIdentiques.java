package com.example.fernandes_dos_santos_dev_mob.exceptions.porte;

public class ExceptionMursIdentiques extends Exception{
    public ExceptionMursIdentiques(int idPorte){
        super("Les murs de la porte "+idPorte+" sont identiques");
    }
}
