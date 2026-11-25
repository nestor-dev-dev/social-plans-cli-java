package org.example.modelo;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Plan {
    private int id;
    private String nombre;
    private ArrayList<Usuario> participantes;
    private LocalDateTime fechaHora;
    private String lugar;
    private int aforo;
    private ArrayList<Actividad> actividades;
    private Usuario propietario;
    private ArrayList<Double> puntuaciones;
    private ArrayList<Usuario> usuariosQueHanPuntuado;

    public Plan(int id,String nombre,LocalDateTime fechaHora, String lugar,int aforo,Usuario propietario) {
        this.id = id;
        this.nombre = nombre;
        this.participantes = new ArrayList<>();
        this.fechaHora = fechaHora;
        this.lugar =lugar;
        this.aforo = aforo;
        this.actividades = new ArrayList<>();
        this.propietario = propietario;
        this.puntuaciones = new ArrayList<>();
        this.usuariosQueHanPuntuado = new ArrayList<>();
    }

    public Plan(int id,String nombre,LocalDateTime fechaHora, String lugar,Usuario propietario) {
        this.id = id;
        this.nombre = nombre;
        this.participantes = new ArrayList<>();
        this.fechaHora = fechaHora;
        this.lugar =lugar;
        this.actividades = new ArrayList<>();
        this.propietario = propietario;
        this.puntuaciones = new ArrayList<>();
        this.usuariosQueHanPuntuado = new ArrayList<>();
    }

    public  int calcularDuracion(){
        int res=0;
        for(int i=0; i<actividades.size(); i++){ //sumamos la duracion de todas las actividades del plan
            res += actividades.get(i).getDuracion();
        }
        if(!actividades.isEmpty()){
            res += (actividades.size()-1)*20; // se le agregan 20 minutos entre actividad y actividad
        }
        return res;
    }

    public double calcularPrecioTotal(int edadUsr){
        double res = 0;
        for(int i=0; i<actividades.size(); i++){
            res += actividades.get(i).calcularPrecioDescontado(edadUsr);
        }
        return res;
    }

    public int calcularVacantesDisponibles(){
        if(aforo == -1){
            return -1;
        }else{
            return aforo - participantes.size();
        }
    }
    public boolean estoyApuntado(Usuario usuario){
        boolean res=false;
        for (int i=0; i<participantes.size(); i++){
            if(participantes.get(i).getId() == usuario.getId()){
                res=true;
            }
        }
        return res;
    }

    public boolean agregarPuntuacion(double miPuntuacion){
        if(miPuntuacion<=10 && miPuntuacion >= 0){
            puntuaciones.add(miPuntuacion);
            return true;
        }else {
            return false;
        }
    }

    public Double verMediaPuntuacion(){
        if(puntuaciones.size() > 0){
            Double sum=0.0;
            for (int i=0; i<puntuaciones.size(); i++){
                sum+=puntuaciones.get(i);
            }
            return sum/puntuaciones.size();
        }else{
            return null;
        }
    }

    public ArrayList<Usuario> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(ArrayList<Usuario> participantes, Usuario usuarioActual) {
        if(usuarioActual == this.propietario){
            this.participantes = participantes;
        }
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora, Usuario usuarioActual) {
        if(usuarioActual == this.propietario) {
            this.fechaHora = fechaHora;
        }
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar, Usuario usuarioActual) {
        if(usuarioActual == this.propietario) {
            this.lugar = lugar;
        }
    }

    public int getAforo() {
        return aforo;
    }

    public void setAforo(int aforo, Usuario usuarioActual) {
        if(usuarioActual == this.propietario) {
            this.aforo = aforo;
        }
    }
    public ArrayList<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(ArrayList<Actividad> actividades, Usuario usuarioActual) {
        if(usuarioActual == this.propietario) {
            this.actividades = actividades;
        }
    }

    public Usuario getPropietario() {
        return propietario;
    }

    public void setPropietario(Usuario propietario, Usuario usuarioActual) {
        if(usuarioActual == this.propietario) {
            this.propietario = propietario;
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre, Usuario usuarioActual) {
        if(usuarioActual == this.propietario) {
            this.nombre = nombre;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id, Usuario usuarioActual) {
        if(usuarioActual == this.propietario) {
            this.id = id;
        }
    }

    public ArrayList<Double> getPuntuaciones() {
        return puntuaciones;
    }

    public void setPuntuacion(ArrayList<Double> puntuaciones, Usuario usuarioActual) {
        if(usuarioActual == this.propietario) {
            this.puntuaciones = puntuaciones;
        }
    }

    public ArrayList<Usuario> getUsuariosQueHanPuntuado() {
        return usuariosQueHanPuntuado;
    }

    public void setUsuariosQueHanPuntuado(ArrayList<Usuario> usuariosQueHanPuntuado) {
        this.usuariosQueHanPuntuado = usuariosQueHanPuntuado;
    }

    public int verNumActividades(){
        return this.getActividades().size();
    }

    public int verNumParticipantes(){
        return this.getParticipantes().size();
    }

    public String toString(int edadUsr) {
        String res = "id: "+this.id+"; propietario: "+this.propietario.getNombre()+
                "; nombre: "+this.nombre+"; fecha: "+this.fechaHora+
                "; lugar: "+this.lugar+"; aforo: "+this.aforo+
                ";duracion: "+this.calcularDuracion()+" min; coste: "+this.calcularPrecioTotal(edadUsr)+
                ";puntuacion: "+this.verMediaPuntuacion()+
                ";actividades: ";
        for (int i=0; i<actividades.size(); i++){
            res += actividades.get(i).getNombre().toString()+"; ";
        }
        res += ";participantes: ";
        for (int i=0; i<participantes.size(); i++){
            res += participantes.get(i).getNombre().toString()+"; ";
        }
        res += ";usuarios que calificaron: ";
        for (int i=0; i<usuariosQueHanPuntuado.size(); i++){
            res += usuariosQueHanPuntuado.get(i).getNombre().toString()+"; ";
        }
        return res;
    }


}
