package com.imt.framework.web.tuto;

import com.imt.framework.web.tuto.entities.Livre;
import com.imt.framework.web.tuto.repositories.LivreRepository;
import com.imt.framework.web.tuto.resources.LivreResource;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

// @ExtendWith(MockitoExtension.class) permet d'utiliser les annotations @Mock sans charger Spring
@ExtendWith(MockitoExtension.class)
class LivreResourceTest {

    @Mock // Crée un faux repository
    private LivreRepository livreRepository;

    @InjectMocks // Injecte le faux repository dans votre vraie Resource
    private LivreResource livreResource;

    @Test
    void testGetBooks_ShouldReturnAllBooks_WhenNoParam() {
        // ARRANGE (Préparation)
        Livre l1 = new Livre(); l1.setTitre("Livre 1");
        Livre l2 = new Livre(); l2.setTitre("Livre 2");
        // On dit au mock : "Si on t'appelle findAll, renvoie cette liste"
        when(livreRepository.findAll()).thenReturn(Arrays.asList(l1, l2));

        // ACT (Action)
        Response response = livreResource.getBooks(null);

        // ASSERT (Vérification)
        assertEquals(200, response.getStatus()); // 200 OK
        List<Livre> resultList = (List<Livre>) response.getEntity();
        assertEquals(2, resultList.size());

        // On vérifie que findAll a bien été appelé une fois
        verify(livreRepository, times(1)).findAll();
    }

    @Test
    void testGetBooks_ShouldReturnFilteredBooks_WhenMaxPriceIsPresent() {
        // ARRANGE
        Double maxPrice = 15.0;
        Livre l1 = new Livre(); l1.setPrice(10.0);
        when(livreRepository.getBooksWithMaxPrice(maxPrice)).thenReturn(List.of(l1));

        // ACT
        Response response = livreResource.getBooks(maxPrice);

        // ASSERT
        List<Livre> resultList = (List<Livre>) response.getEntity();
        assertEquals(1, resultList.size());
        verify(livreRepository).getBooksWithMaxPrice(maxPrice); // Vérifie l'appel de la méthode custom
        verify(livreRepository, never()).findAll(); // Vérifie qu'on n'a PAS appelé findAll
    }

    @Test
    void testCreateBook() {
        // ARRANGE
        Livre newBook = new Livre();

        // ACT
        livreResource.createBook(newBook);

        // ASSERT
        // On vérifie simplement que la méthode save du repository a été appelée
        verify(livreRepository, times(1)).save(newBook);
    }

    @Test
    void testUpdateBook_Success() throws Exception {
        // ARRANGE
        Integer id = 1;
        Livre existingLivre = new Livre(); existingLivre.setId(id); existingLivre.setTitre("Old");
        Livre updateData = new Livre(); updateData.setTitre("New"); updateData.setPrice(20.0); updateData.setAuteur("Moi");

        when(livreRepository.findById(id)).thenReturn(Optional.of(existingLivre));

        // ACT
        livreResource.updateBook(id, updateData);

        // ASSERT
        verify(livreRepository).save(existingLivre); // Vérifie qu'on sauvegarde l'objet modifié
        assertEquals("New", existingLivre.getTitre()); // Vérifie que le titre a changé
    }

    @Test
    void testUpdateBook_NotFound_ShouldThrowException() {
        // ARRANGE
        Integer id = 99;
        Livre updateData = new Livre();
        when(livreRepository.findById(id)).thenReturn(Optional.empty());

        // ACT & ASSERT
        Exception exception = assertThrows(Exception.class, () -> {
            livreResource.updateBook(id, updateData);
        });

        assertEquals("Livre inconnu", exception.getMessage());
        verify(livreRepository, never()).save(any());
    }

    @Test
    void testDeleteBook() {
        // ACT
        livreResource.deleteBook(1);

        // ASSERT
        verify(livreRepository, times(1)).deleteById(1);
    }
}