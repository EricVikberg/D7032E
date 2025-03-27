package piles;

import card.ICard;
import java.util.ArrayList;

/**
 * Represents a pile of cards in the PointSalad game.
 * Implements IPile interface.
 */
public class SaladPile implements IPile {
    private ArrayList<ICard> cards = new ArrayList<ICard>();
    private ICard[] veggieCards = new ICard[2];

    /**
     * Constructs a new SaladPile with the given cards.
     * The first two cards become face-up vegetable cards.
     *
     * @param cards The initial set of cards for this pile
     */
    public SaladPile(ArrayList<ICard> cards) {
        this.cards = cards;
        this.veggieCards[0] = cards.remove(0);
        this.veggieCards[1] = cards.remove(0);
        this.veggieCards[0].setCriteriaSideUp(false);
        this.veggieCards[1].setCriteriaSideUp(false);
    }

    /**
     * Gets the top point card from this pile.
     * If pile is empty, takes a card from the largest other pile.
     *
     * @param piles All game piles for potential card borrowing
     * @return The top point card, or null if none available
     */
    public ICard getPointCard(ArrayList<IPile> piles) {
        if (cards.isEmpty()) {
            // Find largest other pile
            int biggestPileIndex = -1;
            int biggestSize = 0;

            for (int i = 0; i < piles.size(); i++) {
                if (i != piles.indexOf(this) && piles.get(i).getCards().size() > biggestSize) {
                    biggestSize = piles.get(i).getCards().size();
                    biggestPileIndex = i;
                }
            }

            if (biggestSize > 1) {
                cards.add(piles.get(biggestPileIndex).getCards().remove(biggestSize - 1));
            } else {
                return null;
            }
        }
        return cards.get(0);
    }

    /**
     * Gets all cards in this pile (excluding face-up vegetable cards).
     *
     * @return ArrayList of ICard objects
     */
    @Override
    public ArrayList<ICard> getCards() {
        return this.cards;
    }

    /**
     * Gets a specific vegetable card from this pile.
     *
     * @param index Which vegetable card to get (0 or 1)
     * @return The requested vegetable card
     * @throws IndexOutOfBoundsException if index is invalid
     */
    public ICard getVeggieCard(int index) {
        if (index < 0 || index >= veggieCards.length) {
            throw new IndexOutOfBoundsException();
        }
        return veggieCards[index];
    }

    /**
     * Checks if this pile is completely empty.
     *
     * @return true if no cards remain, false otherwise
     */
    public boolean isEmpty() {
        return cards.isEmpty() && veggieCards[0] == null && veggieCards[1] == null;
    }

    /**
     * Removes and returns the top point card from this pile.
     * If pile is empty, takes a card from the largest other pile first.
     *
     * @param piles All game piles for potential card borrowing
     * @return The removed point card, or null if none available
     */
    public ICard buyPointCard(ArrayList<IPile> piles) {
        if (cards.isEmpty()) {
            int biggestPileIndex = 0;
            int biggestSize = 0;

            for (int i = 0; i < piles.size(); i++) {
                if (i != piles.indexOf(this) && piles.get(i).getCards().size() > biggestSize) {
                    biggestSize = piles.get(i).getCards().size();
                    biggestPileIndex = i;
                }
            }

            if (biggestSize > 1) {
                cards.add(piles.get(biggestPileIndex).getCards().remove(
                        piles.get(biggestPileIndex).getCards().size() - 1));
            } else {
                return null;
            }
        }
        return cards.remove(0);
    }

    /**
     * Removes and returns a vegetable card from this pile.
     * Replaces it with a new card from the point cards if available.
     *
     * @param index Which vegetable card to buy
     * @param piles All game piles for potential card borrowing
     * @return The removed vegetable card
     */
    public ICard buyVeggieCard(int index, ArrayList<IPile> piles) {
        ICard aCard = veggieCards[index];
        if(cards.size() <= 1) {
            int biggestPileIndex = 0;
            int biggestSize = 0;

            for (int i = 0; i < piles.size(); i++) {
                if (i != piles.indexOf(this) && piles.get(i).getCards().size() > biggestSize) {
                    biggestSize = piles.get(i).getCards().size();
                    biggestPileIndex = i;
                }
            }
            if(biggestSize > 1) {
                cards.add(piles.get(biggestPileIndex).getCards().remove(
                        piles.get(biggestPileIndex).getCards().size()-1));
                veggieCards[index] = cards.remove(0);
                veggieCards[index].setCriteriaSideUp(false);
            } else {
                veggieCards[index] = null;
            }
        } else {
            veggieCards[index] = cards.remove(0);
            veggieCards[index].setCriteriaSideUp(false);
        }

        return aCard;
    }
}