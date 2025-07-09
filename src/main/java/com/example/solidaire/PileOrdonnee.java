package com.example.solidaire;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class PileOrdonnee extends PileCartes {

    public PileOrdonnee(int x, int y) {
        super(x, y);
    }

    @Override
    public boolean peutPoser(Carte card) {
        if (estVide()) {
            return card.getValeur() == Carte.AS; // Seul un As peut démarrer une pile ordonnée
        } else {
            Carte topCard = top();
            // Vérifier la même couleur et la valeur croissante
            return card.getCouleur() == topCard.getCouleur() && card.getValeur() == topCard.getValeur() + 1;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (estVide()) {
            // Dessinez un espace réservé à une pile vide pour les piles ordonnées
            gc.setStroke(Color.LIGHTBLUE); // Couleur différente pour la distinction
            gc.strokeRect(x, y, Carte.LARGEUR, Carte.HAUTEUR);
        } else {
            top().draw(gc, x, y);
        }
    }
}