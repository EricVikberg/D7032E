package game;

/**
 * Handles end-game scoring and winner determination.
 */
public interface IGameResultHandler {
    /**
     * Calculates final scores for all players and announces the winner.
     * Should:
     * 1. Display each player's hand and score
     * 2. Identify the highest scoring player
     * 3. Announce results to all players
     */
    void handleResult();
}