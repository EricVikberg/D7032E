package game;

import player.IPlayer;

/**
 * Handles turn execution for a player (human or bot).
 */
public interface ITurnHandler {
    /**
     * Executes a complete turn for the specified player.
     *
     * @param thisPlayer The player whose turn should be handled
     */
    void handleTurn(IPlayer thisPlayer);
}