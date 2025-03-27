package player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Represents a bot player in the PointSalad game.
 * Extends the base Player class with bot-specific behavior.
 */
public class BotPlayer extends Player {
    /**
     * Constructs a new BotPlayer with the specified parameters.
     *
     * @param playerID Unique player identifier
     * @param online Whether the player is connected online
     * @param isBot Flag indicating this is a bot player
     * @param connection Network socket connection (null for local bots)
     * @param inFromClient Input stream (null for local bots)
     * @param outToClient Output stream (null for local bots)
     */
    public BotPlayer(int playerID, boolean online, boolean isBot,
                     Socket connection, ObjectInputStream inFromClient,
                     ObjectOutputStream outToClient) {
        super(playerID, online, isBot, connection, inFromClient, outToClient);
    }
}