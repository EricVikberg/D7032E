package game;

import card.ICardCounter;
import card.SaladCardCounter;
import market.IMarket;
import market.IMarketView;
import market.SaladMarket;
import market.SaladMarketView;
import network.INetwork;
import network.SaladNetwork;
import piles.IPile;
import piles.IPileInitializer;
import piles.SaladPileInitializer;
import player.IPlayer;
import scoring.IScoreCalculator;
import scoring.SaladScoreCalc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The main game implementation for PointSalad.
 * Manages game state and core game functionality.
 * Implements the IGame interface.
 */
public class SaladGame implements IGame {
    private String[] input;
    private ArrayList<IPlayer> players = new ArrayList<>();
    private IPileInitializer piles;
    private IMarket market;
    private IMarketView marketView;
    private ICardCounter cardCounter;
    private IScoreCalculator scoreCalculator;
    private INetwork network;

    /**
     * Constructs a new SaladGame instance with the specified arguments.
     *
     * @param args Command line arguments for game configuration
     * @throws IOException if there's an error initializing the game
     */
    public SaladGame(String[] args) throws IOException {
        this.network = new SaladNetwork();
        this.input = args;

        initializePlayers();

        this.piles = new SaladPileInitializer(players.size());
        this.market = new SaladMarket(piles.getPiles());
        this.marketView = new SaladMarketView(market);
        this.cardCounter = new SaladCardCounter();
        this.scoreCalculator = new SaladScoreCalc(cardCounter);
        System.out.println(marketView.printMarket());
    }

    /**
     * Gets the list of players in the game.
     *
     * @return ArrayList of IPlayer objects
     */
    @Override
    public ArrayList<IPlayer> getPlayers() {
        return this.players;
    }


    /**
     * Gets the game piles (card stacks).
     *
     * @return ArrayList of IPile objects
     */
    public ArrayList<IPile> getGamePiles() {
        return this.piles.getPiles();
    }

    /**
     * Gets the market view for displaying available cards.
     *
     * @return IMarketView instance
     */
    public IMarketView getMarketView() {
        return this.marketView;
    }

    /**
     * Gets the card counter utility.
     *
     * @return ICardCounter instance
     */
    public ICardCounter getCardCounter() {
        return this.cardCounter;
    }

    /**
     * Gets the score calculator.
     *
     * @return IScoreCalculator instance
     */
    public IScoreCalculator getScoreCalculator() {
        return this.scoreCalculator;
    }

    /**
     * Sends a message to all non-bot players.
     *
     * @param message The message to send
     */
    public void sendToAllPlayers(String message) {
        for (IPlayer player : players) {
            if(!player.isBot()) {
                player.sendMessage(message);
            }
        }
    }

    /**
     * Initializes players based on input or user prompts.
     * Handles both local and network player setup.
     */
    private void initializePlayers() {
        Scanner in = new Scanner(System.in);
        int numberPlayers = 0;
        int numberOfBots = 0;

        if (this.input.length == 0) {
            while (true) {
                System.out.println("Please enter the number of players (1-6): ");
                numberPlayers = in.nextInt();

                System.out.println("Please enter the number of bots (0-5): ");
                numberOfBots = in.nextInt();
                break;
            }
        } else {
            if(this.input[0].matches("\\d+")) {
                numberPlayers = Integer.parseInt(this.input[0]);
                numberOfBots = Integer.parseInt(this.input[1]);
            }
            else {
                try {
                    network.client(this.input[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (numberPlayers + numberOfBots > 6) {
            System.out.println("You've entered too many players and bots!");
            throw new IllegalArgumentException("You've entered too many players and bots");
        } else if (numberPlayers + numberOfBots < 2) {
            System.out.println("You've entered too few players and bots!");
            throw new IllegalArgumentException("You've entered too few players and bots");
        }

        try {
            this.players = network.server(numberPlayers, numberOfBots, players);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cleans up game resources, including network connections.
     */
    public void close() {
        if (network != null) {
            ((SaladNetwork) network).close();
        }
        System.out.println("Game resources closed.");
    }
}
