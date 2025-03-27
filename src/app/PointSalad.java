package app;

import game.IGame;
import game.IGameLoop;
import game.SaladGame;
import game.SaladGameLoop;

import java.io.IOException;

/**
 * The main entry point for the PointSalad game application.
 * Initializes the game components and manages the game loop.
 */
public class PointSalad {
    /**
     * Constructs a new PointSalad game instance.
     *
     * @param input Command line arguments for game configuration
     * @throws IOException if there's an error initializing the game
     */
    public PointSalad(String[] input) throws IOException {
        System.out.println("Start game");
        IGame game = new SaladGame(input);
        IGameLoop gameLoop = new SaladGameLoop(game);
        gameLoop.runLoop();
    }

    /**
     * Main entry point for the application.
     *
     * @param args Command line arguments
     * @throws IOException if there's an error starting the game
     */
    public static void main(String[] args) throws IOException {
        PointSalad game = new PointSalad(args);
    }
}