/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package jeudedame;

/**
 * @author vdufo
 */

import java.util.Scanner;

public class JeuDeDame { 
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Jeu partie = new Jeu();
        
        afficherBienvenue();
        boolean jeuEnCours = true;
        
        while (jeuEnCours) {
            partie.getPlateau().afficher();
            System.out.println("C'est au tour des " + partie.getJoueurCourant());
            System.out.print("Votre coup > ");
            String entree = scanner.nextLine();
            
            jeuEnCours = traiterCommande(entree, partie);
        }
        
        scanner.close();
        System.out.println("Au revoir !");
    }
    
    public static void afficherBienvenue() {
        System.out.println("=== BIENVENUE AU JEU DE DAMES ===");
        System.out.println("Règles : Tapez les coordonnées 'x1 y1 x2 y2'.");
        System.out.println("Exemple : '0 6 1 5' déplace le pion de la colonne 0, ligne 6 vers colonne 1, ligne 5.");
        System.out.println("Commandes spéciales : 'sauver [nom]', 'charger [nom]', 'quitter'.");
        System.out.println("\n-----------------------------");
    }
    
    public static boolean traiterCommande(String entree, Jeu partie) {
        try {
            if (entree.equals("quitter")) return false;
            
            if (entree.startsWith("sauver")) {
                sauvegarderPartie(entree, partie);
                return true;
            }
            
            if (entree.startsWith("charger")) {
                partie = chargerPartie(entree, partie);
                return true;
            }
            
            jouerCoup(entree, partie);
            return true;
        } catch (Exception e) {
            System.out.println("Erreur de saisie. Format attendu : 0 6 1 5");
            return true;
        }
    }
    
    public static void sauvegarderPartie(String entree, Jeu partie) {
        String nom = entree.split(" ")[1];
        partie.sauvegarder(nom);
    }
    
    public static Jeu chargerPartie(String entree, Jeu partie) {
        String nom = entree.split(" ")[1];
        Jeu nouvellePartie = Jeu.charger(nom);
        if (nouvellePartie != null) return nouvellePartie;
        return partie;
    }
    
    public static void jouerCoup(String entree, Jeu partie) {
        String[] coords = entree.split(" ");
        int x1 = Integer.parseInt(coords[0]);
        int y1 = Integer.parseInt(coords[1]);
        int x2 = Integer.parseInt(coords[2]);
        int y2 = Integer.parseInt(coords[3]);
        Piece pieceSelectionnee = partie.getPlateau().getPiece(x1, y1);

        if (pieceSelectionnee == null) {
            System.out.println("ERREUR : Il n'y a pas de pièce ici !");
        } else if (!partie.estMonTour(pieceSelectionnee)) {
            System.out.println("ERREUR : Ce n'est pas votre pièce ! C'est aux " + partie.getJoueurCourant());
        } else {
            partie.getPlateau().bougerPiece(x1, y1, x2, y2);
            partie.changerTour();
        }
    }
}
