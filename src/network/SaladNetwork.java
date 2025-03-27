package network;

import player.BotPlayer;
import player.HumanPlayer;
import player.IPlayer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles network communication for multiplayer PointSalad games.
 * Implements INetwork interface for server and client functionality.
 */
public class SaladNetwork implements INetwork {
    private ServerSocket serverSocket;
    private ArrayList<Socket> clientSockets = new ArrayList<>();

    /**
     * Sets up the game server and initializes players.
     *
     * @param numberPlayers Number of human players
     * @param numberOfBots Number of bot players
     * @param players List to populate with player instances
     * @return Updated list of players
     * @throws Exception if network setup fails
     */
    public ArrayList<IPlayer> server(int numberPlayers, int numberOfBots, ArrayList<IPlayer> players) throws Exception {
        // Add local player
        players.add(new HumanPlayer(0, false, false, null, null, null));

        // Add bots
        for (int i = numberPlayers; i < numberOfBots + numberPlayers; i++) {
            players.add(new BotPlayer(i, false, true, null, null, null));
        }

        // Setup server if multiplayer
        if (numberPlayers > 1) {
            try {
                serverSocket = new ServerSocket(2048);
            } catch (IOException e) {
                System.err.println("Error while creating server socket or accepting connections: " + e.getMessage());
                throw e;
            }
        }

        // Connect remote players
        for (int i = numberOfBots + 1; i < numberPlayers + numberOfBots; i++) {
            try {
                Socket connectionSocket = serverSocket.accept();
                ObjectInputStream inFromClient = new ObjectInputStream(connectionSocket.getInputStream());
                ObjectOutputStream outToClient = new ObjectOutputStream(connectionSocket.getOutputStream());
                players.add(new HumanPlayer(i, true, false, connectionSocket, inFromClient, outToClient));
                clientSockets.add(connectionSocket);
                System.out.println("Connected to player " + i);
                outToClient.writeObject("You connected to the server as player " + i + "\n");
            } catch (IOException e) {
                System.err.println("Error while creating server socket or accepting connections: " + e.getMessage());
                throw e;
            }
        }
        return players;
    }

    /**
     * Connects to a game server as a client.
     *
     * @param ipAddress Server IP address to connect to
     * @throws Exception if connection fails
     */
    public void client(String ipAddress) throws Exception {
        Socket aSocket = new Socket(ipAddress, 2048);
        ObjectOutputStream outToServer = new ObjectOutputStream(aSocket.getOutputStream());
        ObjectInputStream inFromServer = new ObjectInputStream(aSocket.getInputStream());
        String nextMessage = "";

        System.out.println("Inne i clienten");
        while (!nextMessage.contains("winner")) {
            nextMessage = (String) inFromServer.readObject();
            System.out.println(nextMessage);
            if (nextMessage.contains("Take") || nextMessage.contains("into")) {
                Scanner in = new Scanner(System.in);
                String userInput = in.nextLine();
                outToServer.writeObject(userInput);
            }
        }
    }

    /**
     * Closes all network resources (sockets and connections).
     */
    public void close() {
        try {
            // Close all client sockets
            for (Socket socket : clientSockets) {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
            }
            // Close server socket
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.out.println("Network resources closed.");
        } catch (IOException e) {
            System.err.println("Error while closing network resources: " + e.getMessage());
        }
    }
}