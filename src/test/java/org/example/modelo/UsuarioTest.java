package org.example.modelo;

import org.junit.Test;
import static org.junit.Assert.*;

public class UsuarioTest {
    @Test
    public void testUsr(){
        Usuario u1 = new Usuario(1, "Paco", "12345", 45, 1234566789);

        assertEquals(u1.toString(), "id: 1; nombre: Paco; edad: 45; movil: 1234566789; clave: 12345");
    }
}
