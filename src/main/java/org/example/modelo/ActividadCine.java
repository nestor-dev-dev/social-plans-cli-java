package org.example.modelo;

public class ActividadCine extends Actividad{
    public ActividadCine(int id, EnumActividad tipoActividad, String nombre, int duracion, double coste, int aforo, String descripcion){
        super(id, tipoActividad, nombre, duracion, coste, aforo, descripcion);
    }

    public double calcularPrecioDescontado(int edadUsr){
        if(edadUsr<21){ //si tiene menos de 21 hace el 50% de descuento
            return this.getCoste()-this.getCoste()*0.5;
        }else{
            return this.getCoste();
        }
    }
}
