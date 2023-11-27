package com.example.visualisation.exceptions.piece;

public class ExceptionPiecesReliesParPlusieursMurs extends Exception{
    public ExceptionPiecesReliesParPlusieursMurs(int idPiece1, int idPiece2){
        super("Les pièces "+idPiece1+" et "+idPiece2+" sont reliées par plusieurs murs");
    }
}
