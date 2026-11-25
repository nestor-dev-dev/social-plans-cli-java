package org.example.controlador;

import org.example.excepciones.ActividadException;
import org.example.modelo.*;

import java.util.ArrayList;

public class ControladorActividad {
    public void crearActividad(EnumActividad tipoActividad, String nombre, int duracion, double coste, int aforo, String descripcion, ArrayList<Actividad> actividades) throws ActividadException {


        //calcular nuevo id
        int id = 1;
        if (!actividades.isEmpty()) {
            id = actividades.get(actividades.size() - 1).getId() + 1;
        }

        Actividad nuevaActividad;

        //creamos nueva actividad
        if(tipoActividad.equals(EnumActividad.TEATRO)){
            nuevaActividad = new ActividadTeatro(id, EnumActividad.TEATRO, nombre, duracion, coste, aforo, descripcion);
        } else if (tipoActividad.equals(EnumActividad.CINE)) {
            nuevaActividad = new ActividadCine(id, EnumActividad.CINE, nombre, duracion, coste, aforo, descripcion);
        }else if(tipoActividad.equals(EnumActividad.GENERICA)){
            nuevaActividad = new Actividad(id, EnumActividad.GENERICA, nombre, duracion, coste, aforo, descripcion);
        }else {
            nuevaActividad=null;
        }

        //comprobar que el precio, la duracion y el aforo no son negativo, pero puede ser gratis el precio
        if(coste < 0){
            nuevaActividad=null;
        }

        if(duracion < 0){
            nuevaActividad=null;
        }

        if(aforo < 0){
            nuevaActividad=null;
        }

        if(nuevaActividad != null) {
            actividades.add(nuevaActividad);
        }else{
            throw new ActividadException("No se pudo crear la actividad. Revisa que hayas puesto bien el tipo y que el precio, la duracion y el aforo no sean negativos");
        }
    }
}
