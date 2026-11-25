package org.example.controlador;

import org.example.excepciones.PlanException;
import org.example.modelo.Actividad;
import org.example.modelo.Plan;
import org.example.modelo.Usuario;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class ControladorPlan {
    public Plan crearPlan(ArrayList<Plan> planes, String nombre, LocalDateTime fechaHora, String lugar, int aforo, Usuario propietario) {
        //calcular nuevo id
        int id = 1;
        if (!planes.isEmpty()) {
            id = planes.get(planes.size() - 1).getId() + 1;
        }

        //metemos nuevo plan
        Plan nuevoPlan = new Plan(id, nombre, fechaHora, lugar, aforo, propietario);
        planes.add(nuevoPlan);

        return nuevoPlan;
    }

    public void apuntarsePlan(int idPlan, Usuario usuarioActual, ArrayList<Plan> planes) throws PlanException {
        if (usuarioActual != null) {
            //obtener el plan al que se quiera apuntar
            Plan plan = null;
            for (int i = 0; i < planes.size(); i++) {
                if (planes.get(i).getId() == idPlan) {
                    plan = planes.get(i);
                }
            }

            //apuntarse
            if(plan != null && plan.getFechaHora().isAfter(LocalDateTime.now())) {//la fecha del plan es después de la fecha de hoy
                if (plan.getParticipantes().size() < plan.getAforo() && !plan.estoyApuntado(usuarioActual)
                        && calcularFechaSolapada(plan, usuarioActual, planes)) {
                    //comprobar que no sobrepasa el aforo del plan, que no esta apuntado y que no solapan fechas con sus planes subscritos
                    ArrayList<Usuario> participantesAux = plan.getParticipantes();
                    participantesAux.add(usuarioActual);
                    plan.setParticipantes(participantesAux, plan.getPropietario());
                }else{
                    throw new PlanException("Error, revisa que: 1-no se haya sobrepasado el aforo, 2-no estemos previamente apuntados, " +
                            "3-la fecha no este solapada con la de otro plan, 4-estes logueado");
                }
            }else {
                throw new PlanException("El plan o el usuario no se han encontrado o la fecha del plan es anterior a la fecha actual");
            }
        }
    }

    private boolean calcularFechaSolapada(Plan plan, Usuario usuarioActual, ArrayList<Plan> planes) {
        boolean res = true;
        LocalDateTime fecha = plan.getFechaHora();
        LocalDateTime fechaFin = plan.getFechaHora().plusMinutes(plan.calcularDuracion());
        for (int i = 0; i < planes.size(); i++) {
            //estoy apuntado
            if (planes.get(i).estoyApuntado(usuarioActual)) {
                LocalDateTime fechaOtroPlan = planes.get(i).getFechaHora();
                LocalDateTime fechaFinOtroPlan = planes.get(i).getFechaHora().plusMinutes(planes.get(i).calcularDuracion());

                if (fecha.isBefore(fechaOtroPlan) && fechaFin.isBefore(fechaOtroPlan)) {
                    //la fecha inicio y fin estan antes de la fecha inicio de este plan
                } else if (fecha.isAfter(fechaFinOtroPlan)) {
                    //la fecha inicion esta despues de la fecha fin del otro plan
                } else {
                    res = false;
                }
            }
        }
        return res;
    }

    public void desapuntarseDePlan(int idPlan, Usuario usuarioActual, ArrayList<Plan> planes) throws PlanException {
        if (usuarioActual != null) {
            //obtenemos el plan
            Plan plan = null;
            for (int i = 0; i < planes.size(); i++) {
                if (planes.get(i).getId() == idPlan) {
                    plan = planes.get(i);
                }
            }


            //eliminarse de la lista de participantes de plan solo si no ha empezado el plan y comprobamos que estuvieramos apuntados
            if (plan != null && plan.getFechaHora().isAfter(LocalDateTime.now()) && plan.estoyApuntado(usuarioActual)) {
                ArrayList<Usuario> participantesAux = plan.getParticipantes();
                participantesAux.remove(usuarioActual);
                plan.setParticipantes(participantesAux, plan.getPropietario());
            }else{
                throw new PlanException("Error comprueba que: 1-Estemos previamente apuntados al plan, 2-Que el plan no haya comenzado, 3-Que el plan exista");
            }
        }
    }

    public String verPlanesSuscritos(ArrayList<Plan> planes, Usuario usuarioActual) {
        String res = "";
        //recorre planes
        for (int i = 0; i < planes.size(); i++) {
            if (planes.get(i).estoyApuntado(usuarioActual)) { //comprueba que este apuntado
                res += planes.get(i).getNombre() + " ";
            }
        }
        return res;
    }

    public String verPlanesSuscritosConSusActividades(ArrayList<Plan> planes, Usuario usuarioActual) {
        String res = "";
        //recorre los planes
        for (int i = 0; i < planes.size(); i++) {
            if (planes.get(i).estoyApuntado(usuarioActual)) { //comprueba que esta apuntado
                res += planes.get(i).getNombre() + ": ";
                for (Actividad a : planes.get(i).getActividades()){ //recorre las actividades del plan
                    res += a.getNombre() + " ";
                }
                res += "\n";
            }
        }
        return res;
    }

    public String listarPlanesFecha(ArrayList<Plan> planes, Usuario usuarioActual) {
        String res = "";
        Collections.sort(planes, new Comparator<Plan>() {//usar algoritmo quickSort nativo de java
            @Override
            public int compare(Plan p1, Plan p2) {
                return p1.getFechaHora().compareTo(p2.getFechaHora());
            }//filtramos comparando fechas
        });

        for (Plan plan : planes) {//imprimimos la lista
            res += plan.toString(usuarioActual.getEdad()) + "\n";
        }
        return res;
    }

    public String listarPlanesPuntuacion(ArrayList<Plan> planes, Usuario usuarioActual) {
        String res = "";
        Collections.sort(planes, new Comparator<Plan>() {
            @Override
            public int compare(Plan p1, Plan p2) {
                Double media1 = p1.verMediaPuntuacion();
                Double media2 = p2.verMediaPuntuacion();

                if (media1 == null && media2 == null) {
                    return 0; //se consideran de la misma puntuación devolviendo 0
                } else if (media1 == null) {
                    return 1; //p1 se coloca después de p2, porque se considera que p1=null es mayor que p2
                } else if (media2 == null) {
                    return -1; //p2 se coloca después de p1, porque p2 es mayor
                } else {
                    return Double.compare(media1, media2); //se comparan ambas variables y se colocan según el algoritmo quickSort
                }
            }
        });

        for (Plan plan : planes) {
            res += plan.verMediaPuntuacion() + " " + plan.toString(usuarioActual.getEdad()) + "\n";
        }
        return res;
    }

    public String listarPlanesPuntuacionDuenio(ArrayList<Plan> planes, Usuario usuarioActual) {
        String res = "";
        Collections.sort(planes, new Comparator<Plan>() {
            @Override
            public int compare(Plan p1, Plan p2) {
                Double media1 = p1.getPropietario().calcularMiMediaPuntuacion();
                Double media2 = p2.getPropietario().calcularMiMediaPuntuacion();

                if (media1 == null && media2 == null) {
                    return 0;
                } else if (media1 == null) {
                    return 1;
                } else if (media2 == null) {
                    return -1;
                } else {
                    return Double.compare(media1, media2);
                }
            }
        });

        for (Plan plan : planes) {
            res += "Puntuacion del duenio " + plan.getPropietario().calcularMiMediaPuntuacion() + " " + plan.toString(usuarioActual.getEdad()) + "\n";
        }
        return res;
    }

    public String filtrarPlanesFecha(ArrayList<Plan> planes, Usuario usuarioActual, LocalDateTime date1, LocalDateTime date2) throws PlanException{
        //filter-plans-date:date1;date2 -> los planes que comiencen entre estas dos fechas
        //filter-plans-date:date1;*; -> los planes que comiencen después de date1
        //filter-plans-date:*;date2; -> los planes que comiencen antes de date2
        //* = null

        String res = "";

        if(date1 == null && date2 == null){
            throw new PlanException("Debes agregar al menos una fecha en filter-plans-date");
        }else if(date1 == null){
            //planes que empiecen antes que date2
            for (Plan p : planes){
                if(p.getFechaHora().isBefore(date2)){
                    res += p.toString(usuarioActual.getEdad())+"\n";
                }
            }
        }else if(date2 == null){
            //planes que empiecen despues que date1
            for (Plan p : planes){
                if(p.getFechaHora().isAfter(date1)){
                    res += p.toString(usuarioActual.getEdad())+"\n";
                }
            }
        }else{
            //planes que empiecen entre date1 y date2
            for (Plan p : planes){
                if(p.getFechaHora().isAfter(date1) && p.getFechaHora().isBefore(date2)){
                    res += p.toString(usuarioActual.getEdad())+"\n";
                }
            }
        }

        return res;
    }

    public void aniadirActividadPlanPorId(int idActividad, int idPlan, ArrayList<Actividad> actividades, ArrayList<Plan> planes, Usuario usuario) throws PlanException{
        Actividad actividad = null;
        Plan plan = null;
        //busca la actividad seleccionado
        for (Actividad a : actividades){
            if(a.getId() == idActividad){
                actividad = a;
            }
        }
        //busca el plan seleccionado
        for (Plan p : planes){
            if(p.getId() == idPlan){
                plan = p;
            }
        }

        if(actividad == null || plan == null){ //comprueba que existen dicho plan y actividad
            throw new PlanException("La actividad y el plan deben existir");
        }else {
            aniadirActividadPlan(actividad, plan, usuario); //añade la actividad al plan
        }
    }

    private void aniadirActividadPlan(Actividad nuevaActividad, Plan plan, Usuario usuarioActual) throws PlanException{
        //comprueba que el aforo del plan es menor que el de la actividad, y que el usuario es el propietario del plan
        if (plan.getAforo() < nuevaActividad.getAforo() && usuarioActual.getId() == plan.getPropietario().getId()) {
            ArrayList<Actividad> aux = plan.getActividades();
            aux.add(nuevaActividad);
            plan.setActividades(aux, usuarioActual);
        } else {
            throw new PlanException("Error, comprueba que: el aforo del plan sea menor al de la actividad y que el usuario logueado sea propietario del plan\n" +
                    "El plan y la actividad deben existir");
        }
    }

    public void eliminarPlan(int idPlan, ArrayList<Plan> planes, Usuario usuarioActual) throws PlanException{
        boolean encontrado = false;
        //busca el plan seleccionado y comprueba si es el usuario
        for (int i = 0; i < planes.size(); i++) {
            if (planes.get(i).getId() == idPlan && planes.get(i).getPropietario() == usuarioActual) {
                planes.remove(i);
                encontrado = true;
            }
        }
        if(!encontrado){
            throw new PlanException("No se encontro el plan o no eres el propietario");
        }
    }
}
