package org.example.modelo;

public class ActividadTeatro extends Actividad{
    public ActividadTeatro(int id, EnumActividad tipoActividad, String nombre, int duracion, double coste, int aforo, String descripcion){
        super(id, tipoActividad, nombre, duracion, coste, aforo, descripcion);
    }

    public double calcularPrecioDescontado(int edadUsr){
        if(edadUsr<25){ //menos de 25 hace el 50% de descuento
            return this.getCoste()-this.getCoste()*0.5;
        }else if(edadUsr>65){ //mas de 65 hace el 70% de descuento
            return this.getCoste()-this.getCoste()*0.7;
        }else{
            return this.getCoste();
        }
    }
}
