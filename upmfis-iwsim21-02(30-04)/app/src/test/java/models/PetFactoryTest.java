package models;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidParameterException;

import static org.junit.Assert.*;

public class PetFactoryTest {

    @Before
    public void setUp() throws Exception {
        PetFactory petFactory= new PetFactory();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void createPet_RIAC_long10_Numero() { /* Crear objeto pet con RIAC long. 10 (número)*/
        PetFactory.createPet("Frodo", "Bueno", "Calle Palacios", "0123456789", "seguridad", "", "");
    }
    @Test
    public void createExoticPet_RIAC_long15_Numero() { /* Crear objeto ExoticPet con RIAC long. 15 (número) */
        PetFactory.createPet("Frodo", "Bueno", "Calle Palacios", "012345678901234", "seguridad", "", "certificate");
    }
    @Test
    public void createExoticPet2_RIAC_long10_Numero() { /* Crear objeto ExoticPet segunda opción con RIAC long. 10 (número) */
        PetFactory.createPet("Frodo", "Bueno", "Calle Palacios", "0123456789", "seguridad", "procedure", "");
    }
    @Test
    public void createExoticPet3_RIAC_long15_Numero() { /* Crear objeto ExoticPet tercera opción con RIAC long. 15 (número) */
        PetFactory.createPet("Frodo", "Bueno", "Calle Palacios", "012345678901234", "seguridad", "procedure", "certificate");
    }
    @Test(expected = InvalidParameterException.class)
    public void createBadPet_NameEmpty_RIAC_long10_Numero() { /* Crear objeto mal Pet con nombre vacio y con RIAC long. 10 (número) */
        PetFactory.createPet("", "Bueno", "Calle Palacios", "0123456789", "seguridad", "", "");
    }
    @Test(expected = InvalidParameterException.class)
    public void createBadPet_AddressEmpty_RIAC_long10_Numero() { /* Crear objeto mal Pet con address vacio y con RIAC long. 10 (número) */
        PetFactory.createPet("Frodo", "Bueno", "", "0123456789", "seguridad", "", "");
    }
    @Test(expected = InvalidParameterException.class)
    public void createBadPet_RIAC_longNo10no15_Numero() { /* Crear objeto mal Pet con RIAC long. != 10 (número) */
        PetFactory.createPet("Frodo", "Bueno", "Calle Palacios", "01234567890", "seguridad", "", "");
    }
    @Test(expected = InvalidParameterException.class)
    public void createBadPet_RIAC_long10_NoNumero() { /* Crear objeto mal Pet con RIAC long.= 10 (No número) */
        PetFactory.createPet("Frodo", "Bueno", "Calle Palacios", "abcdefghij", "seguridad", "", "");
    }
    @Test(expected = InvalidParameterException.class)
    public void createBadPet_RIAC_long15_NoNumero() { /* Crear objeto mal Pet con RIAC long.= 10 (No número) */
        PetFactory.createPet("Frodo", "Bueno", "Calle Palacios", "abcdefghijklmnñ", "seguridad", "", "");
    }
}
