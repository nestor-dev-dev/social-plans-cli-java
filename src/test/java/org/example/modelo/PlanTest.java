package org.example.modelo;

import org.junit.Test;

import java.time.LocalDateTime;
import static org.junit.Assert.*;

public class PlanTest {
    @Test
    public void testContructor(){
        Usuario usuario = new Usuario(1, "Paco", "1234", 24, 666666666);
        Plan p1 = new Plan(1, "Plan1", LocalDateTime.of(3000, 01, 01, 01, 01), "Madrid", 10, usuario);

        assertEquals(p1.toString(usuario.getEdad()), "id: 1; propietario: Paco; nombre: Plan1; fecha: 3000-01-01T01:01; lugar: Madrid; aforo: 10;duracion: 0 min; coste: 0.0;puntuacion: null;actividades: ;participantes: ;usuarios que calificaron: ");
    }
}
