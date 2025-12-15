/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package jeudedame;

import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author vdufo
 */
public class JeuTest {
    
    private static final String TEST_FILE = "test_partie.ser";
    private static final String COULEUR_BLANC = "BLANC";
    private static final String COULEUR_NOIR = "NOIR";
    
    public JeuTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testConstructeurEtGetters() {
        Jeu jeu = new Jeu();
        assertNotNull(jeu.getPlateau());
        assertEquals(COULEUR_BLANC, jeu.getJoueurCourant());
    }

    @Test
    public void testChangerTour() {
        Jeu jeu = new Jeu();
        assertEquals(COULEUR_BLANC, jeu.getJoueurCourant());
        jeu.changerTour();
        assertEquals(COULEUR_NOIR, jeu.getJoueurCourant());
        jeu.changerTour();
        assertEquals(COULEUR_BLANC, jeu.getJoueurCourant());
    }

    @Test
    public void testEstMonTour() {
        Jeu jeu = new Jeu();
        Piece pionBlanc = new Piece(new Point2D(0,0), COULEUR_BLANC);
        Piece pionNoir = new Piece(new Point2D(0,1), COULEUR_NOIR);

        // Tour blanc
        assertTrue(jeu.estMonTour(pionBlanc));
        assertFalse(jeu.estMonTour(pionNoir));

        // Tour noir
        jeu.changerTour();
        assertFalse(jeu.estMonTour(pionBlanc));
        assertTrue(jeu.estMonTour(pionNoir));

        // Piece null
        assertFalse(jeu.estMonTour(null));
    }

    @Test
    public void testSauvegarderEtCharger() {
        Jeu jeu = new Jeu();
        jeu.changerTour(); // tourAuNoir = true

        // Sauvegarder
        jeu.sauvegarder(TEST_FILE);
        File f = new File(TEST_FILE);
        assertTrue(f.exists(), "Le fichier de sauvegarde doit exister");

        // Charger
        Jeu loaded = Jeu.charger(TEST_FILE);
        assertNotNull(loaded);
        assertEquals("NOIR", loaded.getJoueurCourant());
        assertNotNull(loaded.getPlateau());

        // Charger fichier inexistant → null
        Jeu loaded2 = Jeu.charger("fichier_inexistant.ser");
        assertNull(loaded2);
    }

    @Test
    public void testSauvegarderErreur() {
        Jeu jeu = new Jeu();
        // Nom de fichier invalide pour provoquer IOException
        assertDoesNotThrow(() -> jeu.sauvegarder("/chemin/invalide/test.ser"));
        // On ne vérifie pas le fichier, juste qu'il n'y a pas d'exception fatale
    }
    
}
