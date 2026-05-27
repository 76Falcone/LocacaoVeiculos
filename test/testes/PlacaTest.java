package testes;

import org.junit.Test;
import static org.junit.Assert.*;
import model.Placa;

public class PlacaTest {

    @Test
    public void testPlacaValidaTradicional() {
        Placa placa = new Placa("ABC1234");
        assertEquals("ABC1234", placa.getValor());
    }

    @Test
    public void testPlacaValidaMercosul() {
        Placa placa = new Placa("abc1d23");
        assertEquals("ABC1D23", placa.getValor()); // Normalizado para maiúsculo
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlacaInvalidaCurta() {
        new Placa("AB123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlacaInvalidaCaracteres() {
        new Placa("AB@1234");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPlacaNula() {
        new Placa(null);
    }
}
