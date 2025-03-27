package piles;

import card.ICard;
import java.util.ArrayList;

/**
 * Represents a pile of game cards with both point cards and vegetable cards.
 * Defines operations for card access and management.
 */
public interface IPile {
    /**
     * Gets the top point card without removing it.
     * May draw from other piles if this pile is empty.
     *
     * @param piles All available piles for card replenishment
     * @return The top point card, or null if no cards available
     */
    ICard getPointCard(ArrayList<IPile> piles);

    /**
     * Gets all cards in the point card stack
     *
     * @return List of point cards in the pile
     */
    ArrayList<ICard> getCards();

    /**
     * Gets a specific vegetable card from the face-up market.
     *
     * @param index Which vegetable card to get
     * @return The requested vegetable card
     */
    ICard getVeggieCard(int index);

    /**
     * Checks if the pile is completely empty (no point or vegetable cards).
     *
     * @return true if both point stack and vegetable cards are empty
     */
    boolean isEmpty();

    /**
     * Removes and returns the top point card from the pile.
     * May draw from other piles if this pile is empty.
     *
     * @param piles All available piles for card replenishment
     * @return The removed point card, or null if no cards available
     */
    ICard buyPointCard(ArrayList<IPile> piles);

    /**
     * Removes and returns a vegetable card from the market.
     * Replenishes from point cards if available.
     *
     * @param index Which vegetable card to remove
     * @param piles All available piles for card replenishment
     * @return The removed vegetable card
     */
    ICard buyVeggieCard(int index, ArrayList<IPile> piles);
}