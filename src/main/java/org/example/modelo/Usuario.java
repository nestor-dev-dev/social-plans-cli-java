package org.example.modelo;

import java.util.ArrayList;

public class Usuario {
    private int id;
    private String nombre;
    private String contrasenia;
    private int edad;
    private int movil;
    private ArrayList<Double> puntuaciones;
    private ArrayList<Usuario> usuariosQuePuntuaron;

    public Usuario(){

    }

    public Usuario(int id, String nombre, String contrasenia, int edad, int movil) {
        this.id = id;
        this.nombre = nombre;
        this.contrasenia = contrasenia;
        this.edad = edad;
        this.movil = movil;
        this.puntuaciones = new ArrayList<>();
        this.usuariosQuePuntuaron = new ArrayList<>();
    }

    public Double calcularMiMediaPuntuacion(){
        if(puntuaciones.isEmpty()){
            return null;
        }else{
            double sum=0;
            for(int i=0; i<puntuaciones.size(); i++){
                sum+=puntuaciones.get(i);
            }
            return sum/puntuaciones.size();
        }
    }

    public void puntuarUsr(Usuario usuario, double nota){
        //agregamos la nota
        ArrayList<Double> aux = usuario.getPuntuaciones();
        aux.add(nota);
        usuario.setPuntuaciones(aux);


        //nos agregamos como puntuadores
        usuario.getUsuariosQuePuntuaron().add(this);
    }

    public Double verPuntuacionMedia(Usuario usuario){
        return usuario.calcularMiMediaPuntuacion();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public int getMovil() {
        return movil;
    }

    public void setMovil(int movil) {
        this.movil = movil;
    }

    public ArrayList<Double> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuaciones(ArrayList<Double> puntuaciones) {
        this.puntuaciones = puntuaciones;
    }

    public ArrayList<Usuario> getUsuariosQuePuntuaron() {
        return usuariosQuePuntuaron;
    }

    public void setUsuariosQuePuntuaron(ArrayList<Usuario> usuariosQuePuntuaron) {
        this.usuariosQuePuntuaron = usuariosQuePuntuaron;
    }

    @Override
    public String toString() {
        return "id: "+this.id+"; nombre: "+this.nombre+"; edad: "+this.edad+
                "; movil: "+this.movil+"; clave: "+this.contrasenia;
    }
}
