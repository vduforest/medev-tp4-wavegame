package jeudedame;

import java.io.Serializable;

public class Piece implements Serializable {

    private Point2D pos;
    private String couleur;
    private Boolean isKing;

    // Taille du plateau (même valeur que dans Plateau)
    private static final int TAILLE = 10;

    public enum Direction {
        HAUT_GAUCHE,
        HAUT_DROIT,
        BAS_GAUCHE,
        BAS_DROIT
    }

    public Piece(Point2D pos, String couleur) {
        this.pos = pos;
        this.couleur = couleur;
        this.isKing = false;
    }

    // --- Getters / Setters ---
    public Point2D getPos() {
        return pos;
    }

    public String getCouleur() {
        return couleur;
    }

    public Boolean getIsKing() {
        return isKing;
    }

    public void setPos(Point2D pos) {
        this.pos = pos;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public void setIsKing(Boolean isKing) {
        this.isKing = isKing;
    }

    public void passerDame() {
        this.isKing = true;
    }

    // -----------------------------------------------------------
    // DEPLACER : ne pas déplacer si la case d'arrivée est occupée
    // -----------------------------------------------------------
    public boolean deplacer(Plateau plateau, Direction direction, int distance) {
        if (plateau == null || direction == null) {
            throw new IllegalArgumentException("Plateau ou direction null.");
        }

        int x1 = pos.getX();
        int y1 = pos.getY();

        // Règles de distance : pion vs dame
        if (!Boolean.TRUE.equals(isKing)) {
            // pion : toujours 1 case, on force
            distance = 1;
        } else {
            // dame : au moins 1 case
            if (distance <= 0) {
                throw new IllegalArgumentException("La distance doit être positive pour une dame.");
            }
        }

        int dx = 0, dy = 0;
        switch (direction) {
            case HAUT_GAUCHE -> {
                dx = -distance;
                dy = -distance;
            }
            case HAUT_DROIT -> {
                dx = distance;
                dy = -distance;
            }
            case BAS_GAUCHE -> {
                dx = -distance;
                dy = distance;
            }
            case BAS_DROIT -> {
                dx = distance;
                dy = distance;
            }
        }

        int x2 = x1 + dx;
        int y2 = y1 + dy;

        // Vérifier les bords du plateau (on ne touche pas à Plateau)
        if (x2 < 0 || x2 >= TAILLE || y2 < 0 || y2 >= TAILLE) {
            return false;
        }

        // Si il y a déjà un pion ou une dame à l'arrivée → on ne bouge pas
        if (plateau.getPiece(x2, y2) != null) {
            return false;
        }

        // Déplacement autorisé : on utilise bougerPiece du Plateau
        plateau.bougerPiece(x1, y1, x2, y2);
        // bougerPiece met déjà pos à jour via setPos(new Point2D(x2,y2))

        return true;
    }

    // -----------------------------------------------------------
    // MANGER : vérifier s'il y a une pièce à manger
    //         puis déplacer l'attaquant
    //  ⚠️ La suppression réelle de la victime dans la grille
    //     nécessite une méthode de Plateau qui met la case à null.
    // -----------------------------------------------------------
    public boolean manger(Plateau plateau, Direction direction, int distance) {
        if (plateau == null || direction == null) {
            throw new IllegalArgumentException("Plateau ou direction null.");
        }

        // Vérification de la distance selon pion ou dame
        validerDistance(distance);

        int dx = 0, dy = 0;
        switch (direction) {
            case HAUT_GAUCHE -> {
                dx = -distance;
                dy = -distance;
            }
            case HAUT_DROIT -> {
                dx = distance;
                dy = -distance;
            }
            case BAS_GAUCHE -> {
                dx = -distance;
                dy = distance;
            }
            case BAS_DROIT -> {
                dx = distance;
                dy = distance;
            }
        }

        int x1 = pos.getX();
        int y1 = pos.getY();
        int x2 = x1 + dx;
        int y2 = y1 + dy;

        // Vérification des bords et case vide
        if (!verifierCaseArrivee(plateau, x2, y2)) {
            return false;
        }

        if (!Boolean.TRUE.equals(isKing)) {
            return mangerPion(plateau, x1, y1, x2, y2);
        } else {
            return mangerDame(plateau, x1, y1, x2, y2);
        }
    }

// Vérifie la distance selon pion ou dame
    private void validerDistance(int distance) {
        if (!Boolean.TRUE.equals(isKing)) {
            if (distance != 2) {
                throw new IllegalArgumentException("Un pion doit sauter exactement 2 cases pour manger.");
            }
        } else {
            if (distance < 2) {
                throw new IllegalArgumentException("Une dame doit sauter au moins 2 cases pour manger.");
            }
        }
    }

// Vérifie que la case d'arrivée est valide et vide
    private boolean verifierCaseArrivee(Plateau plateau, int x2, int y2) {
        if (x2 < 0 || x2 >= TAILLE || y2 < 0 || y2 >= TAILLE) {
            return false;
        }
        return plateau.getPiece(x2, y2) == null;
    }

// Gestion du manger pour un pion
    private boolean mangerPion(Plateau plateau, int x1, int y1, int x2, int y2) {
        int mx = (x1 + x2) / 2;
        int my = (y1 + y2) / 2;
        Piece victime = plateau.getPiece(mx, my);

        if (victime == null || victime.getCouleur().equals(this.couleur)) {
            return false;
        }

        // Ici, suppression de la victime nécessiterait une méthode dans Plateau
        plateau.bougerPiece(x1, y1, x2, y2);
        return true;
    }

// Gestion du manger pour une dame
    private boolean mangerDame(Plateau plateau, int x1, int y1, int x2, int y2) {
        int stepX = Integer.signum(x2 - x1);
        int stepY = Integer.signum(y2 - y1);
        int cx = x1 + stepX;
        int cy = y1 + stepY;
        Piece victime = null;

        while (cx != x2 && cy != y2) {
            Piece p = plateau.getPiece(cx, cy);
            if (p != null) {
                if (victime != null) {
                    return false; // plus d'une pièce sur le chemin
                }
                victime = p;
            }
            cx += stepX;
            cy += stepY;
        }

        if (victime == null || victime.getCouleur().equals(this.couleur)) {
            return false;
        }

        // Ici aussi, suppression de la victime nécessiterait une méthode dans Plateau
        plateau.bougerPiece(x1, y1, x2, y2);
        return true;
    }

}
