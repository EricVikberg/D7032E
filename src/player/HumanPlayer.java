package player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Represents a human player in the PointSalad game.
 * Extends the base Player class with human-specific behavior.
 */
public class HumanPlayer extends Player {
    /**
     * Constructs a new HumanPlayer with the specified parameters.
     *
     * @param playerID Unique player identifier
     * @param online Whether the player is connected online
     * @param isBot Flag indicating this is not a bot player
     * @param connection Network socket connection (null for local players)
     * @param inFromClient Input stream (null for local players)
     * @param outToClient Output stream (null for local players)
     */
    public HumanPlayer(int playerID, boolean online, boolean isBot,
                       Socket connection, ObjectInputStream inFromClient,
                       ObjectOutputStream outToClient) {
        super(playerID, online, isBot, connection, inFromClient, outToClient);
    }
}