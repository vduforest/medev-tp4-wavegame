package jeudedame;

/**
 * @author vdufo
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;

public class PieceTest {

    private Piece[][] getGrille(Plateau plateau) throws Exception {
        Field f = Plateau.class.getDeclaredField("grille");
        f.setAccessible(true);
        return (Piece[][]) f.get(plateau);
    }

    private void viderPlateau(Plateau plateau) throws Exception {
        Piece[][] grille = getGrille(plateau);
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                grille[x][y] = null;
            }
        }
    }

    @Test
    public void testDeplacerDameCaseLibre() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        Piece dame = new Piece(new Point2D(4, 4), "NOIR");
        dame.passerDame();
        grille[4][4] = dame;

        boolean ok = dame.deplacer(plateau, Piece.Direction.BAS_DROIT, 2);
        assertTrue(ok);
        assertEquals(6, dame.getPos().getX());
        assertEquals(6, dame.getPos().getY());
        assertSame(dame, grille[6][6]);
        assertNull(grille[4][4]);
    }

    @Test
    public void testDeplacerDameHorsPlateau() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        Piece dame = new Piece(new Point2D(9, 9), "NOIR");
        dame.passerDame();
        grille[9][9] = dame;

        boolean ok = dame.deplacer(plateau, Piece.Direction.BAS_DROIT, 1);
        assertFalse(ok);
        assertEquals(9, dame.getPos().getX());
        assertEquals(9, dame.getPos().getY());
        assertSame(dame, grille[9][9]);
    }

    @Test
    public void testMangerDameSansVictime() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        Piece dame = new Piece(new Point2D(5, 5), "NOIR");
        dame.passerDame();
        grille[5][5] = dame;

        boolean ok = dame.manger(plateau, Piece.Direction.BAS_DROIT, 2);
        assertFalse(ok);
        assertEquals(5, dame.getPos().getX());
        assertEquals(5, dame.getPos().getY());
    }

    @Test
    public void testMangerDamePieceMemeCouleur() throws Exception {
        Plateau plateau = new Plateau();
        viderPlateau(plateau);
        Piece[][] grille = getGrille(plateau);

        Piece dame = new Piece(new Point2D(5, 5), "NOIR");
        dame.passerDame();
        Piece victime = new Piece(new Point2D(6, 6), "NOIR");
        grille[5][5] = dame;
        grille[6][6] = victime;

        boolean ok = dame.manger(plateau, Piece.Direction.BAS_DROIT, 2);
        assertFalse(ok);
        assertEquals(5, dame.getPos().getX());
        assertEquals(5, dame.getPos().getY());
    }

    @Test
    public void testDeplacerNullPlateauOuDirection() {
        Piece pion = new Piece(new Point2D(2, 2), "BLANC");
        assertThrows(IllegalArgumentException.class, () -> pion.deplacer(null, Piece.Direction.BAS_DROIT, 1));
        assertThrows(IllegalArgumentException.class, () -> pion.deplacer(new Plateau(), null, 1));
    }

    @Test
    public void testMangerNullPlateauOuDirection() {
        Piece pion = new Piece(new Point2D(2, 2), "BLANC");
        assertThrows(IllegalArgumentException.class, () -> pion.manger(null, Piece.Direction.BAS_DROIT, 2));
        assertThrows(IllegalArgumentException.class, () -> pion.manger(new Plateau(), null, 2));
    }
}


