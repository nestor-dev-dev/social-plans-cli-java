package org.example.controlador;

import org.example.excepciones.UsuarioException;
import org.example.modelo.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.ArrayList;

public class ControladorUsuarios {

    public ControladorUsuarios(){

    }

    public void crearUsr(ArrayList<Usuario> usuarios, String nombre, String contrasenia, int edad, int movil) throws UsuarioException{
        //usuario y movil unicos
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getNombre() == nombre || usuarios.get(i).getMovil() == movil) {
                throw new UsuarioException("El usuario y el telefono movil deben ser unicos");
            }
        }

        //edad entre 14 y 100
        if (edad < 14 || edad > 100) {
            throw new UsuarioException("El usuario debe tener entre 14 y 100 anios");
        }

        //contraseña 3 caracteres
        if (contrasenia.length() < 3) {
            throw new UsuarioException("La contrasenia debe tener al menos 3 caracteres");
        }



        //calcular id
        int id;
        if (usuarios.isEmpty()) {
            id = 1;
        } else {
            id = usuarios.get(usuarios.size() - 1).getId() + 1;
        }

        Usuario usuario = new Usuario(id, nombre, contrasenia, edad, movil);
        usuarios.add(usuario);

    }

    public Usuario logIn(ArrayList<Usuario> usuarios, String nombre, String contrasenia) {
        Usuario usuarioActual = null;
        if (!usuarios.isEmpty()) { //en caso que no hayan usuarios no puedes loguearte
            for (int i = 0; i < usuarios.size(); i++) { //recorre lista de usuarios
                if (usuarios.get(i).getNombre().equals(nombre) && usuarios.get(i).getContrasenia().equals(contrasenia)) {
                    //comprueba que tanto nombre como contraseña coincidan
                    usuarioActual = usuarios.get(i);
                }
            }
        }

        return usuarioActual;
    }

    public Usuario logOut() {
        return new Usuario();
    }

    public String imprimirTodo(ArrayList<Usuario> usuarios, ArrayList<Plan> planes, ArrayList<Actividad> actividades, Usuario usuarioActual) {
        String res = "\n";
        res += "Usuario actual: " + usuarioActual + ";\n";
        res += "Usuarios:\n";
        //recorrer usuarios
        for (int i = 0; i < usuarios.size(); i++) {
            res += "u" + (i + 1) + ": " + usuarios.get(i).toString() + ";\n";
        }

        res += "Planes:\n";
        //recorrer planes
        for (int i = 0; i < planes.size(); i++) {
            res += "p" + (i + 1) + ": " + planes.get(i).toString(usuarioActual.getEdad()) + ";\n";
        }

        res += "Actividades:\n";
        //recorrer actividades
        for (int i = 0; i < actividades.size(); i++) {
            res += "a" + (i + 1) + ": " + actividades.get(i).toString() + ";\n";
        }


        return res;
    }

    public void puntuarPlan(int idPlan, double puntuacion, ArrayList<Plan> planes, Usuario usuarioActual) throws UsuarioException{
        //recorrer los planes
        Plan plan = null;
        for (Plan p : planes){
            //el plan existe
            if(p.getId() == idPlan){
                plan = p;
            }
        }
        if(plan == null){
            throw new UsuarioException("Plan inexistente");
        }

        //buscamos que hayamos estado subscritos
        boolean aux = false;
        for (Usuario u : plan.getParticipantes()){
            if(usuarioActual.getId() == u.getId()){
                aux = true;
            }
        }
        if(!aux){
            throw new UsuarioException("Debemos habernos subscrito al plan para poder puntuarlo");
        }


        //que el plan ya haya concluido
        if(!(plan.getFechaHora().plusMinutes(plan.calcularDuracion())).isBefore(LocalDateTime.now())){
            throw new UsuarioException("El plan debe haber concluido");
        }

        //que no hayamos puesto una nota antes
        aux = false;
        for (Usuario u : plan.getUsuariosQueHanPuntuado()){
            if(usuarioActual.getId() == u.getId()){
                aux = true;
            }
        }
        if(aux){
            throw new UsuarioException("Ya hemos calificado previamente el plan");
        }

        //añadir puntuacion
        if(puntuacion >= 0 && puntuacion <= 10){
            plan.getPuntuaciones().add(puntuacion);
            plan.getUsuariosQueHanPuntuado().add(usuarioActual);
        }else{
            throw new UsuarioException("La calificacion debe estar entre 0 y 10");
        }
    }

    public void puntuarUsuario(double puntuacion, String nombreUsuarioAPuntuar, Usuario usuarioActual, ArrayList<Plan> planes, ArrayList<Usuario> usuarios) throws UsuarioException{
        boolean esPosiblePuntuar = false;
        //1-Buscamos planes a los que hayamos estado subscritos y buscar si estuvimos con el otro usuario
        //recorrer todos los planes
        boolean errorNoEncontrado = true;
        for (Plan p : planes){
            //ver si esuve apuntado
            boolean existo = false;
            for (Usuario u : p.getParticipantes()){
                if(usuarioActual.getId() == u.getId()){
                    existo = true;
                }
            }
            //ver si el otro también lo estuvo
            boolean existeElOtro = false;
            if(existo){
                for (Usuario u : p.getParticipantes()){
                    if(nombreUsuarioAPuntuar.equals(u.getNombre())){
                        existeElOtro = true;
                    }
                }
            }

            //ver si concluyó o está activo
            if(existeElOtro && existo && p.getFechaHora().isBefore(LocalDateTime.now())){
                esPosiblePuntuar = true;
                errorNoEncontrado = false;
            }
        }
        if(errorNoEncontrado){
            throw new UsuarioException("Error: El plan en el que coinciden aun no empezo o ambos usuarios no coinciden en ningun plan o alguno no existe.\n" +
                    " Comprueba tambien que estes logueado, si no no existiras como usuario");
        }

        //2-Ver que la nota esté entre 0 y 10
        if(puntuacion < 0 || puntuacion > 10){
            throw new UsuarioException("Error: La calificacion debe estar entre 0 y 10");
        }


        //3-En caso de encontrar a ambos usuarios y que la nota esté entre 0 y 10, puntuar
        if(esPosiblePuntuar){
            //buscamos al usuario
            for (Usuario u : usuarios){
                if(nombreUsuarioAPuntuar.equals(u.getNombre())){
                    //Ver que no hayamos puntuado antes al mismo usuario
                    boolean hemosPuntuadoAntes = false;
                    for (Usuario puntuador : u.getUsuariosQuePuntuaron()){
                        if(puntuador.getId() == usuarioActual.getId()){
                            hemosPuntuadoAntes=true;
                        }
                    }

                    //Puntuamos
                    if(!hemosPuntuadoAntes){
                        usuarioActual.puntuarUsr(u, puntuacion);
                    }else{
                        throw new UsuarioException("No se puede calificar dos veces al mismo usuario");
                    }
                }
            }
        }else{
            throw new UsuarioException("Error: El plan en el que coinciden aun no empezo o ambos usuarios no coinciden en ningun plan o alguno no existe.\n" +
                    " Comprueba tambien que estes logueado, si no no existiras como usuario");
        }
    }

    public String verPuntuacionUsuario(Usuario usuarioActual){
        return "La puntuacion media es " + usuarioActual.calcularMiMediaPuntuacion() + " y te han puntuado " +
                usuarioActual.getPuntuaciones().size() + " usuarios";
    }
}
