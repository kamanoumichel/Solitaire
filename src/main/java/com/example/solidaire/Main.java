package com.example.solidaire;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;

import java.util.ArrayList;
import java.util.Scanner;

public class Main extends Application {

    private static GraphicsContext gc;
    private Canvas canvas;

    public final static int CARTES_PAR_COULEUR = 13;
    public final static int NOMBRE_COULEURS = 4;
    public final static int NOMBRE_PILES = 7;

    public final static int MARGE_HAUTE = 40;
    public final static int MARGE_GAUCHE = 5;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        initUI(stage);
    }

    private void initUI(Stage stage) {
        Pane root = new Pane();
        canvas = new Canvas(1000, 700); // Ajustez la taille de la toile selon les besoins de la mise en page
        gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, canvas.getWidth(), canvas.getHeight(), Color.WHITESMOKE);

        stage.setTitle("Solitaire");
        stage.setScene(scene);
        stage.show();

        Solitaire.initialisation();
        drawGame(); // Tirage initial de l'état du jeu


        // Gérer les clics de souris sur le volet racine
        root.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                int x = (int) event.getX();
                int y = (int) event.getY();
                System.out.println("Clic gauche détecté à la position (" + x + "," + y + ")");
                Solitaire.handleClick(x, y); // Traite le clic
                drawGame(); // Redessine le jeu après l'action
            }
        });

        // Thread pour l'entrée console (comme suggéré dans le TP4), bien que l'interface graphique soit principale
        // Ceci est davantage destiné au débogage ou à un contrôle alternatif, et non au gameplay principal
        Thread consoleThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Enter 'redraw' to refresh or other commands (not fully implemented):");
                String input = scanner.nextLine();
                if ("redraw".equalsIgnoreCase(input.trim())) {
                    Platform.runLater(() -> {
                        drawGame();
                    });
                }
            }
        });
        consoleThread.setDaemon(true);
        consoleThread.start();
        // Choisir quelle démo exécuter
        // run1(); // Test simple de la classe Carte
        // run2(); // Test du Deck
        // run3(); // Test de la Defausse avec thread
        // run4();
    }

    public static void run1() {
        // Test simple de la classe Carte
        Carte carte1 = new Carte(Carte.DAME, Carte.COEUR);
        carte1.retourne();
        carte1.draw(gc, 10, 10);

        Carte carte2 = new Carte(8, Carte.PIQUE);
        carte2.retourne();
        carte2.draw(gc, 80, 10);

        Carte carte3 = new Carte(9, Carte.TREFLE);
        carte3.retourne();
        carte3.draw(gc, 150, 10);

        Carte carte4 = new Carte(Carte.VALET, Carte.CARREAU);
        carte4.retourne();
        //carte4.draw(gc, 400, 400);

        Carte carte5 = new Carte(5, Carte.COEUR);
        //carte5.draw(gc, 500, 100); // Carte non retournée

       //ma pile
        /*PileCartes pile= new PileCartes(100,100);
        pile.ajouteCarte(new Carte(Carte.DAME, Carte.COEUR));
        pile.ajouteCarte(new Carte(10, Carte.CARREAU));
        pile.ajouteCarte(new Carte(9, Carte.TREFLE));
        pile.top().retourne();
        pile.draw(gc);*/

    }

    public static void run2() {
        // Test du Deck
        Deck monDeck = new Deck(100, 100);
        monDeck.shuffle();
        monDeck.top().retourne();
        monDeck.draw(gc);
    }

    public static void run3() {
        // Création du Deck et de la défausse
        Deck monDeck = new Deck(20, 20);
        Defausse defausse = new Defausse(80, 80);
        // Mélange et affichage initial du Deck et de la défausse
        monDeck.shuffle();
        monDeck.top().retourne();
        monDeck.draw(gc);
        // Création d'un thread pour gérer les entrées de la console
        // Création d'un thread pour gérer les entrées de la console
        Thread consoleThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Entrez une commande (par exemple 'deplacer'): ");
            String input = scanner.nextLine();
            System.out.println(input);


            Platform.runLater(() -> { // Mise à jour de l'interface graphique après l'action
                Carte carte = monDeck.pop();
                defausse.ajouteCarte(carte);
                monDeck.top().retourne();
                defausse.draw(gc);
                monDeck.draw(gc);
            });

            input = scanner.nextLine();
            System.out.println(input);
            Platform.runLater(() -> { // Mise à jour de l'interface graphique après l'action
                Carte carte = monDeck.pop();
                defausse.ajouteCarte(carte);
                monDeck.top().retourne();
                defausse.draw(gc);
                monDeck.draw(gc);});
        });
        consoleThread.setDaemon(true);
        consoleThread.start();

    }



    // test des piles sur la table
    public static void run4(){
        ArrayList<PilesTable> toutesPiles = new ArrayList<PilesTable>();
        for (int i = 0; i < NOMBRE_PILES; i++)
            toutesPiles.add(new PilesTable(MARGE_GAUCHE + (Carte.LARGEUR + 5) * i, Carte.HAUTEUR + 5 + MARGE_HAUTE, i+1));
        for (int i = 0; i < NOMBRE_PILES; i++)
            toutesPiles.get(i).draw(gc);


        // Test PileTable
        PilesTable pileTable = new PilesTable(100, 100, 0); // Pile vide
        System.out.println(pileTable.peutPoser(new Carte(13, Carte.PIQUE))); // true (Roi)
        System.out.println(pileTable.peutPoser(new Carte(10, Carte.COEUR))); // false

        // Test PileOrdonnee
        PileOrdonnee pileOrdonnee = new PileOrdonnee(200, 200);
        pileOrdonnee.ajouteCarte(new Carte(1, Carte.COEUR)); // As de Cœur
        System.out.println(pileOrdonnee.peutPoser(new Carte(2, Carte.COEUR))); // true
        System.out.println(pileOrdonnee.peutPoser(new Carte(2, Carte.PIQUE))); // false (mauvaise couleur)
    }

    private void drawGame() {
        // Effacer le canva avant de redessiner
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        Solitaire.drawAllPiles(gc);
    }
}
