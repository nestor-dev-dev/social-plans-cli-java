package org.example.vista;

import org.example.modelo.Usuario;

import java.util.Scanner;

public class Vista {

    public void lineaDeComandos(Usuario usuario){
        String usuarioNombre = "";
        if(usuario.getNombre() != null){
            usuarioNombre = "-"+usuario.getNombre();
        }
        System.out.print("gps"+usuarioNombre+">");
    }

    public String introducirComando(){
        Scanner sc = new Scanner(System.in);
        String resultado = "";
        try {
            resultado = sc.next();
        }catch (Exception e){
            e.printStackTrace();
        }
        return resultado;
    }

    public void mostrarPosibilidadDeHelp(){
        System.out.println("Escriba help para ver los comandos");
    }

    public void mostrarComandosPosibles(){
        System.out.println("Comandos (No poner ningun espacio en el comando):\n" +
                "view-all = mostrar todo el contenido de la aplicacion\n" +
                "exit = salir\n" +
                "help = ver comandos\n" +
                "login:Nombre;Contrasenia = iniciar sesion\n" +
                "create-user:Nombre;Contrasenia;Edad;Numero-telefono = crear usuario\n" +
                "logout = cerrar sesion\n" +
                "create-plan:NombreEvento;YYYY-MM-DD-HH-MM;Lugar;Aforo = crear un plan\n" +
                "join-plan:id-plan = apuntarse a un plan\n" +
                "undress-plan:id-plan = desapuntarse de un plan\n" +
                "add-activity-plan:id-actividad;id-plan = aniadir actividad a plan\n" +
                "rm-plan:id-plan = eliminar plan\n" +
                "view-my-plans = ver planes a los que estoy subscrito\n" +
                "view-my-plans-activities = ver planes a los que estoy subscrito con las actividades de cada plan\n" +
                "filter-plans-date:YYYY-MM-DD-HH-MM;YYYY-MM-DD-HH-MM = mostrar los planes que comiencen entre estas dos fechas\n"+
                "filter-plans-date:*;YYYY-MM-DD-HH-MM = mostrar los planes que comiencen antes de la fecha\n"+
                "filter-plans-date:YYYY-MM-DD-HH-MM;* = mostrar los planes que comiencen despues de la fecha\n"+
                "list-plans:date = mostrar planes ordenados por fecha\n" +
                "list-plans:puntuation = mostrar planes ordenados por puntuacion\n" +
                "list-plans:owner-calification = mostrar los planes ordenados por la puntuacion del duenio en otros planes\n"+
                "rate-plan:id-plan;puntuacion = puntuar o valorar un plan\n" +
                "rate-user:nombre-usuario;puntuacion = puntuar usuario\n" +
                "view-my-calification = ver la nota que tengo como usuario\n" +
                "create-activity:TipoActividad;Nombre-actividad;Duracion;Coste€;aforo;Descripcion = crear actividad\n" +
                "\t\tTipoActividad = Generica, Cine, Teatro\n");
    }

    public void imprimirString(String s){
        System.out.println(s);
    }

}
