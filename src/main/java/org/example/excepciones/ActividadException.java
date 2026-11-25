package org.example.excepciones;

public class ActividadException extends RuntimeException{
    public ActividadException(){}

    public ActividadException(String msg){
        super("\u001B[31m"+msg+"\u001B[0m");
    }
}
