package com.example.solidaire;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Collections;

public class Deck extends PileCartes {

    public Deck(int x, int y) {
        super(x, y);
        // Initialiser avec 52 cartes
        for (int couleur = Carte.COEUR; couleur <= Carte.PIQUE; couleur++) {
            for (int valeur = Carte.AS; valeur <= Carte.ROI; valeur++) {
                ajouteCarte(new Carte(valeur, couleur));
            }
        }
        shuffle();
    }

    public void shuffle() {
        Collections.shuffle(carteLinkedList);
    }

    @Override
    public boolean peutPoser(Carte carte) {
        // Aucune carte ne peut être explicitement « posée » sur le jeu depuis l'extérieur
        return false;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (estVide()) {
            // Dessiner un espace réservé à une pile vide
            gc.setStroke(Color.GRAY);
            gc.strokeRect(x, y, Carte.LARGEUR, Carte.HAUTEUR);
        } else {
            // Seul le dos du jeu est visible
            gc.setFill(Color.BLUE); // Exemple de couleur pour le dos du deck
            gc.fillRect(x + 2, y + 2, Carte.LARGEUR - 4, Carte.HAUTEUR - 4); // Consistent with Carte back
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x, y, Carte.LARGEUR, Carte.HAUTEUR);
        }
    }
}