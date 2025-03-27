package game;

import java.util.ArrayList;
import card.ICardCounter;
import market.IMarketView;
import piles.IPile;
import player.IPlayer;
import scoring.IScoreCalculator;

/**
 * Represents the core game state and provides access to game components.
 * Serves as the central interface for game operations.
 */
public interface IGame {
    /**
     * Gets all players currently in the game.
     *
     * @return List of all players (human and bot)
     */
    ArrayList<IPlayer> getPlayers();

    /**
     * Gets all card piles in the game.
     *
     * @return List of all game piles
     */
    ArrayList<IPile> getGamePiles();

    /**
     * Gets the market view for displaying available cards.
     *
     * @return The market view instance
     */
    IMarketView getMarketView();

    /**
     * Sends a message to all human players in the game.
     *
     * @param message The message to broadcast
     */
    void sendToAllPlayers(String message);

    /**
     * Gets the card counter utility.
     *
     * @return The card counter instance
     */
    ICardCounter getCardCounter();

    /**
     * Gets the score calculator.
     *
     * @return The score calculator instance
     */
    IScoreCalculator getScoreCalculator();
}