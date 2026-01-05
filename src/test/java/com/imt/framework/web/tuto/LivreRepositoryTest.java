package com.imt.framework.web.tuto;

import com.imt.framework.web.tuto.entities.Livre;
import com.imt.framework.web.tuto.repositories.LivreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LivreRepositoryTest {

    @Autowired
    private LivreRepository livreRepository;

    @Test
    void testGetBooksWithMaxPrice() {
        // Arrange
        Livre cheap = new Livre(); cheap.setPrice(10.0); cheap.setTitre("A");
        Livre expensive = new Livre(); expensive.setPrice(50.0); expensive.setTitre("B");

        livreRepository.save(cheap);
        livreRepository.save(expensive);

        // Act
        List<Livre> result = livreRepository.getBooksWithMaxPrice(20.0);

        // Assert
        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getTitre());
    }
}