package game;

import piles.IPile;
import player.IPlayer;

/**
 * Controls the main game loop for PointSalad.
 * Implements IGameLoop interface.
 */
public class SaladGameLoop implements IGameLoop {
    IGame saladGame;

    /**
     * Constructs a new SaladGameLoop with the specified game instance.
     *
     * @param game The game instance to manage
     */
    public SaladGameLoop(IGame game) {
        this.saladGame = game;
    }

    /**
     * Runs the main game loop until all cards are used.
     * Handles turn order between human and bot players.
     * Calls the result handler to finish the game and determine the winner.
     */
    public void runLoop() {
        int currentPlayer = (int) (Math.random() * (saladGame.getPlayers().size()));
        boolean keepPlaying = true;
        ITurnHandler humanHandler = new SaladHumanTurnHandler(this.saladGame);
        ITurnHandler botHandler = new SaladBotTurnHandler(this.saladGame);
        IGameResultHandler resultHandler = new SaladResultHandler(this.saladGame);

        while(keepPlaying) {
            IPlayer thisPlayer = saladGame.getPlayers().get(currentPlayer);
            boolean stillAvailableCards = false;

            // Check if any piles still have cards
            for(IPile p: saladGame.getGamePiles()) {
                if(!p.isEmpty()) {
                    stillAvailableCards = true;
                    break;
                }
            }

            if(!stillAvailableCards) {
                keepPlaying = false;
                break;
            }

            // Handle turn based on player type
            if(!thisPlayer.isBot()) {
                humanHandler.handleTurn(thisPlayer);
            } else {
                botHandler.handleTurn(thisPlayer);
            }

            // Advance to next player
            if(currentPlayer == saladGame.getPlayers().size()-1) {
                currentPlayer = 0;
            } else {
                currentPlayer++;
            }
        }

        System.out.println("No more cards left");
        resultHandler.handleResult();
    }
}
