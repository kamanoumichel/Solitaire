package com.example.solidaire;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.LinkedList;

public class PilesTable extends PileCartes {

    private static final int Y_OFFSET_VISIBLE = 25; // Décalage vertical pour les cartes empilées visibles
    private static final int Y_OFFSET_HIDDEN = 5;  // Décalage vertical pour les cartes cachées (plus petit)

    public PilesTable(int x, int y, int initialCards) {
        super(x, y);
    }

    @Override
    public boolean peutPoser(Carte card) {
        if (estVide()) {
            return card.getValeur() == Carte.ROI; // Seul un roi peut commencer une pile vide
        } else {
            Carte topCard = top();
            // Vérifie l'alternance des couleurs
            boolean isRedTop = (topCard.getCouleur() == Carte.COEUR || topCard.getCouleur() == Carte.CARREAU);
            boolean isRedNew = (card.getCouleur() == Carte.COEUR || card.getCouleur() == Carte.CARREAU);
            boolean alternatingColor = isRedTop != isRedNew;

            // Vérifie la valeur décroissante
            boolean descendingValue = card.getValeur() == topCard.getValeur() - 1;

            return alternatingColor && descendingValue;
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        if (estVide()) {
            // Dessine un espace réservé à une pile vide
            gc.setStroke(Color.DARKGRAY);
            gc.strokeRect(x, y, Carte.LARGEUR, Carte.HAUTEUR);
        } else {
            // Piochez toutes les cartes de la pile, empilées
            int currentY = y;
            for (Carte card : carteLinkedList) {
                card.draw(gc, x, currentY);
                if (card.getVisible()) {
                    currentY += Y_OFFSET_VISIBLE;
                } else {
                    currentY += Y_OFFSET_HIDDEN;
                }
            }
        }
    }

    @Override
    public boolean estInclus(int tx, int ty) {
        if (estVide()) {
            return super.estInclus(tx, ty); // Vérifiez la limite supérieure de la pile vide
        } else {
            // Calculez la hauteur totale de la pile, y compris toutes les cartes empilées
            int totalPileHeight = 0;
            for(Carte carte : carteLinkedList) {
                if (carte.getVisible()) {
                    totalPileHeight += Y_OFFSET_VISIBLE;
                } else {
                    totalPileHeight += Y_OFFSET_HIDDEN;
                }
                // Ajouter la hauteur de la dernière carte
                totalPileHeight += Carte.HAUTEUR - (carte.getVisible() ? Y_OFFSET_VISIBLE : Y_OFFSET_HIDDEN);
            }



            return tx >= x && tx <= x + Carte.LARGEUR &&
                    ty >= y && ty <= y + totalPileHeight;
        }
    }

    // Méthode pour obtenir une sous-pile de cartes visibles pour déplacer plusieurs cartes
    public LinkedList<Carte> getSubPile(int clickedY) {
        LinkedList<Carte> subPile = new LinkedList<>();
        int currentY = y;
        int startIndex = -1;

        for (int i = 0; i < carteLinkedList.size(); i++) {
            Carte card = carteLinkedList.get(i);
            int cardBottomY = currentY + Carte.HAUTEUR;

            if (card.getVisible()) {
                // Si cliqué dans la partie visible de cette carte
                if (clickedY >= currentY && clickedY < cardBottomY) {
                    startIndex = i;
                    break;
                }
                currentY += Y_OFFSET_VISIBLE;
            } else {
                currentY += Y_OFFSET_HIDDEN;
            }
        }

        if (startIndex != -1) {
            for (int i = startIndex; i < carteLinkedList.size(); i++) {
                subPile.add(carteLinkedList.get(i));
            }
        }
        return subPile;
    }

    // Méthode pour supprimer une sous-pile
    public void removeSubPile(LinkedList<Carte> subPile) {
        carteLinkedList.removeAll(subPile);
    }
}