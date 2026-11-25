package org.example.modelo;


public class Actividad {
    private int id;
    private EnumActividad tipoActividad;
    private String nombre;
    private int duracion;
    private double coste;
    private int aforo; //opcional
    private String descripcion;

    public Actividad(int id, EnumActividad tipoActividad, String nombre, int duracion, double coste, int aforo, String descripcion){
        this.id = id;
        this.tipoActividad = tipoActividad;
        this.nombre = nombre;
        this.duracion = duracion;
        this.coste = coste;
        this.aforo = aforo;
        this.descripcion = descripcion;
    }
    public double calcularPrecioDescontado(int edadUsr){
        return coste;
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

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public double getCoste() {
        return coste;
    }

    public void setCoste(double coste) {
        this.coste = coste;
    }

    public int getAforo() {
        return aforo;
    }

    public void setAforo(int aforo) {
        this.aforo = aforo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EnumActividad getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(EnumActividad tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public String toString() {
        if(this.aforo == -1){
            return "id: "+this.id+"; nombre: "+this.nombre+"; descripcion: " +
                    this.descripcion+"; duracion: "+this.duracion+" min; coste: "+
                    this.coste+"€; aforo: sin limite";
        }else{
            return "id: "+this.id+"; nombre: "+this.nombre+"; descripcion: " +
                    this.descripcion+"; duracion: "+this.duracion+" min; coste: "+
                    this.coste+"€; aforo: "+this.aforo;
        }
    }
}
