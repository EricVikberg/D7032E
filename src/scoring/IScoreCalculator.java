package scoring;

import card.ICard;
import player.IPlayer;
import java.util.ArrayList;

/**
 * Defines the interface for score calculation in PointSalad.
 * Implementations should handle all scoring criteria defined in the game rules.
 */
public interface IScoreCalculator {

    /**
     * Calculates the total score for a player's hand based on all criteria cards.
     *
     * @param hand The player's hand of cards to be scored
     * @param thisPlayer The player being scored
     * @param players All players in the game (for relative scoring conditions)
     * @return The calculated total score
     */
    int calculateScore(ArrayList<ICard> hand, IPlayer thisPlayer, ArrayList<IPlayer> players);
}