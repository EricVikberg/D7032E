package player;

import card.ICard;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Abstract base class representing a player in the PointSalad game.
 * Implements IPlayer interface and provides common functionality.
 */
public abstract class Player implements IPlayer {
    protected int playerID;
    protected boolean online;
    protected boolean isBot;
    protected Socket connection;
    protected ObjectInputStream inFromClient;
    protected ObjectOutputStream outToClient;
    Scanner in = new Scanner(System.in);
    protected ArrayList<ICard> hand = new ArrayList<ICard>();
    protected int score = 0;

    /**
     * Constructs a new Player with the specified parameters.
     *
     * @param playerID Unique player identifier
     * @param online Whether the player is connected online
     * @param isBot Whether the player is a bot
     * @param connection Network socket connection
     * @param inFromClient Input stream for network communication
     * @param outToClient Output stream for network communication
     */
    public Player(int playerID, boolean online, boolean isBot,
                  Socket connection, ObjectInputStream inFromClient,
                  ObjectOutputStream outToClient) {
        this.playerID = playerID;
        this.connection = connection;
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
        this.isBot = isBot;
        this.online = online;
    }

    /**
     * Sends a message to the player.
     *
     * @param message The message to send
     */
    public void sendMessage(Object message) {
        if(online) {
            try {outToClient.writeObject(message);} catch (Exception e) {}
        } else if(!isBot){
            System.out.println(message);
        }
    }

    /**
     * Reads a message from the player.
     *
     * @return The received message
     */
    public String readMessage() {
        String word = "";
        if(online)
            try{word = (String) inFromClient.readObject();} catch (Exception e){}
        else
            try {word=in.nextLine();} catch(Exception e){}
        return word;
    }

    /**
     * Gets the player's unique ID.
     *
     * @return The player ID
     */
    public int getPlayerID() {
        return this.playerID;
    }

    /**
     * Gets the player's current hand of cards.
     *
     * @return ArrayList of ICard objects
     */
    public ArrayList<ICard> getHand() {
        return this.hand;
    }

    /**
     * Gets the player's current score.
     *
     * @return The player's score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Sets the player's score.
     *
     * @param score The new score value
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Updates the player's network connection.
     *
     * @param connection New socket connection
     * @param inFromClient New input stream
     * @param outToClient New output stream
     */
    public void updateConnection(Socket connection, ObjectInputStream inFromClient,
                                 ObjectOutputStream outToClient) {
        this.connection = connection;
        this.inFromClient = inFromClient;
        this.outToClient = outToClient;
    }

    /**
     * Checks if this player is a bot.
     *
     * @return true if the player is a bot, false otherwise
     */
    public boolean isBot() {
        return this.isBot;
    }
}