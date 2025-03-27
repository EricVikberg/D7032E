package game;

import card.ICard;
import java.util.ArrayList;

/**
 * Provides utility methods for displaying game information.
 */
public interface IGameUtils {
    /**
     * Generates a formatted string representation of a player's hand.
     * Shows:
     * - All criteria cards with their rules
     * - Count of each vegetable type
     *
     * @param hand The player's hand to display
     * @return Formatted string showing hand contents
     */
    String displayHand(ArrayList<ICard> hand);
}