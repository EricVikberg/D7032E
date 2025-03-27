package card;

import java.util.ArrayList;

/**
 * Implements ICardCounter to provide counting functionality for vegetable cards.
 * Counts specific vegetables or all vegetables in a player's hand.
 */
public class SaladCardCounter implements ICardCounter {
    /**
     * Constructs a new SaladCardCounter.
     */
    public SaladCardCounter() {
    }

    /**
     * Counts occurrences of a specific vegetable in the hand.
     * Only counts cards with vegetable side up.
     *
//     * @param hand The player's hand of cards
     * @param vegetable The vegetable type to count
     * @return Number of matching vegetable cards found
     */
    public int countVegetables(ArrayList<ICard> hand, Vegetable vegetable) {
        int count = 0;
        for (ICard card : hand) {
            if (!card.getCriteriaSideUp() && card.getVegetable() == vegetable) {
                count++;
            }
        }
        return count;
    }

    /**
     * Counts all vegetable cards in the hand (any type).
     * Only counts cards with vegetable side up.
     *
     * @param hand The player's hand of cards
     * @return Total number of vegetable cards found
     */
    public int countTotalVegetables(ArrayList<ICard> hand) {
        int count = 0;
        for (ICard card : hand) {
            if (!card.getCriteriaSideUp()) {
                count++;
            }
        }
        return count;
    }
}