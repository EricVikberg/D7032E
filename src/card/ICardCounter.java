package card;

import java.util.ArrayList;

/**
 * Provides counting operations for vegetable cards in a player's hand.
 */
public interface ICardCounter {
    /**
     * Counts occurrences of a specific vegetable in the hand.
     * Only counts cards with vegetable side up.
     *
     * @param hand The player's hand to search through
     * @param vegetable The vegetable type to count
     * @return Number of matching vegetable cards found
     */
    int countVegetables(ArrayList<ICard> hand, Vegetable vegetable);

    /**
     * Counts all vegetable cards in the hand (any type).
     * Only counts cards with vegetable side up.
     *
     * @param hand The player's hand to search through
     * @return Total number of vegetable cards found
     */
    int countTotalVegetables(ArrayList<ICard> hand);
}