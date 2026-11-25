package org.example.excepciones;

public class SistemaException extends RuntimeException{
    public SistemaException(){}

    public SistemaException(String msg){
        super("\u001B[31m"+msg+"\u001B[0m");
    }
}
