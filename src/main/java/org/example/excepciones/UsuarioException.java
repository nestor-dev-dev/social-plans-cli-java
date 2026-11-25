package org.example.excepciones;

public class UsuarioException extends RuntimeException{
    public UsuarioException(){}

    public UsuarioException(String msg){
        super("\u001B[31m"+msg+"\u001B[0m");
    }
}
