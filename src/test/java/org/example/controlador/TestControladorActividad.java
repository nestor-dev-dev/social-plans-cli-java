package org.example.controlador;
import org.example.modelo.Actividad;
import org.example.modelo.EnumActividad;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TestControladorActividad {

    @Test
    public void testCrearActicidad(){
        ControladorActividad controladorActividad = new ControladorActividad();
        ArrayList<Actividad> actividades = new ArrayList<>();
        try {
            controladorActividad.crearActividad(EnumActividad.TEATRO, "Ir al teatro", 50, 70, 100, "si", actividades);
        }catch (Exception e){
            fail("No deberia lanzar excepcion");
        }

        assertEquals(actividades.size(), 1);

        try {
            controladorActividad.crearActividad(EnumActividad.TEATRO, "Ir al teatro", -50, 70, 100, "si", actividades);//duracion negativa
            fail("Deberia lanzar excepcion");
        }catch (Exception e){}

        try {
            controladorActividad.crearActividad(EnumActividad.TEATRO, "Ir al teatro", 50, -70, 100, "si", actividades);//coste negativo
            fail("Deberia lanzar excepcion");
        }catch (Exception e){}

        try {
            controladorActividad.crearActividad(EnumActividad.TEATRO, "Ir al teatro", 50, -70, 100, "si", actividades);//aforo negativo
            fail("Deberia lanzar excepcion");
        }catch (Exception e){}
    }
}
