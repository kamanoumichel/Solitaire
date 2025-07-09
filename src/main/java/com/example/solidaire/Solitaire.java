package com.example.solidaire;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;
import java.util.LinkedList;

public class Solitaire {
    // Constantes pour la disposition du jeu
    public static final int NOMBRE_DE_PILES = 13;
    public static final int NOMBRE_PILES_ORDONNES = 4;
    public static final int NOMBRE_PILES_TABLES = 7;

    public static final int MARGE_SOMMET = 20;
    public static final int MARGE_GAUCHE = 20;
    public static final int ESPACE_INTER_PILES = 10; // Espace entre les cartes/piles

    public static Deck deck;
    public static Defausse defausse;
    public static ArrayList<PilesTable> pilesTable;
    public static ArrayList<PileOrdonnee> pilesOrdonnee;

    // Une liste contenant toutes les piles pour une itération facile (utile pour la détection des clics)
    public static ArrayList<PileCartes> allPiles;

    public static void initialisation() {
        // Initialiser Deck et Defausse
        deck = new Deck(MARGE_GAUCHE, MARGE_SOMMET);
        defausse = new Defausse(MARGE_GAUCHE + Carte.LARGEUR + ESPACE_INTER_PILES, MARGE_SOMMET);

        // Initialiser les piles ordonnées (piles de fondation) - Celles-ci doivent être placées avant les piles de table
        pilesOrdonnee = new ArrayList<>();
        int currentXForOrderedPiles = MARGE_GAUCHE + 3 * (Carte.LARGEUR + ESPACE_INTER_PILES); // Début après la zone Deck/Defausse
        for (int i = 0; i < NOMBRE_PILES_ORDONNES; i++) {
            pilesOrdonnee.add(new PileOrdonnee(currentXForOrderedPiles + i * (Carte.LARGEUR + ESPACE_INTER_PILES), MARGE_SOMMET));
        }

        // Initialiser les piles de table (Tableau Piles)
        pilesTable = new ArrayList<>();
        int currentYForTablePiles = MARGE_SOMMET + Carte.HAUTEUR + MARGE_SOMMET; // Sous la rangée supérieure de piles
        for (int i = 0; i < NOMBRE_PILES_TABLES; i++) {
            PilesTable pile = new PilesTable(MARGE_GAUCHE + i * (Carte.LARGEUR + ESPACE_INTER_PILES), currentYForTablePiles, i + 1);
            for (int j = 0; j <= i; j++) { // Distribuez les cartes en fonction des règles du tableau
                if (!deck.estVide()) {
                    Carte card = deck.pop();
                    pile.ajouteCarte(card);
                    if (j == i) { // Seule la dernière carte distribuée à une pile de tableau est face visible
                        card.retourne();
                    }
                }
            }
            pilesTable.add(pile);
        }

        // Remplissez la liste allPiles pour une gestion simplifiée des clics
        allPiles = new ArrayList<>();
        allPiles.add(deck);
        allPiles.add(defausse);
        allPiles.addAll(pilesOrdonnee);
        allPiles.addAll(pilesTable);
    }

    public static void drawAllPiles(GraphicsContext gc) {
        deck.draw(gc);
        defausse.draw(gc);

        for (PileOrdonnee pile : pilesOrdonnee) {
            pile.draw(gc);
        }

        for (PilesTable pile : pilesTable) {
            pile.draw(gc);
        }
    }

    public static void handleClick(int x, int y) {
        PileCartes clickedPile = null;
        for (PileCartes pile : allPiles) {
            if (pile.estInclus(x, y)) {
                clickedPile = pile;
                break;
            }
        }

        if (clickedPile != null) {
            if (clickedPile == deck) {
                handleDeckClick();
            } else if (clickedPile == defausse) {
                handleDefausseClick();
            } else if (pilesTable.contains(clickedPile)) {
                handlePilesTableClick((PilesTable) clickedPile, x, y);
            }

        }
    }

    private static void handleDeckClick() {
        if (deck.estVide()) {
            // Déplacez toutes les cartes de la défausse vers le paquet
            while (!defausse.estVide()) {
                Carte card = defausse.pop();
                card.retourne(); // Turn cards face down again
                deck.ajouteCarte(card);
            }
            deck.shuffle();
           // Pas de top().retourne() ici, la carte du dessus du paquet reste cachée
        } else {
            // Déplacez la carte du dessus du paquet pour la défausser
            Carte card = deck.pop();
            if (card != null) {
                defausse.ajouteCarte(card);
            }
            // Si le paquet n'est pas vide, la carte suivante reste face cachée
        }
    }

    private static void handleDefausseClick() {
        if (defausse.estVide()) {
            return; // Aucune action si la pile de défausse est vide
        }

        Carte cardToMove = defausse.top();
        if (cardToMove == null) return;

        // 1. Essayons de placer sur une pile ordonnée
        for (PileOrdonnee orderedPile : pilesOrdonnee) {
            if (orderedPile.peutPoser(cardToMove)) {
                defausse.pop();
                orderedPile.ajouteCarte(cardToMove);
                return; // Action entreprise, sortie
            }
        }

        // 2. Essayons de placer sur une pile de table
        for (PilesTable tablePile : pilesTable) {
            if (tablePile.peutPoser(cardToMove)) {
                defausse.pop();
                tablePile.ajouteCarte(cardToMove);
                return; // Action entreprise, sortie
            }
        }
        // 3. Aucune action si la carte ne peut pas être déplacée
    }

    private static void handlePilesTableClick(PilesTable clickedTablePile, int clickX, int clickY) {
        if (clickedTablePile.estVide()) {
            return; // Aucune action si la pile est vide
        }

        // Déterminer si une carte spécifique de la pile a été cliquée
        LinkedList<Carte> subPile = clickedTablePile.getSubPile(clickY);

        if (!subPile.isEmpty()) {
            Carte clickedCard = subPile.getFirst();

            // 1. Si la carte cliquée (ou la carte la plus profonde de la sous-pile) est face cachée, retournez-la (seule la carte du dessus de la pile compte)
            if (!clickedTablePile.top().getVisible()) {
                clickedTablePile.top().retourne();
                return;
            }

            // S'il s'agit d'une seule carte du haut de la pile
            if (subPile.size() == 1 && clickedCard == clickedTablePile.top()) {
                // 2. Essayons de déplacer la carte du dessus vers une pile ordonnée
                for (PileOrdonnee orderedPile : pilesOrdonnee) {
                    if (orderedPile.peutPoser(clickedCard)) {
                        clickedTablePile.pop();
                        orderedPile.ajouteCarte(clickedCard);
                        // Si la pile en dessous devient vide, ou si la nouvelle carte du dessus est face cachée, retournez-la
                        if (!clickedTablePile.estVide() && !clickedTablePile.top().getVisible()) {
                            clickedTablePile.top().retourne();
                        }
                        return;
                    }
                }
            }

            // 3. Sinon, essayons de déplacer la sous-pile (ou la carte unique) vers une autre pile de la table
            Carte deepestCardOfSubPile = subPile.getFirst();

            for (PilesTable targetTablePile : pilesTable) {
                if (targetTablePile != clickedTablePile && targetTablePile.peutPoser(deepestCardOfSubPile)) {
                    // Vérifiez si la carte la plus profonde de la sous-pile peut être placée
                    clickedTablePile.removeSubPile(subPile);
                    for (Carte card : subPile) {
                        targetTablePile.ajouteCarte(card);
                    }
                    // Si la pile d'origine en dessous devient vide, ou si la nouvelle carte du dessus est face cachée, retournez-la
                    if (!clickedTablePile.estVide() && !clickedTablePile.top().getVisible()) {
                        clickedTablePile.top().retourne();
                    }
                    return;
                }
            }
        }
    }
}