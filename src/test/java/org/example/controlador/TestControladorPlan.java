package org.example.controlador;
import org.example.modelo.*;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;
public class TestControladorPlan {
    @Test
    public void testCrearPlan(){
        ControladorPlan controladorPlan = new ControladorPlan();
        Usuario usuario = new Usuario(1, "Paco", "1234", 24, 666666666);
        ArrayList<Plan> planes = new ArrayList<>();
        controladorPlan.crearPlan(planes, "Plan 1", LocalDateTime.of(3000, 01, 01, 01, 01), "Madrid",
                45, usuario);
        assertEquals(planes.size(), 1);
        assertEquals(planes.get(0).toString(usuario.getEdad()), "id: 1; propietario: Paco; nombre: Plan 1; fecha: 3000-01-01T01:01; lugar: Madrid; aforo: 45;duracion: 0 min; coste: 0.0;puntuacion: null;actividades: ;participantes: ;usuarios que calificaron: ");
    }

    @Test
    public void testFunciones(){
        Actividad a1 = new ActividadTeatro(1, EnumActividad.TEATRO, "ir al teatro", 60, 6, 50, "Ver una obra");
        Usuario usuario = new Usuario(1, "Paco", "1234", 24, 666666666);
        Usuario usuario2 = new Usuario(2, "Ana", "1234", 24, 777777777);
        Plan p1 = new Plan(1, "Plan1", LocalDateTime.of(3000, 01, 01, 01, 01), "Madrid", 1, usuario);
        Plan p2 = new Plan(2, "Plan2", LocalDateTime.of(3000, 01, 01, 01, 01), "Madrid", 1, usuario);
        Plan p3 = new Plan(3, "Plan3", LocalDateTime.of(2000, 01, 01, 01, 01), "Madrid", 1, usuario);
        ArrayList<Actividad> actividades = new ArrayList<>();
        ArrayList<Plan> planes = new ArrayList<>();
        actividades.add(a1);
        planes.add(p1);

        ControladorPlan controladorPlan = new ControladorPlan();

        //1 - añiadir actividad a plan
        try {
            controladorPlan.aniadirActividadPlanPorId(1, 1, actividades, planes, usuario);
        }catch (Exception e){
            fail("no deberia dar una excepcion");
        }

        try {
            controladorPlan.aniadirActividadPlanPorId(1, 100, actividades, planes, usuario);//el plan debe existir
            fail("Deberia dar excepcion");
        }catch (Exception e){}

        try {
            controladorPlan.aniadirActividadPlanPorId(2, 1, actividades, planes, usuario);//la actividad debe existir
            fail("Deberia dar excepcion");
        }catch (Exception e){}

        //2 - apuntarse a plan
        try{
            controladorPlan.apuntarsePlan(1, usuario, planes);
        }catch (Exception e){
            fail("no deberia dar una excepcion");
        }

        try {
            controladorPlan.apuntarsePlan(1, usuario, planes);//no deberíamos poder apuntarnos dos veces
            fail("Deberia dar excepcion");
        }catch (Exception e){}

        try {
            controladorPlan.apuntarsePlan(1, usuario2, planes);//superamos el aforo
            fail("Deberia dar excepcion");
        }catch (Exception e){}

        try {
            controladorPlan.apuntarsePlan(2, usuario, planes);//fecha solapada
            fail("Deberia dar excepcion");
        }catch (Exception e){}

        try {
            controladorPlan.apuntarsePlan(3, usuario, planes);//la fecha ya pasó, nos apuntamos tarde
            fail("Deberia dar excepcion");
        }catch (Exception e){}

        try {
            controladorPlan.apuntarsePlan(10, usuario, planes);//plan no encontrado
            fail("Deberia dar excepcion");
        }catch (Exception e){}


        //3 - Strings: ver cosas
        assertEquals(controladorPlan.verPlanesSuscritos(planes, usuario), "Plan1 ");

        assertEquals(controladorPlan.listarPlanesFecha(planes, usuario), "id: 1; propietario: Paco; nombre: Plan1; fecha: 3000-01-01T01:01; lugar: Madrid; aforo: 1;duracion: 60 min; coste: 3.0;puntuacion: null;actividades: ir al teatro; ;participantes: Paco; ;usuarios que calificaron: \n");

        assertEquals(controladorPlan.listarPlanesPuntuacion(planes, usuario), "null id: 1; propietario: Paco; nombre: Plan1; fecha: 3000-01-01T01:01; lugar: Madrid; aforo: 1;duracion: 60 min; coste: 3.0;puntuacion: null;actividades: ir al teatro; ;participantes: Paco; ;usuarios que calificaron: \n");

        assertEquals(controladorPlan.verPlanesSuscritosConSusActividades(planes, usuario), "Plan1: ir al teatro \n");


        //4 - desapuntarse de plan
        try{
            controladorPlan.desapuntarseDePlan(1, usuario, planes);
        }catch (Exception e){
            fail("no deberia dar una excepcion");
        }

        try{
            controladorPlan.desapuntarseDePlan(1, usuario, planes);//no me puedo desapuntar si no estoy apuntado
            fail("Deberia dar una excepcion");
        }catch (Exception e){}

        try{
            controladorPlan.desapuntarseDePlan(11, usuario, planes);//el plan no existe
            fail("Deberia dar una excepcion");
        }catch (Exception e){}

        //nos apuntamos otra vez
        controladorPlan.apuntarsePlan(1, usuario, planes);
        //hacermos que el plan ya haya empezado
        planes.get(0).setFechaHora(LocalDateTime.of(2000, 01, 01, 01, 01), usuario);

        try{
            controladorPlan.desapuntarseDePlan(11, usuario, planes);//el plan ya ha empezado
            fail("Deberia dar una excepcion");
        }catch (Exception e){}


        //5 - eliminar plan
        try {
            controladorPlan.eliminarPlan(1, planes, usuario);
        }catch (Exception e){
            fail("no deberia dar una excepcion");
        }

        try {
            controladorPlan.eliminarPlan(1, planes, usuario);//el plan no existe
            fail("Deberia dar excepcion");
        }catch (Exception e){}

        try {
            controladorPlan.eliminarPlan(1, planes, usuario2);//debes ser el propietario
            fail("Deberia dar excepcion");
        }catch (Exception e){}


    }

    @Test
    public void testUsuariosPuntuados(){
        ControladorUsuarios controladorUsuarios = new ControladorUsuarios();
        ControladorPlan controladorPlan = new ControladorPlan();
        ArrayList<Plan> planes = new ArrayList<>();
        ArrayList<Usuario> usuarios = new ArrayList<>();

        controladorUsuarios.crearUsr(usuarios, "Paco1", "12345", 45, 1234566789);
        controladorUsuarios.crearUsr(usuarios, "Paco2", "12345", 45, 223466789);
        controladorUsuarios.crearUsr(usuarios, "Paco3", "12345", 45, 334566789);
        controladorUsuarios.crearUsr(usuarios, "Paco4", "12345", 45, 423456789);
        controladorUsuarios.crearUsr(usuarios, "Paco5", "12345", 45, 4234789);
        controladorUsuarios.crearUsr(usuarios, "Paco6", "12345", 45, 423789);

        controladorPlan.crearPlan(planes, "Plan1", LocalDateTime.of(3000, 01, 01, 01, 01), "Madrid", 100, usuarios.get(0));
        controladorPlan.crearPlan(planes, "Plan2", LocalDateTime.of(4000, 01, 01, 01, 01), "Madrid", 10, usuarios.get(0));
        controladorPlan.crearPlan(planes, "Plan3", LocalDateTime.of(4000, 01, 01, 01, 01), "Madrid", 10, usuarios.get(1));

        controladorPlan.apuntarsePlan(1, usuarios.get(0), planes);
        controladorPlan.apuntarsePlan(1, usuarios.get(1), planes);
        controladorPlan.apuntarsePlan(1, usuarios.get(2), planes);
        controladorPlan.apuntarsePlan(1, usuarios.get(3), planes);
        controladorPlan.apuntarsePlan(2, usuarios.get(3), planes);
        controladorPlan.apuntarsePlan(2, usuarios.get(4), planes);

        //hacemos que la fecha del plan sea anterior
        planes.get(0).setFechaHora(LocalDateTime.of(2000, 01, 01, 01, 01), usuarios.get(0));

        //valorar usuario
        controladorUsuarios.puntuarUsuario(5, "Paco2", usuarios.get(0), planes, usuarios);
        controladorUsuarios.puntuarUsuario(10, "Paco2", usuarios.get(1), planes, usuarios);
        controladorUsuarios.puntuarUsuario(0, "Paco2", usuarios.get(2), planes, usuarios);
        controladorUsuarios.puntuarUsuario(6, "Paco1", usuarios.get(2), planes, usuarios);

        //hacemos que la fecha del plan sea posterior
        planes.get(0).setFechaHora(LocalDateTime.of(3000, 01, 01, 01, 01), usuarios.get(0));

        //comprobamos la funcion filtrarPlanes fecha
        assertEquals(controladorPlan.filtrarPlanesFecha(planes, usuarios.get(0),
                LocalDateTime.of(2000, 01, 01, 01, 01),
                LocalDateTime.of(4000, 01, 01, 01, 01)),
                "id: 1; propietario: Paco1; nombre: Plan1; fecha: 3000-01-01T01:01; lugar: Madrid; aforo: 100;duracion: 0 min; coste: " +
                        "0.0;puntuacion: null;actividades: ;participantes: Paco1; Paco2; Paco3; Paco4; ;usuarios que calificaron: \n");


        assertEquals(controladorPlan.filtrarPlanesFecha(planes, usuarios.get(0),
                        LocalDateTime.of(2000, 01, 01, 01, 01),
                        null),
                "id: 1; propietario: Paco1; nombre: Plan1; fecha: 3000-01-01T01:01; lugar: Madrid; aforo: 100;duracion: 0 min; coste: 0.0;puntuacion: null;actividades: ;participantes: Paco1; Paco2; Paco3; Paco4; ;usuarios que calificaron: \n" +
                        "id: 2; propietario: Paco1; nombre: Plan2; fecha: 4000-01-01T01:01; lugar: Madrid; aforo: 10;duracion: 0 min; coste: 0.0;puntuacion: null;actividades: ;participantes: Paco4; Paco5; ;usuarios que calificaron: \n" +
                        "id: 3; propietario: Paco2; nombre: Plan3; fecha: 4000-01-01T01:01; lugar: Madrid; aforo: 10;duracion: 0 min; coste: 0.0;puntuacion: null;actividades: ;participantes: ;usuarios que calificaron: \n");

        assertEquals(controladorPlan.filtrarPlanesFecha(planes, usuarios.get(0),
                        null,
                        LocalDateTime.of(4000, 01, 01, 01, 01)),
                "id: 1; propietario: Paco1; nombre: Plan1; fecha: 3000-01-01T01:01; lugar: Madrid; aforo: 100;duracion: 0 min; coste: " +
                        "0.0;puntuacion: null;actividades: ;participantes: Paco1; Paco2; Paco3; Paco4; ;usuarios que calificaron: \n");


        //filtramos listar planes por puntuacion de dueño
        assertEquals(controladorPlan.listarPlanesPuntuacionDuenio(planes, usuarios.get(0)),
                "Puntuacion del duenio 5.0 id: 3; propietario: Paco2; nombre: Plan3; fecha: 4000-01-01T01:01; lugar: Madrid; aforo: 10;duracion: 0 min; coste: 0.0;puntuacion: null;actividades: ;participantes: ;usuarios que calificaron: \n" +
                        "Puntuacion del duenio 6.0 id: 1; propietario: Paco1; nombre: Plan1; fecha: 3000-01-01T01:01; lugar: Madrid; aforo: 100;duracion: 0 min; coste: 0.0;puntuacion: null;actividades: ;participantes: Paco1; Paco2; Paco3; Paco4; ;usuarios que calificaron: \n" +
                        "Puntuacion del duenio 6.0 id: 2; propietario: Paco1; nombre: Plan2; fecha: 4000-01-01T01:01; lugar: Madrid; aforo: 10;duracion: 0 min; coste: 0.0;puntuacion: null;actividades: ;participantes: Paco4; Paco5; ;usuarios que calificaron: \n");
    }



}
