package org.example.modelo;

import org.example.controlador.ControladorActividad;
import org.example.controlador.ControladorPlan;
import org.junit.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ActividadTest {
    @Test
    public void testConstructor(){
        Actividad a1 = new ActividadCine(0, EnumActividad.CINE, "ir al cine", 120, 8, 100, "Ver una peli muy chula");
        Actividad a2 = new ActividadTeatro(1, EnumActividad.TEATRO, "ir al teatro", 60, 6, 50, "Ver una obra");

        assertEquals(a1.toString()+"\n"+a2.toString(), "id: 0; nombre: ir al cine; descripcion: Ver una peli muy chula; duracion: 120 min; coste: 8.0€; aforo: 100\n" +
                "id: 1; nombre: ir al teatro; descripcion: Ver una obra; duracion: 60 min; coste: 6.0€; aforo: 50");


    }

    @Test
    public void testPrecioDescontado(){
        Actividad a1 = new ActividadTeatro(1, EnumActividad.TEATRO, "ir al teatro", 60, 6, 50, "Ver una obra");
        Usuario usuario = new Usuario(1, "Paco", "1234", 24, 666666666);
        Plan p1 = new Plan(1, "Plan1", LocalDateTime.of(3000, 01, 01, 01, 01), "Madrid", 10, usuario);
        ArrayList<Actividad> actividades = new ArrayList<>();
        ArrayList<Plan> planes = new ArrayList<>();
        actividades.add(a1);
        planes.add(p1);

        ControladorPlan controladorPlan = new ControladorPlan();

        controladorPlan.aniadirActividadPlanPorId(1, 1, actividades, planes, usuario);
        controladorPlan.apuntarsePlan(1, usuario, planes);

        assertEquals(planes.get(0).calcularPrecioTotal(usuario.getEdad()), (double) 6*0.5, 0.0001);
    }

}
