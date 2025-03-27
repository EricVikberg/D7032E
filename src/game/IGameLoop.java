package game;

/**
 * Controls the main game loop and turn sequence.
 */
public interface IGameLoop {
    /**
     * Starts and runs the main game loop until completion.
     * Handles:
     * - Player turn order
     * - Game state updates
     * - End game conditions
     */
    void runLoop();
}