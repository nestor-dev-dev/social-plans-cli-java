package org.example.controlador;

import jdk.nashorn.internal.runtime.ECMAException;
import org.example.modelo.Actividad;
import org.example.modelo.EnumActividad;
import org.example.modelo.Plan;
import org.example.modelo.Usuario;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import static org.junit.Assert.*;

public class TestControladorUsuarios {
    @Test
    public void testFunciones(){

        /*

            LEER PROFESOR IMPORTANTE
            Queríamos usar assertThrows y assertDoesNotThrow pero requería de junit 5, y nosotros tenemos el 4, no sé por qué no nos dejaba agregarlo al pom,
            ni siquiera con ayuda de chat gpt

         */

        //crear usuario
        ControladorUsuarios controladorUsuarios = new ControladorUsuarios();
        ArrayList<Usuario> usuarios = new ArrayList<>();
        try{
            controladorUsuarios.crearUsr(usuarios, "Paco", "1234", 18, 666666666);
        }catch (Exception e){
            fail("No debería lanzar la excepción");
        }

        try {
            controladorUsuarios.crearUsr(usuarios, "Paco", "1235", 19, 667666666);//usuario repetido
            fail("Debería lanzar la excepción");
        }catch (Exception e){}

        try {
            controladorUsuarios.crearUsr(usuarios, "Ana", "1236", 20, 666666666);//telefono repetido
            fail("Debería lanzar la excepción");
        }catch (Exception e){}

        try {
            controladorUsuarios.crearUsr(usuarios, "Ana2", "12", 20, 666666668);//contraseña de 3 caracteres o más
            fail("Debería lanzar la excepción");
        }catch (Exception e){}

        try {
            controladorUsuarios.crearUsr(usuarios, "Ana3", "123", 13, 666666688);//edad mayor o igual a 14 años
            fail("Debería lanzar la excepción");
        }catch (Exception e){}

        try {
            controladorUsuarios.crearUsr(usuarios, "Ana4", "123", 101, 666666888);//edad menor o igual a 100 años
            fail("Debería lanzar la excepción");
        }catch (Exception e){}

        //login
        Usuario usuario = controladorUsuarios.logIn(usuarios, "Paco", "1234");
        assertEquals(usuario, usuarios.get(0));

        //puntuar plan
        ControladorPlan controladorPlan = new ControladorPlan();
        ArrayList<Plan> planes = new ArrayList<>();
        controladorPlan.crearPlan(planes, "Plan 1", LocalDateTime.of(3000, 01, 01, 01, 01), "Madrid",
                45, usuario);
        try {
            controladorUsuarios.puntuarPlan(1, 6.8, planes, usuario);//porque no estamos apuntados
            fail("Debería lanzar la excepción");
        }catch (Exception e){}

        try {
            controladorUsuarios.puntuarPlan(8, 6.8, planes, usuario);//porque el plan no existe
            fail("Debería lanzar la excepción");
        }catch (Exception e){}

        controladorPlan.apuntarsePlan(1, usuario, planes);

        try{
            controladorUsuarios.puntuarPlan(1, 6.8, planes, usuario);//porque no ha concluido
            fail("Debería lanzar la excepción");
        }catch (Exception e){}

        planes.get(0).setFechaHora(LocalDateTime.of(2000, 01, 01, 01, 01), usuario);//para que ya haya concluido y podamos puntuar

        try {
            controladorUsuarios.puntuarPlan(1, 16.8, planes, usuario);//la nota debe ir del 0 al 10
            fail("Debería lanzar la excepción");
        }catch (Exception e){}

        try {
            controladorUsuarios.puntuarPlan(1, 6.8, planes, usuario);
        }catch (Exception e){
            fail("No deberia lanzar excepcion");
        }

        try{
            controladorUsuarios.puntuarPlan(1, 6.8, planes, usuario);//porque ya hemos calificado antes
            fail("Debería lanzar la excepción");
        }catch (Exception e){}


        //imprimir todas las cosas
        ArrayList<Actividad> actividades = new ArrayList<>();
        assertEquals(controladorUsuarios.imprimirTodo(usuarios, planes, actividades, usuario), "\n" +
                "Usuario actual: id: 1; nombre: Paco; edad: 18; movil: 666666666; clave: 1234;\n" +
                "Usuarios:\n" +
                "u1: id: 1; nombre: Paco; edad: 18; movil: 666666666; clave: 1234;\n" +
                "Planes:\n" +
                "p1: id: 1; propietario: Paco; nombre: Plan 1; fecha: 2000-01-01T01:01; lugar: Madrid; aforo: 45;duracion: 0 min; coste: 0.0;puntuacion: 6.8;actividades: ;participantes: Paco; ;usuarios que calificaron: Paco; ;\n" +
                "Actividades:\n");

        //logOut
        Usuario usuarioLogOut = controladorUsuarios.logOut();
        assertEquals(usuarioLogOut, usuarioLogOut);
    }

    @Test
    public void testFunciones2(){
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

        controladorPlan.apuntarsePlan(1, usuarios.get(0), planes);
        controladorPlan.apuntarsePlan(1, usuarios.get(1), planes);
        controladorPlan.apuntarsePlan(1, usuarios.get(2), planes);
        controladorPlan.apuntarsePlan(1, usuarios.get(3), planes);
        controladorPlan.apuntarsePlan(2, usuarios.get(3), planes);
        controladorPlan.apuntarsePlan(2, usuarios.get(4), planes);

        //hacemos que la fecha del plan sea anterior
        planes.get(0).setFechaHora(LocalDateTime.of(2000, 01, 01, 01, 01), usuarios.get(0));

        //valorar usuarios
        controladorUsuarios.puntuarUsuario(5, "Paco2", usuarios.get(0), planes, usuarios);
        controladorUsuarios.puntuarUsuario(10, "Paco2", usuarios.get(1), planes, usuarios);
        controladorUsuarios.puntuarUsuario(0, "Paco2", usuarios.get(2), planes, usuarios);

        try {
            controladorUsuarios.puntuarUsuario(7, "Paco2", usuarios.get(0), planes, usuarios);//no se calificar dos veces al mismo
            fail("Deberia lanzar excepcion");
        }catch (Exception e){}

        try {
            controladorUsuarios.puntuarUsuario(7, "Paco5", usuarios.get(0), planes, usuarios);//porque no están apuntados al mismo plan
            fail("Deberia lanzar excepcion");
        }catch (Exception e){}

        try {
            controladorUsuarios.puntuarUsuario(7, "Paco5", usuarios.get(3), planes, usuarios);//el plan aun no ha empezado
            fail("Deberia lanzar excepcion");
        }catch (Exception e){}

        try {
            controladorUsuarios.puntuarUsuario(7, "Paco6", usuarios.get(0), planes, usuarios);//uno de los dos no esta apuntado
            fail("Deberia lanzar excepcion");
        }catch (Exception e){}

        try {
            controladorUsuarios.puntuarUsuario(-1, "Paco3", usuarios.get(0), planes, usuarios);//la calificaion debe ser mayor o igual a 0
            fail("Deberia lanzar excepcion");
        }catch (Exception e){}

        try {
            controladorUsuarios.puntuarUsuario(10.01, "Paco3", usuarios.get(0), planes, usuarios);//la calificaion debe ser menor o igual a 10
            fail("Deberia lanzar excepcion");
        }catch (Exception e){}

        //view-my-calification
        assertEquals(controladorUsuarios.verPuntuacionUsuario(usuarios.get(1)), "La puntuacion media es 5.0 y te han puntuado 3 usuarios");

        //view-my-plans-activities
        ArrayList<Actividad> actividades = new ArrayList<>();
        ControladorActividad controladorActividad = new ControladorActividad();
        controladorActividad.crearActividad(EnumActividad.TEATRO, "Actividad 1", 10, 60, 1000, "si", actividades);
        controladorActividad.crearActividad(EnumActividad.TEATRO, "Actividad 2", 10, 60, 1000, "si", actividades);
        controladorActividad.crearActividad(EnumActividad.TEATRO, "Actividad 3", 10, 60, 1000, "si", actividades);
        controladorActividad.crearActividad(EnumActividad.TEATRO, "Actividad 4", 10, 60, 1000, "si", actividades);
        controladorActividad.crearActividad(EnumActividad.TEATRO, "Actividad 5", 10, 60, 1000, "si", actividades);

        controladorPlan.aniadirActividadPlanPorId(1, 1, actividades, planes, usuarios.get(0));
        controladorPlan.aniadirActividadPlanPorId(2, 1, actividades, planes, usuarios.get(0));
        controladorPlan.aniadirActividadPlanPorId(3, 1, actividades, planes, usuarios.get(0));
        controladorPlan.aniadirActividadPlanPorId(4, 2, actividades, planes, usuarios.get(0));
        controladorPlan.aniadirActividadPlanPorId(5, 2, actividades, planes, usuarios.get(0));

        assertEquals(controladorPlan.verPlanesSuscritosConSusActividades(planes, usuarios.get(3)), "Plan1: Actividad 1 Actividad 2 Actividad 3 \n" +
                                                                                                            "Plan2: Actividad 4 Actividad 5 \n");

    }
}
