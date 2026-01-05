package com.imt.framework.web.tuto;

import com.imt.framework.web.tuto.entities.Livre;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LivreTest {

    @Test
    void testLivreGettersAndSetters() {
        // Arrange
        Livre livre = new Livre();

        // Act
        livre.setId(1);
        livre.setTitre("Test Titre");
        livre.setAuteur("Test Auteur");
        livre.setPrice(10.5);

        // Assert
        assertEquals(1, livre.getId());
        assertEquals("Test Titre", livre.getTitre());
        assertEquals("Test Auteur", livre.getAuteur());
        assertEquals(10.5, livre.getPrice());
    }
}