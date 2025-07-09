package com.example.solidaire;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Defausse extends PileCartes {

    public Defausse(int x, int y) {
        super(x, y);
    }

    @Override
    public void ajouteCarte(Carte carte) {
        carte.retourne(); // Rendre la carte visible lorsqu'elle est ajoutée à la pile de défausse
        super.ajouteCarte(carte);
    }

    @Override
    public boolean peutPoser(Carte carte) {
        // Les cartes ne peuvent pas être explicitement « posées » sur la pile de défausse par l'action de l'utilisateur.
        // Elles sont déplacées ici depuis le paquet.
        return false;
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (estVide()) {
            // Dessine un espace réservé à une pile vide pour la pile de défausse
            gc.setStroke(Color.LIGHTGRAY);
            gc.strokeRect(x, y, Carte.LARGEUR, Carte.HAUTEUR);
        } else {
            top().draw(gc, x, y);
        }
    }
}