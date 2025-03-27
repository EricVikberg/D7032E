package network;

import player.IPlayer;
import java.util.ArrayList;

/**
 * Defines the network operations for multiplayer game functionality.
 * Handles both server and client-side network communication.
 */
public interface INetwork {
    /**
     * Sets up the game server and initializes players.
     *
     * @param numberPlayers Number of human players to initialize
     * @param numberOfBots Number of bot players to initialize
     * @param players List to populate with initialized player instances
     * @return Updated list of players with network connections established
     * @throws Exception if network setup fails, including:
     *         - IOException if server socket cannot be created
     */
    ArrayList<IPlayer> server(int numberPlayers, int numberOfBots,
                              ArrayList<IPlayer> players) throws Exception;

    /**
     * Connects to a game server as a client.
     *
     * @param ipAddress The server IP address to connect to
     * @throws Exception if connection fails, including:
     *         - IOException if connection cannot be established
     */
    void client(String ipAddress) throws Exception;
}