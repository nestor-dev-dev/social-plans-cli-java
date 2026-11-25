package org.example.controlador;


import org.example.excepciones.ActividadException;
import org.example.excepciones.PlanException;
import org.example.excepciones.SistemaException;
import org.example.excepciones.UsuarioException;
import org.example.modelo.Actividad;
import org.example.modelo.EnumActividad;
import org.example.modelo.Plan;
import org.example.modelo.Usuario;
import org.example.vista.Vista;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;

public class Sistema {
    private ArrayList<Usuario> usuarios;
    private ArrayList<Actividad> actividades;
    private ArrayList<Plan> planes;
    private Usuario usuarioActual;
    private ControladorUsuarios controladorUsuarios;
    private Vista vista;
    private ControladorPlan controladorPlan;

    private ControladorActividad controladorActividad;



    public Sistema(){
        this.usuarios=new ArrayList<>();
        this.actividades= new ArrayList<>();
        this.planes = new ArrayList<>();
        this.usuarioActual = new Usuario();
        controladorUsuarios = new ControladorUsuarios();
        vista = new Vista();
        controladorPlan = new ControladorPlan();
        controladorActividad = new ControladorActividad();
    }

    public static void main(String[] args) {
        Sistema sistema = new Sistema();
        sistema.poblador();
        sistema.realizarAccion();
    }

    private void realizarAccion(){
        usuarioActual = new Usuario();
        vista.mostrarPosibilidadDeHelp();
        boolean seguir = true;
        while (seguir){
            vista.lineaDeComandos(usuarioActual);
            String comando = vista.introducirComando();
            try{
                seguir = actuar(comando);
            }catch (Exception e){
                vista.imprimirString(e.getMessage());
            }
        }
    }

    private boolean actuar(String comando) throws SistemaException, PlanException, UsuarioException, ActividadException {
        boolean seguir = true;
        try {
            if(comando.equals("exit")){
                seguir = false;
            }else if(comando.equals("help")){
                vista.mostrarComandosPosibles();
            }else if (comando.startsWith("login:")) {
                login(comando);
            }else if (comando.startsWith("create-user:")) {
                createUser(comando);
            }else if (comando.equals("logout")) {
                usuarioActual = controladorUsuarios.logOut();
            }else if (comando.startsWith("join-plan:")) {
                joinPlan(comando);
            }else if (comando.startsWith("undress-plan:")) {
                undressPlan(comando);
            }else if (comando.startsWith("create-plan:")) {
                createPlan(comando);
            }else if (comando.startsWith("add-activity-plan:")) {
                aniadirActividadPlan(comando);
            }else if (comando.equals("view-my-plans")) {
                vista.imprimirString(controladorPlan.verPlanesSuscritos(planes, usuarioActual));
            }else if (comando.equals("view-my-plans-activities")) {
                vista.imprimirString(controladorPlan.verPlanesSuscritosConSusActividades(planes, usuarioActual));
            }else if (comando.equals("list-plans:date")) {
                vista.imprimirString(controladorPlan.listarPlanesFecha(planes, usuarioActual));
            }else if (comando.equals("list-plans:puntuation")) {
                vista.imprimirString(controladorPlan.listarPlanesPuntuacion(planes, usuarioActual));
            }else if (comando.equals("list-plans:owner-calification")) {
                vista.imprimirString(controladorPlan.listarPlanesPuntuacionDuenio(planes, usuarioActual));
            }else if (comando.startsWith("create-activity:")) {
                createActivity(comando);
            }else if (comando.equals("view-all")) {
                vista.imprimirString(controladorUsuarios.imprimirTodo(usuarios, planes, actividades, usuarioActual));
            }else if (comando.startsWith("rm-plan:")) {
                borrarPlan(comando);
            }else if (comando.startsWith("rate-plan:")) {
                valorarPlan(comando);
            }else if (comando.startsWith("rate-user:")) {
                valorarUsuario(comando);
            }else if (comando.equals("view-my-calification")) {
                verCalificaionUsuario(comando);
            }else if (comando.startsWith("filter-plans-date:")) {
                filtrarPlanesFecha(comando);
            }else{
                throw new SistemaException("No se encontro el comando ["+comando+"] o estan mal introducidos los datos");
            }
        }catch (NumberFormatException e){
            throw new SistemaException("Error: se esperaba un formato numérico determinado y se encontró otro formato diferente en "+ comando + "\n" +
                    "usa el comando help para ver el formato");
        }catch (DateTimeException e){
            throw new SistemaException("Error: la fecha del comando "+ comando + " es incorrecta o inexistente, usa el comando help para ver el formato correcto");
        }catch (UsuarioException | ActividadException | PlanException e){
            throw e;
        }catch (NullPointerException e){
            throw new SistemaException("Error: Se opera con un null");
        }catch (ArithmeticException e){
            throw new SistemaException("Error: se dividió por 0");
        }catch (IndexOutOfBoundsException e){
            throw new SistemaException("Error: Out of bounds");
        }catch (Exception e){
            throw new SistemaException( "No se encontro el comando ["+comando+"] o estan mal introducidos los datos");
        }

        return seguir;
    }

    private void login(String comando) throws Exception{
        int index = comando.indexOf(":") + 1;//calculamos cuantos caracteres hay hasta el primer ":" y le sumamos el caracter :
        if (index == 0){
            throw new SistemaException("Error de formato, no pusiste los :");//si no encuentra los :, lamzamos excepcion
        }

        String[] partes = comando.substring(index).split(";");
        //a partir del caracter 6, mete todos los subStrings que esten separados por ; en el array partes
        if((partes.length == 2)){ //verifica que solo haya metido dos variables
            usuarioActual = controladorUsuarios.logIn(usuarios, partes[0], partes[1]);// loguea al usuario con su nombre que es partes[0] y contraseña que es la 1
            if(usuarioActual == null){ //en el caso que no se encuentre dicho usuario
                usuarioActual = new Usuario();
                throw new UsuarioException("Error: usuario y/o contrasenia incorrectos"); //lanza excepcion de Usuario
            }
        }else{
            throw new Exception();
        }
    }

    private void createUser(String comando) throws UsuarioException{
        int index = comando.indexOf(":") + 1;
        if (index == 0){
            throw new SistemaException("Error de formato, no pusiste los :");
        }

        String[] partes = comando.substring(index).split(";");
            if (!(partes.length == 4)) { //a falta o sobrante de datos lanzara la siguiente excepcion
                throw new UsuarioException("Error, datos de usuario incorrectos\n" +
                        "\tRecuerda que el usuario y el numero de telefono deben ser unicos\n" +
                        "\tque la edad debe estar entre 14 y 100 anios y que\n" +
                        "\tla contrasenia debe tener al menos 3 caracteres");
            }else{ //crea el usuario
                controladorUsuarios.crearUsr(usuarios, partes[0], partes[1], Integer.parseInt(partes[2]), Integer.parseInt(partes[3]));
            }
    }

    private void createPlan(String comando) throws PlanException{
        int index = comando.indexOf(":") + 1;
        if (index == 0){
            throw new SistemaException("Error de formato, no pusiste los :");
        }


        String[] partes = comando.substring(index).split(";");
        if(usuarioActual.getNombre() != null && (partes.length == 4)){
            //extraer fecha
            String[] fechaString = partes[1].substring(0).split("-");
            LocalDateTime fecha = LocalDateTime.of(Integer.parseInt(fechaString[0]), Integer.parseInt(fechaString[1]), Integer.parseInt(fechaString[2]),
                    Integer.parseInt(fechaString[3]), Integer.parseInt(fechaString[4]));

            Arrays.toString(fechaString);

            controladorPlan.crearPlan(planes, partes[0], fecha, partes[2], Integer.parseInt(partes[3]), usuarioActual);
        }else{
            throw new PlanException("Error, datos de plan incorrectos o incompletos (revista el formato escribiendo help)\n"+"Necesitas estar registrado para crear un plan");
        }
    }

    private void joinPlan(String comando) throws PlanException{
        int index = comando.indexOf(":") + 1;
        if (index == 0){
            throw new SistemaException("Error de formato, no pusiste los :");
        }

        String[] partes = comando.substring(index).split(";");
            if(partes.length != 1){
                throw new PlanException("Error, datos de plan incorrectos o incompletos (revista el formato escribiendo help)\n"+
                        "Para apuntarse o desapuntarse a un plan, el plan no debe haber comenzado\n"+
                        "Puede que ya estuvieras apuntado al plan");
            }else{
                controladorPlan.apuntarsePlan(Integer.parseInt(partes[0]), usuarioActual, planes);
            }
    }

    private void undressPlan(String comando) throws PlanException{
        int index = comando.indexOf(":") + 1;
        if (index == 0){
            throw new SistemaException("Error de formato, no pusiste los :");
        }

        String[] partes = comando.substring(index).split(";");
            if(partes.length != 1){
                throw new PlanException("Error, datos de plan incorrectos o incompletos (revista el formato escribiendo help)\n");
            }else{
                controladorPlan.desapuntarseDePlan(Integer.parseInt(partes[0]), usuarioActual, planes);
            }
    }

    private void createActivity(String comando) throws ActividadException{
        int index = comando.indexOf(":") + 1;
        if (index == 0){
            throw new SistemaException("Error de formato, no pusiste los :");
        }

        String[] partes = comando.substring(index).split(";");
            if(!(partes.length == 6)){
                throw new ActividadException("Error, datos de actividad incorrectos o incompletos (revista el formato escribiendo help)\n");
            }else{
                controladorActividad.crearActividad(EnumActividad.valueOf(partes[0].toUpperCase()), partes[1], Integer.parseInt(partes[2]),
                        Double.parseDouble(partes[3]), Integer.parseInt(partes[4]), partes[5], actividades);
            }
    }

    private void aniadirActividadPlan(String comando) throws PlanException{
        int index = comando.indexOf(":") + 1;
        if (index == 0){
            throw new SistemaException("Error de formato, no pusiste los :");
        }

        String[] partes = comando.substring(index).split(";");
            if(!(partes.length == 2) ){
                throw new PlanException("Error, datos de actividad o de plan incorrectos o incompletos (revista el formato escribiendo help)\n");
            }else{
                controladorPlan.aniadirActividadPlanPorId(Integer.parseInt(partes[0]), Integer.parseInt(partes[1]), actividades, planes, usuarioActual);
            }
    }

    private void borrarPlan(String comando) throws PlanException{
        int index = comando.indexOf(":") + 1;
        if (index == 0){
            throw new SistemaException("Error de formato, no pusiste los :");
        }


        String[] partes = comando.substring(index).split(";");
            if(!(partes.length == 1)){
                throw new PlanException("Error, ID no encontrada");
            }else{
                controladorPlan.eliminarPlan(Integer.parseInt(partes[0]), planes, usuarioActual);
            }
    }

    private void valorarPlan(String comando) throws PlanException{
        int index = comando.indexOf(":") + 1;
        if (index == 0){
            throw new SistemaException("Error de formato, no pusiste los :");
        }

        String[] partes = comando.substring(index).split(";");
            if(!(partes.length == 2)){
                throw new PlanException("Error, revisa que:\n" +
                        "\t1-Que estes subscrito al plan\n" +
                        "\t2-Que no hayas valorado antes el plan\n" +
                        "\t3-Que el plan haya finalizado");
            }else{
                controladorUsuarios.puntuarPlan(Integer.parseInt(partes[0]), Double.parseDouble(partes[1]), planes, usuarioActual);
            }
    }

    private void valorarUsuario(String comando) throws Exception{
        int index = comando.indexOf(":") + 1;
        if (index == 0){
            throw new SistemaException("Error de formato, no pusiste los :");
        }

        String[] partes = comando.substring(index).split(";");
        if(!(partes.length == 2)){//nombre de usuario;puntuacion
            throw new Exception();
        }else{
            controladorUsuarios.puntuarUsuario(Double.parseDouble(partes[1]), partes[0], usuarioActual, planes, usuarios);
        }
    }

    public void verCalificaionUsuario(String comando) throws UsuarioException{
        try {
            vista.imprimirString(controladorUsuarios.verPuntuacionUsuario(usuarioActual));
        }catch (Exception e){
            throw new UsuarioException("Error: Comprueba que estes logueado");
        }
    }

    public void filtrarPlanesFecha(String comando) throws SistemaException{
        //filter-plans-date:date1;date2 -> los planes que comiencen entre estas dos fechas
        //filter-plans-date:date1;*; -> los planes que comiencen después de date1
        //filter-plans-date:*;date2; -> los planes que comiencen antes de date2
        //* = null
        int index = comando.indexOf(":") + 1;
        if (index == 0){
            throw new SistemaException("Error de formato, no pusiste los :");
        }

        String[] partes = comando.substring(index).split(";");
        if(partes.length == 2){
            //extraer fecha
            String[] fecha1String = partes[0].substring(0).split("-");
            LocalDateTime fecha1 = null;
            if(!fecha1String[0].equals("*")){
                fecha1 = LocalDateTime.of(Integer.parseInt(fecha1String[0]), Integer.parseInt(fecha1String[1]), Integer.parseInt(fecha1String[2]),
                        Integer.parseInt(fecha1String[3]), Integer.parseInt(fecha1String[4]));
            }

            String[] fecha2String = partes[1].substring(0).split("-");
            LocalDateTime fecha2 = null;
            if(!fecha2String[0].equals("*")){
                fecha2 = LocalDateTime.of(Integer.parseInt(fecha2String[0]), Integer.parseInt(fecha2String[1]), Integer.parseInt(fecha2String[2]),
                        Integer.parseInt(fecha2String[3]), Integer.parseInt(fecha2String[4]));
            }

            //realizar la funcion
            vista.imprimirString(controladorPlan.filtrarPlanesFecha(planes, usuarioActual, fecha1, fecha2));
        }else{
            throw new SistemaException("Debes incluir dos parametros");
        }
    }

    public void poblador(){
        //crear usuarios
        controladorUsuarios.crearUsr(usuarios, "Jose", "1234", 20, 666666666);
        controladorUsuarios.crearUsr(usuarios, "Maria", "1234", 100, 766666666);
        controladorUsuarios.crearUsr(usuarios, "Marta", "1234", 80, 866666666);
        controladorUsuarios.crearUsr(usuarios, "Luis", "1234", 14, 966666666);
        controladorUsuarios.crearUsr(usuarios, "Juan", "1234", 55, 166666666);
        controladorUsuarios.crearUsr(usuarios, "Pedro", "1234", 69, 266666666);

        //crear planes
        controladorPlan.crearPlan(planes, "plan1", LocalDateTime.of(3000, 01, 01, 01, 01), "Madrid", 50, usuarios.get(0));
        controladorPlan.crearPlan(planes, "plan2", LocalDateTime.of(3500, 01, 01, 01, 01), "Madrid", 50, usuarios.get(1));
        controladorPlan.crearPlan(planes, "plan3", LocalDateTime.of(3000, 01, 01, 01, 01), "Madrid", 50, usuarios.get(2));
        controladorPlan.crearPlan(planes, "plan4", LocalDateTime.of(4000, 01, 01, 01, 01), "Madrid", 50, usuarios.get(0));
        controladorPlan.crearPlan(planes, "plan5", LocalDateTime.of(2500, 01, 01, 01, 01), "Madrid", 50, usuarios.get(0));
        controladorPlan.crearPlan(planes, "plan6", LocalDateTime.of(3000, 01, 01, 01, 01), "Madrid", 50, usuarios.get(0));

        //crear actividades
        controladorActividad.crearActividad(EnumActividad.GENERICA, "actividad1", 50, 100, 100, "una actividad chula", actividades);
        controladorActividad.crearActividad(EnumActividad.GENERICA, "actividad2", 50, 100, 100, "una actividad chula", actividades);
        controladorActividad.crearActividad(EnumActividad.TEATRO, "actividad3", 50, 100, 100, "una actividad chula", actividades);
        controladorActividad.crearActividad(EnumActividad.TEATRO, "actividad4", 50, 100, 100, "una actividad chula", actividades);
        controladorActividad.crearActividad(EnumActividad.TEATRO, "actividad5", 50, 100, 100, "una actividad chula", actividades);
        controladorActividad.crearActividad(EnumActividad.CINE, "actividad6", 50, 100, 100, "una actividad chula", actividades);
        controladorActividad.crearActividad(EnumActividad.CINE, "actividad7", 50, 100, 100, "una actividad chula", actividades);


        //añadir actividad a plan
        controladorPlan.aniadirActividadPlanPorId(1, 1, actividades, planes, usuarios.get(0));
        controladorPlan.aniadirActividadPlanPorId(6, 1, actividades, planes, usuarios.get(0));
        controladorPlan.aniadirActividadPlanPorId(7, 1, actividades, planes, usuarios.get(0));
        controladorPlan.aniadirActividadPlanPorId(2, 2, actividades, planes, usuarios.get(1));
        controladorPlan.aniadirActividadPlanPorId(3, 3, actividades, planes, usuarios.get(2));
        controladorPlan.aniadirActividadPlanPorId(4, 4, actividades, planes, usuarios.get(0));
        controladorPlan.aniadirActividadPlanPorId(5, 5, actividades, planes, usuarios.get(0));

        //apuntarse a plan
        controladorPlan.apuntarsePlan(1, usuarios.get(0), planes);
        controladorPlan.apuntarsePlan(1, usuarios.get(1), planes);
        controladorPlan.apuntarsePlan(1, usuarios.get(2), planes);
        controladorPlan.apuntarsePlan(2, usuarios.get(5), planes);
        controladorPlan.apuntarsePlan(3, usuarios.get(5), planes);
        controladorPlan.apuntarsePlan(4, usuarios.get(5), planes);

        //forzar concluir planes
        planes.get(0).setFechaHora(LocalDateTime.of(2000, 01, 01, 01, 01), usuarios.get(0));
        planes.get(1).setFechaHora(LocalDateTime.of(2000, 01, 01, 01, 01), usuarios.get(1));

        //valorar planes
        controladorUsuarios.puntuarPlan(1, 10, planes, usuarios.get(1));
        controladorUsuarios.puntuarPlan(1, 6, planes, usuarios.get(2));

        controladorUsuarios.puntuarPlan(2, 2, planes, usuarios.get(5));

        //puntuar usuario
        controladorUsuarios.puntuarUsuario(8, "Jose", usuarios.get(1), planes, usuarios);
        controladorUsuarios.puntuarUsuario(9, "Jose", usuarios.get(2), planes, usuarios);
        controladorUsuarios.puntuarUsuario(0, "Maria", usuarios.get(2), planes, usuarios);
        controladorUsuarios.puntuarUsuario(1, "Maria", usuarios.get(0), planes, usuarios);
        controladorUsuarios.puntuarUsuario(10, "Marta", usuarios.get(0), planes, usuarios);
        controladorUsuarios.puntuarUsuario(9, "Marta", usuarios.get(1), planes, usuarios);
    }
}
