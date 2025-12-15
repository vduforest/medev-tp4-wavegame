/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package jeudedame;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JeuDeDameTest {

    private Jeu partie;

    @BeforeEach
    void setUp() {
        // Initialisation d'une partie avant chaque test
        partie = new Jeu();
    }

    @AfterEach
    void tearDown() {
        partie = null;
    }

    @Test
    void testTraiterCommandeQuitter() {
        boolean resultat = JeuDeDame.traiterCommande("quitter", partie);
        assertFalse(resultat, "La commande 'quitter' doit arrêter le jeu (false)");
    }

    @Test
    void testTraiterCommandeSauver() {
        boolean resultat = JeuDeDame.traiterCommande("sauver testSauvegarde", partie);
        assertTrue(resultat, "La commande 'sauver' doit continuer le jeu (true)");
        // Optionnel : vérifier que le fichier de sauvegarde existe
    }

    @Test
    void testTraiterCommandeChargerFichierInexistant() {
        boolean resultat = JeuDeDame.traiterCommande("charger fichierInexistant", partie);
        assertTrue(resultat, "Charger un fichier inexistant continue le jeu (true)");
    }

    @Test
    void testTraiterCommandeJouerCoupValide() {
        // Vérifier qu'une pièce est présente et c'est au joueur courant
        Piece piece = partie.getPlateau().getPiece(0, 6); // exemple position initiale
        if (piece != null && partie.estMonTour(piece)) {
            boolean resultat = JeuDeDame.traiterCommande("0 6 1 5", partie);
            assertTrue(resultat, "Un coup valide doit permettre de continuer le jeu");
        }
    }

    @Test
    void testTraiterCommandeJouerCoupVide() {
        boolean resultat = JeuDeDame.traiterCommande("9 9 10 10", partie);
        assertTrue(resultat, "Un coup sur une case vide continue le jeu (true)");
    }

    @Test
    void testTraiterCommandeJouerCoupPieceAdverse() {
        // Simuler un coup sur une pièce adverse
        Piece piece = partie.getPlateau().getPiece(0, 1); // supposons une pièce adverse
        if (piece != null && !partie.estMonTour(piece)) {
            boolean resultat = JeuDeDame.traiterCommande("0 1 1 2", partie);
            assertTrue(resultat, "Coup sur pièce adverse continue le jeu (true)");
        }
    }
}
