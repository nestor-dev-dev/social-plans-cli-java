package org.example.excepciones;

public class PlanException extends RuntimeException{
    public PlanException(){}

    public PlanException(String msg){
        super("\u001B[31m"+msg+"\u001B[0m");
    }
}
