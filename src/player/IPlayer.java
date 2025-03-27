package player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import card.ICard;

/**
 * Represents a game player (human or bot) with their game state and actions.
 */
public interface IPlayer {
    /**
     * Sends a message to the player.
     *
     * @param message The message to send (can be String or serializable object)
     */
    void sendMessage(Object message);

    /**
     * Reads a message from the player.
     *
     * @return The received message as a String
     */
    String readMessage();

    /**
     * Gets the player's unique identifier.
     *
     * @return The player's ID number
     */
    int getPlayerID();

    /**
     * Gets the player's current hand of cards.
     *
     * @return List of cards in the player's hand
     */
    ArrayList<ICard> getHand();

    /**
     * Gets the player's current score.
     *
     * @return The player's score
     */
    int getScore();

    /**
     * Sets the player's score
     * @param score The new score value

     */
    void setScore(int score);

    /**
     * Checks if this player is a bot.
     *
     * @return true if the player is a bot, false if human
     */
    boolean isBot();
}