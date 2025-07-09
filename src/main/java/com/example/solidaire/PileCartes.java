package com.example.solidaire;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.LinkedList;

public abstract class PileCartes {
    protected LinkedList<Carte> carteLinkedList;
    protected int x;
    protected int y;

    public PileCartes(int x, int y) {
        this.x = x;
        this.y = y;
        this.carteLinkedList = new LinkedList<>();
    }

    public Carte top() {
        if (!estVide()) {
            return carteLinkedList.peekLast();
        }
        return null;
    }

    public Carte pop() {
        if (!estVide()) {
            return carteLinkedList.removeLast();
        }
        return null;
    }

    public void ajouteCarte(Carte card) {
        carteLinkedList.add(card);
    }

    public int taille() {
        return carteLinkedList.size();
    }

    public boolean estVide() {
        return carteLinkedList.isEmpty();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Dessin par défaut pour une pile générique (affiche uniquement la carte du dessus)
    public void draw(GraphicsContext gc) {
        if (estVide()) {
            // Dessinez un espace réservé pour une pile vide
            gc.setStroke(Color.GRAY);
            gc.strokeRect(x, y, Carte.LARGEUR, Carte.HAUTEUR);
        } else {
            top().draw(gc, x, y);
        }
    }

    // Vérifie si une coordonnée donnée (tx, ty) est dans les limites de la pile
    public boolean estInclus(int tx, int ty) {
        return tx >= x && tx <= x + Carte.LARGEUR &&
                ty >= y && ty <= y + Carte.HAUTEUR;
    }

    // Méthode abstraite à implémenter par des sous-classes pour des règles de placement de cartes spécifiques
    public abstract boolean peutPoser(Carte carte);
}