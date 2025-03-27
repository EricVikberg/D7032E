package game;

import player.IPlayer;

/**
 * Handles the end-game scoring and winner determination.
 * Implements IGameResultHandler interface.
 */
public class SaladResultHandler implements IGameResultHandler {
    private IGame game;
    private IGameUtils handDisplayer;

    /**
     * Constructs a new SaladResultHandler with the specified game instance.
     *
     * @param game The current game instance
     */
    public SaladResultHandler(IGame game) {
        this.game = game;
        this.handDisplayer = new SaladUtils(this.game);
    }

    /**
     * Calculates and displays scores for all players, then determines and announces the winner.
     * Shows each player's hand and score, followed by the winner announcement.
     */
    public void handleResult() {
        game.sendToAllPlayers("\n-------------------------------------- CALCULATING SCORES --------------------------------------\n");

        // Calculate and display scores for each player
        for(IPlayer player : game.getPlayers()) {
            game.sendToAllPlayers("Player " + player.getPlayerID() + "'s hand is: \n" +
                    handDisplayer.displayHand(player.getHand()));
            player.setScore(game.getScoreCalculator().calculateScore(
                    player.getHand(), player, game.getPlayers()));
            game.sendToAllPlayers("\nPlayer " + player.getPlayerID() + "'s score is: " + player.getScore());
        }

        // Determine winner
        int maxScore = 0;
        int playerID = 0;
        for(IPlayer player : game.getPlayers()) {
            if(player.getScore() > maxScore) {
                maxScore = player.getScore();
                playerID = player.getPlayerID();
            }
        }

        // Announce winner
        for(IPlayer player : game.getPlayers()) {
            if(player.getPlayerID() == playerID) {
                player.sendMessage("\nCongratulations! You are the winner with a score of " + maxScore);
            } else {
                player.sendMessage("\nThe winner is player " + playerID + " with a score of " + maxScore);
            }
        }
    }
}