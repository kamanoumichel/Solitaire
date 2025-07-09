package com.example.solidaire;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.transform.Rotate;

public class Carte {
    public static final int COEUR = 0;
    public static final int CARREAU = 1;
    public static final int TREFLE = 2;
    public static final int PIQUE = 3;

    public static final int AS = 1;
    public static final int VALET = 11;
    public static final int DAME = 12;
    public static final int ROI = 13;

    public static final int LARGEUR = 70; // Width of the card
    public static final int HAUTEUR = 90; // Height of the card

    private int couleur;
    private int valeur;
    private boolean visible;

    public Carte(int valeur, int couleur) {
        this.valeur = valeur;
        this.couleur = couleur;
        this.visible = false; // By default, cards are face down
    }

    public int getCouleur() {
        return couleur;
    }

    public int getValeur() {
        return valeur;
    }

    public boolean getVisible() {
        return visible;
    }

    public void retourne() {
        this.visible = !this.visible; // Flip visibility
    }

    public String getCouleurCommeChaine() {
        switch (couleur) {
            case COEUR:
                return "Coeurs";
            case CARREAU:
                return "Carreaux";
            case TREFLE:
                return "Trèfles";
            case PIQUE:
                return "Piques";
            default:
                return "Inconnu";
        }
    }

    public String getValeurCommeChaine() {
        switch (valeur) {
            case AS:
                return "AS";
            case VALET:
                return "J";
            case DAME:
                return "Q";
            case ROI:
                return "K";
            default:
                return String.valueOf(valeur);
        }
    }

    @Override
    public String toString() {
        return getValeurCommeChaine() + " de " + getCouleurCommeChaine();
    }

    public void draw(GraphicsContext gc, int x, int y) {
        // Dessin des bordures de la carte
        gc.setStroke(Color.BLACK);
        gc.setFill(Color.WHITE);
        gc.strokeRect(x, y, LARGEUR, HAUTEUR);
        gc.fillRect(x, y, LARGEUR, HAUTEUR);

        if (!visible) {
            // Dessin du dos de la carte
            gc.setFill(Color.BLUE);
            gc.fillRect(x + 2, y + 2, LARGEUR - 4, HAUTEUR - 4); // Slightly smaller filled rect
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(2);
            gc.strokeOval(x + LARGEUR / 4, y + HAUTEUR / 4, LARGEUR / 2, HAUTEUR / 2);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 10));
            gc.fillText("SOLITAIRE", x + (LARGEUR / 2) - 25, y + (HAUTEUR / 2) + 3);
            gc.setLineWidth(1); // Reset line width
        } else {
            // Dessin de la face de la carte
            Color textColor = (couleur == COEUR || couleur == CARREAU) ? Color.RED : Color.BLACK;
            gc.setFill(textColor);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 16));

            String valeurStr = getValeurCommeChaine();
            String couleurSymbole = "";

            switch (couleur) {
                case COEUR:
                    couleurSymbole = "\u2665"; // coeur
                    break;
                case CARREAU:
                    couleurSymbole = "\u2666"; // Diamand
                    break;
                case TREFLE:
                    couleurSymbole = "\u2663"; // trefle
                    break;
                case PIQUE:
                    couleurSymbole = "\u2660"; // pique
                    break;
            }

            // Valeur du tirage et couleur en haut à gauche
            gc.fillText(valeurStr, x + 5, y + 18);
            gc.fillText(couleurSymbole, x + 5, y + 35);

            // Valeur de tirage et couleur en bas à droite (tournées/retournées)
            gc.save();
            // Translater vers le coin inférieur à droite de la carte
            gc.translate(x + LARGEUR - 5, y + HAUTEUR - 5);
            // Rotation de 180 degrés
            gc.rotate(180);
            // Dessiner du texte, ajusté pour la nouvelle origine et la nouvelle rotation
            gc.fillText(valeurStr, 15, 15); // Ajuster ces valeurs pour affiner la position
            gc.fillText(couleurSymbole, -2, 8); // Ajuster ces valeurs pour affiner la position
            gc.restore();

            // Dessin d'un symbole plus grand au centre
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 30));
            gc.fillText(couleurSymbole, x + (LARGEUR / 2) - 15, y + (HAUTEUR / 2) + 10);
        }
    }
}